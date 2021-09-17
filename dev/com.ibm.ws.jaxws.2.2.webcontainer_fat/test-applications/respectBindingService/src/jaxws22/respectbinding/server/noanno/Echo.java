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
//A test service for the RespectBinding feature

//Package does not match the eclipse directory. Needs to be that way
//so webservice.xml and web.xml can be easily recycled across
//multiple services.

package jaxws22.respectbinding.server.noanno; // don't change this package

import javax.jws.WebService;

import jaxws22.respectbinding.server.Exception_Exception;

@WebService(targetNamespace = "http://server.respectbinding.jaxws22/", wsdlLocation = "WEB-INF/wsdl/EchoService.wsdl")
public class Echo {
    public String echo(String in) throws Exception_Exception {
        System.out.println("EcshoPort's RespectBinding isn't set and called with arg:" + in);
        return in;
    }
}