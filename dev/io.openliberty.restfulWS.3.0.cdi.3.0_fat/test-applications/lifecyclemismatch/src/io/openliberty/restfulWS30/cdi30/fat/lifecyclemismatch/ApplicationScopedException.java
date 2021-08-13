/*******************************************************************************
 * Copyright (c) 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package io.openliberty.restfulWS30.cdi30.fat.lifecyclemismatch;

public class ApplicationScopedException extends Exception {

    private static final long serialVersionUID = -1975560538784455458L;

    public ApplicationScopedException() {
        super();
    }

    public ApplicationScopedException(String message) {
        super(message);
    }

    public ApplicationScopedException(Throwable cause) {
        super(cause);
    }

    public ApplicationScopedException(String message, Throwable cause) {
        super(message, cause);
    }
}