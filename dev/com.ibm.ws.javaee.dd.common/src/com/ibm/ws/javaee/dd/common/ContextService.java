/*******************************************************************************
 * Copyright (c) 2021,2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.javaee.dd.common;

import java.util.List;

/**
 * Represents &lt;context-service&gt;.
 */
public interface ContextService extends JNDIEnvironmentRef {
    /**
     * @return &lt;description&gt; or null if not set.
     */
    Description getDescription();

    /**
     * @return &lt;cleared&gt; elements as a read-only list
     */
    List<String> getCleared();

    /**
     * @return &lt;propagated&gt; elements as a read-only list
     */
    List<String> getPropagated();

    /**
     * @return &lt;unchanged&gt; elements as a read-only list
     */
    List<String> getUnchanged();

    /**
     * @return &lt;property&gt; elements as a read-only list
     */
    List<Property> getProperties();
}
