/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
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
package com.ibm.ws.jaxrs20.cdi12.fat.basic;

import javax.enterprise.context.Dependent;
import javax.ws.rs.Path;

@Dependent
@Path("/helloworldc")
public class HelloWorldResourceForC extends HelloWorldResource {

    private final String type = "PerRequest";

    public HelloWorldResourceForC() {
        this.setType(type);
    }
}