/*******************************************************************************
 * Copyright (c) 2011, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.ibm.ws.ejbcontainer.security.jacc_fat;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import com.ibm.websphere.simplicity.log.Log;

import componenttest.custom.junit.runner.FATRunner;
import componenttest.custom.junit.runner.Mode;
import componenttest.custom.junit.runner.Mode.TestMode;

/**
 * Performs testing of EJB pure annotations with a Stateless bean and role mappings in ibm-application-bnd.xml.
 * This tests the class level annotations RunAs (Employee), RolesAllowed (Manager) and DeclareRoles(DeclaredRole01) with a
 * variety of method level annotations. This test invokes SecurityEJBA01Bean methods with a variety of method signatures to insure that
 * annotations are processed correctly with methods of the same name and different signature.
 *
 * This test extends PureAnnA01Base, so it runs the same set of tests in PureAnnA01Test with
 * a variation in the configuration -- this test covers the application-bnd role mappings in ibm-application-bnd.xml while PureAnnA01Test
 * covers application-bnd mappings in server.xml.
 */
@Mode(TestMode.FULL)
@RunWith(FATRunner.class)
public class PureAnnAppBndXMLBindingsInWarEarTest extends EJBAnnTestBase {

    protected static Class<?> logClass = PureAnnAppBndXMLBindingsInWarEarTest.class;

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUp() throws Exception {

        Log.info(logClass, "setUp", "Starting the server....");
        commonSetup(logClass, Constants.SERVER_EJB_BINDINGS,
                    Constants.APPLICATION_SECURITY_EJB_INWAR_EAR_XML, Constants.SERVLET_SECURITY_EJB, Constants.CONTEXT_ROOT_SECURITY_EJB_INWAR_EAR_XML);

    }

