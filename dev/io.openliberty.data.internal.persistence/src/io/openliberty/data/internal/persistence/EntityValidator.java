/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
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
package io.openliberty.data.internal.persistence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Abstraction for a Jakarta Validation Validator.
 */
public interface EntityValidator {

    /**
     * Construct a provider for the EntityValidator abstraction.
     *
     * @param validation com.ibm.ws.beanvalidation.service.BeanValidation service for this provider to use.
     * @return the provider.
     */
    static EntityValidator newInstance(Object validation, Class<?> repositoryInterface) {
        try {
            @SuppressWarnings("unchecked")
            Class<EntityValidator> EntityValidatorImpl = (Class<EntityValidator>) EntityValidator.class.getClassLoader() //
                            .loadClass("io.openliberty.data.internal.persistence.validation.EntityValidatorImpl");
            return EntityValidatorImpl.getConstructor(Object.class, Class.class).newInstance(validation, repositoryInterface);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException x) {
            throw new RuntimeException(x); // internal error
        } catch (InvocationTargetException x) {
            throw new RuntimeException(x.getCause()); // internal error
        }
    }

    /**
     * Determines whether validation is needed for the method return value and parameters.
     *
     * @param method a repository method.
     * @return pair of booleans where the first is whether to validate the return value
     *         and the second is whether to validate parameters.
     */
    public boolean[] isValidatable(Method method);

    /**
     * Validates method parameters where the method or its class is annotated with ValidateOnExecution.
     *
     * @param object instance that has the method with parameters to validate.
     * @param method the method.
     * @param args   the method parameters.
     * @throws ConstraintValidationException if any of the constraints are violated.
     */
    public <T> void validateParameters(T object, Method method, Object[] args);

    /**
     * Validates the return value where the method or its class is annotated with ValidateOnExecution.
     *
     * @param object      instance that has the method with the return value to validate.
     * @param method      the method.
     * @param returnValue the return value of the method.
     * @throws ConstraintValidationException if any of the constraints are violated.
     */
    public <T> void validateReturnValue(T object, Method method, Object returnValue);
}