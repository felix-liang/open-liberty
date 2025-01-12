/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
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

package fats.cxf.basic.wssec;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2012-10-10T10:52:58.284-05:00
 * Generated source version: 2.6.2
 *
 */
@WebService(targetNamespace = "http://wssec.basic.cxf.fats", name = "UsrToken")
@XmlSeeAlso({ fats.cxf.basic.wssec.types.ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface UsrToken {

    @WebResult(name = "getVerResponse", targetNamespace = "http://wssec.basic.cxf.fats/types", partName = "out")
    @WebMethod
    public fats.cxf.basic.wssec.types.GetVerResponse invoke(
                                                            @WebParam(partName = "in", name = "getVer",
                                                                      targetNamespace = "http://wssec.basic.cxf.fats/types") fats.cxf.basic.wssec.types.GetVer in);
}
