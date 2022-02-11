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

/**
 * Represents &lt;managed-thread-factory&gt;.
 */
public interface ManagedThreadFactory extends JNDIContextServiceRef {
    /**
     * @return &lt;priority&gt; if specified
     * @see #isSetPriority
     */
    int getPriority();

    /**
     * @return true if &lt;priority&gt; is specified
     * @see #getPriority
     */
    boolean isSetPriority();
}