    protected TestName getName() {
        return name;
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the DenyAll
     * <LI> annotation at method level overrides the class level RolesAllowed annotation.
     * <LI> This test covers invoking the EJB method denyAll() with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed exception for valid userId in Manager role.
     * <LI>
     * </OL>
     */

    @Test
    public void testPureA01_DenyAll_MethodOverridesClassRolesAllowed_DenyAccess() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=denyAll";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyException(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the DenyAll
     * <LI> annotation at method level overrides the class level RolesAllowed annotation.
     * <LI> This test covers invoking the EJB method denyAllwithParam(String) with single String parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed exception for valid userId in Employee role.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_DenyAllWithParam_MethodOverridesClassRolesAllowed_DenyAccess() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=denyAllwithParam";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyException(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE);
        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the PermitAll
     * <LI> annotation at method level overrides the class level RolesAllowed annotation.
     * <LI> This test covers invoking the EJB method permitAll() with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Employee role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_PermitAll_MethodOverridesClassRolesAllowed_PermitAccessNotClassRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=permitAll";
        String response = generateResponseFromServlet(queryString, Constants.EMPLOYEE_USER, Constants.EMPLOYEE_PWD);
        verifyResponse(response, Constants.EMPLOYEE_USER_PRINCIPAL, Constants.EMPLOYEE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the PermitAll
     * <LI> annotation at method level overrides the class level RolesAllowed annotation.
     * <LI> This test covers invoking the EJB method permitAllwithParam(String) with single String parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid user in no role is allowed access.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_PermitAllwithParam_MethodOverridesClassRolesAllowed_PermitAccess() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=permitAllwithParam";
        String response = generateResponseFromServlet(queryString, Constants.NO_ROLE_USER, Constants.NO_ROLE_USER_PWD);
        verifyResponse(response, Constants.NO_ROLE_USER_PRINCIPAL, Constants.NO_ROLE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_FALSE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at class level controls access when no annotation at method level.
     * <LI> This test covers invoking the EJB method manager() no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Manager role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_Manager_NoMethodAnnClassUsed_PermitAccessManager() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=manager";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyResponse(response, Constants.MANAGER_USER_PRINCIPAL, Constants.MANAGER_USER_IDENTITY, Constants.IS_MANAGER_TRUE, Constants.IS_EMPLOYEE_FALSE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at class level controls access when no annotation at method level.
     * <LI> This test covers invoking the EJB method manager(String) with single String parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> EJBAccessException when user "manager" and "Manager" is required.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_ManagerWithParam_methodRolesAllowedmanagerOverridesClass_DenyAccessManagerRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=managerwithParam";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyExceptionWithUserAndRole(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE, Constants.MANAGER_USER,
                                       Constants.MANAGER_METHOD);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at class level controls access when no annotation at method level.
     * <LI> This test covers invoking the EJB method manaager() with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed for valid user not in a role.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_ManagerNoAuth_NoMethodAnnClassUsed_DenyAccessNoRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=manager";
        String response = generateResponseFromServlet(queryString, Constants.NO_ROLE_USER, Constants.NO_ROLE_USER_PWD);
        verifyExceptionWithUserAndRole(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE, Constants.NO_ROLE_USER,
                                       Constants.MANAGER_METHOD);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at method level overrides the class level annotation.
     * <LI> This test covers invoking the EJB method employee with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Employee role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_Employee_MethodOverrideClassRolesAllowed_PermitAccessSameRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employee";
        String response = generateResponseFromServlet(queryString, Constants.EMPLOYEE_USER, Constants.EMPLOYEE_PWD);
        verifyResponse(response, Constants.EMPLOYEE_USER_PRINCIPAL, Constants.EMPLOYEE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at method level (Employee) overrides class level (Manager)
     * <LI> This test covers invoking the EJB method employee() with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed for Manager role.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_Employee_MethodOverrideClassRolesAllowed_DenyAccessClassRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employee";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyExceptionWithUserAndRole(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE, Constants.MANAGER_USER,
                                       Constants.EMPLOYEE_METHOD);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at method level (Employee) overrides class level (Manager)
     * <LI> This test covers invoking the EJB method emplooyee(String) with single String parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed for "employee" rather than "Employee"
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeWithParam_MethodOverrideClassRolesAllowed_PermitAccessEmployee() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employeewithParam";
        String response = generateResponseFromServlet(queryString, Constants.EMPLOYEE_USER, Constants.EMPLOYEE_PWD);
        verifyResponse(response, Constants.EMPLOYEE_USER_PRINCIPAL, Constants.EMPLOYEE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet. The RolesAllowed
     * <LI> annotation at method level (Employee) overrides class level (Manager).
     * <LI> This test covers invoking the EJB method employee() with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Authorization failed for user in no role when Employee role is required.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeNoAuth_MethodOverrideClassRolesAllowed_DenyAccessNoRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employee";
        String response = generateResponseFromServlet(queryString, Constants.NO_ROLE_USER, Constants.NO_ROLE_USER_PWD);
        verifyExceptionWithUserAndRole(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE, Constants.NO_ROLE_USER,
                                       Constants.EMPLOYEE_METHOD);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the RolesAllowed
     * <LI> annotation at method level (Employee Manager) overrides the class level (Manager).
     * <LI> This test covers invoking the EJB method employeeAndManager() method with no parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Employee role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeAndManager_MethodOverrideClassRolesAllowed_PermitAccessEmployee() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employeeAndManager";
        String response = generateResponseFromServlet(queryString, Constants.EMPLOYEE_USER, Constants.EMPLOYEE_PWD);
        verifyResponse(response, Constants.EMPLOYEE_USER_PRINCIPAL, Constants.EMPLOYEE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the RolesAllowed
     * <LI> annotation at method level (Employee Manager) overrides the class level (Manager).
     * <LI> This test covers invoking the EJB method employeeAndManagerwithParams(String,String) with two String parameters.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Employee role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeAndManagerWithParams_MethodOverrideClassRolesAllowed_PermitAccessEmployee() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employeeAndManagerwithParams";
        String response = generateResponseFromServlet(queryString, Constants.EMPLOYEE_USER, Constants.EMPLOYEE_PWD);
        verifyResponse(response, Constants.EMPLOYEE_USER_PRINCIPAL, Constants.EMPLOYEE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the RolesAllowed
     * <LI> annotation at method level (Employee Manager) overrides the class level (Manager).
     * <LI> This test covers invoking the EJB method employeeAndManagerwithParam(String) with single String parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> A valid userId in Manager role is allowed to access the EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeAndManagerAllWithParam_MethodOverrideClassRolesAllowed_PermitAccessManager() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employeeAndManagerwithParam";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyResponse(response, Constants.MANAGER_USER_PRINCIPAL, Constants.MANAGER_USER_IDENTITY, Constants.IS_MANAGER_TRUE, Constants.IS_EMPLOYEE_FALSE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the RolesAllowed
     * <LI> annotation at method level (Employee Manager) overrides the class level (Manager).
     * <LI> This test covers invoking the EJB method employeeAndManagerwithInt(int) with single integer parameter.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> EJBAccessException when user in no role attempts to access EJB method.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_EmployeeAndManagerwithIntNoAuth_MethodOverrideClassRolesAllowed_DenyAccessNoRole() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=employeeAndManagerwithInt";
        String response = generateResponseFromServlet(queryString, Constants.NO_ROLE_USER, Constants.NO_ROLE_USER_PWD);
        verifyExceptionWithUserAndRole(response, MessageConstants.EJB_ACCESS_EXCEPTION, MessageConstants.JACC_AUTH_DENIED_USER_NOT_GRANTED_REQUIRED_ROLE, Constants.NO_ROLE_USER,
                                       Constants.EMPLOYEE_AND_MANAGER_METHOD);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet where the DeclareRoles
     * <LI> annotation at class level (DeclaredRole01) allows checking of this role.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> isCallerInRole will return false for the Declared_Role user.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_DeclareRoles01() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=declareRoles01";
        String response = generateResponseFromServlet(queryString, Constants.DECLARED_ROLE_USER, Constants.DECLARED_ROLE_USER_PWD);
        verifyResponse(response, Constants.DECLARED_ROLE_USER_PRINCIPAL, Constants.DECLARED_ROLE_USER_IDENTITY, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_FALSE,
                       Constants.IS_DECLARED_ROLE_FALSE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

    /**
     * Verify the following:
     * <OL>
     * <LI> Attempt to access an EJB method injected into a servlet with a class level RunAs (Employee). There is no
     * <LI> annotation at method level in the invoked EJB (SecurityEJBA05Bean) so it is invoked as Manager role. This first
     * <LI> EJB injects and invokes a second EJB (SecurityRunAsBean) which requires Employee role to access the employee method.
     * <LU> The RunAs (Employee) annotation results in the second EJB employee being invoked successfully with run-as user.
     * </OL>
     * <P> Expected Results:
     * <OL>
     * <LI> Manager role allowed access to first EJB method which invokes second EJB that requires Employee role with run-as user.
     * <LI>
     * </OL>
     */
    @Test
    public void testPureA01_RunAsSpecified_PermitAccessEmployee() throws Exception {
        Log.info(logClass, getName().getMethodName(), "**Entering " + getName().getMethodName());

        String queryString = "/SimpleServlet?testInstance=ejb01&testMethod=runAsSpecified";
        String response = generateResponseFromServlet(queryString, Constants.MANAGER_USER, Constants.MANAGER_PWD);
        verifyResponseWithoutDeprecated(response, Constants.RUN_AS_USER_PRINCIPAL, Constants.IS_MANAGER_FALSE, Constants.IS_EMPLOYEE_TRUE);

        Log.info(logClass, getName().getMethodName(), "**Exiting " + getName().getMethodName());
    }

}