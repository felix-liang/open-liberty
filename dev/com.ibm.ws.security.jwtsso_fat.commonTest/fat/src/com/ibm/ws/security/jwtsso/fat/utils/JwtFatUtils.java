/*******************************************************************************
 * Copyright (c) 2018, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.jwtsso.fat.utils;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.ibm.websphere.simplicity.log.Log;
import com.ibm.ws.security.fat.common.utils.FatStringUtils;
import com.ibm.ws.security.fat.common.utils.ServerFileUtils;
import com.ibm.ws.security.fat.common.web.WebResponseUtils;

import componenttest.custom.junit.runner.RepeatTestFilter;
import componenttest.rules.repeater.JakartaEE9Action;
import componenttest.topology.impl.LibertyServer;

public class JwtFatUtils extends ServerFileUtils {

    protected static Class<?> thisClass = JwtFatUtils.class;

    public JsonObject extractJwtPrincipalFromResponse(Object response) throws Exception {
        String method = "extractJwtPrincipalFromResponse";
        try {
            String fullResponse = WebResponseUtils.getResponseText(response);
            return extractJwtPrincipalFromString(fullResponse);
        } catch (Exception e) {
            Log.error(thisClass, method, e);
            throw new Exception("Failed to extract the JWT principal from the provided response: " + e);
        }
    }

    public JsonObject extractJwtPrincipalFromString(String response) throws Exception {
        String method = "extractJwtPrincipalFromString";
        try {
            String jwtPrincipalRegex = "getUserPrincipal: (\\{.+\\})";
            String jwtString = FatStringUtils.extractRegexGroup(response, jwtPrincipalRegex);
            Log.info(thisClass, method, "Got JWT string from response: " + jwtString);

            JsonReader reader = Json.createReader(new StringReader(jwtString));
            return reader.readObject();
        } catch (Exception e) {
            Log.error(thisClass, method, e);
            throw new Exception("Failed to extract the JWT principal from the provided string: " + e);
        }
    }

    public void updateFeatureFileForEE9(LibertyServer server) throws Exception {

        String version = RepeatTestFilter.getMostRecentRepeatAction();
        // try to replace the EE9 noMpJwt repeat instance name with noMpJwt_ee9 - if this is a different repeat action this will do nothing
        version = version.replace(JakartaEE9Action.ID + "_" + JwtFatConstants.NO_MPJWT, JwtFatConstants.NO_MPJWT_EE9);
        // if the EE9 repeat instance is still in the the action, remove it
        updateFeatureFile(server, "jwtSsoFeatures", version.replace(JakartaEE9Action.ID + "_", ""));
        if (JakartaEE9Action.isActive()) {
            updateFeatureFile(server, "featuresWithoutJwtSso", "ee9");
        }

    }
}