#!/bin/bash

# Params:
# CATEGORY:    the category name (e.g. CDI_1)
# GH_EVENT_NAME: the event triggering this workflow. Will be 'pull_request' if a PR triggered it

set +e

FAT_RESULTS=$PWD/fat-results
mkdir $FAT_RESULTS

# Override default LITE mode per-bucket timeout to 45m
FAT_ARGS="-Dfattest.timeout=2700000"

# If this is the special 'MODIFIED_*_MODE' job, figure out which buckets 
# were directly modfied (if any) so they can be launched
if [[ $CATEGORY =~ MODIFIED_.*_MODE ]]; then
  if [[ $CATEGORY == 'MODIFIED_FULL_MODE' ]]; then
    echo "FAT buckets in this job will run in FULL mode"
    # Give FULL mode buckets a 2h timeout each
    FAT_ARGS="-Dfat.test.mode=FULL -Dfattest.timeout=7200000"
  fi
  git diff --name-only HEAD^...HEAD^2 >> modified_files.diff
  echo "Modified files are:"
  cat modified_files.diff
  FAT_BUCKETS=$(sed -n "s/^dev\/\([^\/]*_fat[^\/]*\)\/.*$/\1/p" modified_files.diff | uniq)
  if [[ -z $FAT_BUCKETS ]]; then
    echo "No FATs were directly modfied. Skipping this job."
    exit 0
  fi
else
  # This is a regular category defined in .github/test-categories/
  if [[ ! -f .github/test-categories/$CATEGORY ]]; then
    echo "::error::Test category [$CATEGORY] does not exist. A file containing a list of FAT buckets was not found at .github/test-categories/$CATEGORY";
    exit 1;
  fi
  FAT_BUCKETS=$(cat .github/test-categories/$CATEGORY)
fi

echo -e "\n### Will be running buckets: \n$FAT_BUCKETS"
for FAT_BUCKET in $FAT_BUCKETS
do
  if [[ ! -d "dev/$FAT_BUCKET" ]]; then
    echo "::error::FAT bucket [$FAT_BUCKET] does not exist.";
    exit 1;
  fi
done

# For PR type events, set the git_diff for the change detector tool so unreleated FATs do not run
if [[ $GH_EVENT_NAME == 'pull_request' && ! $CATEGORY =~ MODIFIED_.*_MODE ]]; then
  FAT_ARGS="$FAT_ARGS -Dgit_diff=HEAD^...HEAD^2"
  echo -e "\n### This event is a pull request. Will run FATs with: $FAT_ARGS"
fi

echo -e "\n## Environment dump:"
env
echo -e "\n## Ant version:"
ant -version
echo -e "\n## Java version:"
java -version

unzip -q openliberty-image.zip
cd dev
chmod +x gradlew
chmod 777 build.image/wlp/bin/*
echo "org.gradle.daemon=false" >> gradle.properties

echo -e "\n### Build com.ibm.ws.componenttest and fattest.simplicity"
mkdir -p gradle/fats/ && touch setup.gradle.log
./gradlew :cnf:initialize :com.ibm.ws.componenttest:build :fattest.simplicity:build > gradle/fats/setup.gradle.log

for FAT_BUCKET in $FAT_BUCKETS
do
  echo -e "\n### BEGIN running FAT bucket $FAT_BUCKET with FAT_ARGS=$FAT_ARGS"
  BUCKET_PASSED=true
  mkdir -p gradle/fats/ && touch $FAT_BUCKET.gradle.log
  ./gradlew :$FAT_BUCKET:buildandrun $FAT_ARGS > gradle/fats/$FAT_BUCKET.gradle.log || BUCKET_PASSED=false
  OUTPUT_DIR=$FAT_BUCKET/build/libs/autoFVT/output
  RESULTS_DIR=$FAT_BUCKET/build/libs/autoFVT/results
  mkdir -p $OUTPUT_DIR
  if $BUCKET_PASSED; then
    echo "The bucket $FAT_BUCKET passed.";
    touch "$OUTPUT_DIR/passed.log";
  else
    echo "::error::The bucket $FAT_BUCKET failed.";
    touch "$OUTPUT_DIR/fail.log";
  fi
  echo -e "\n### Collecing fat results in $FAT_RESULTS"
  for f in $RESULTS_DIR/junit/TEST-*.xml
  do 
      if cp $f $FAT_RESULTS &> /dev/null
      then 
          : #If copy successful do nothing
      else 
          # Otherwise, unit test task may have run, but no results were producted
          echo "No fat results for $FAT_BUCKET"
      fi
  done
  echo -e "\n### END running FAT bucket $FAT_BUCKET";
done

echo "Done running all FAT buckets."

