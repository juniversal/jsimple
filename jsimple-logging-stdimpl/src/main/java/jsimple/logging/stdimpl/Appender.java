/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Logback http://logback.qos.ch
 * (collectively, "Third Party Code"). Microsoft Mobile is not the original
 * author of the Third Party Code.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 */

package jsimple.logging.stdimpl;

/**
 * Note that Appenders MUST BE THREAD SAFE!
 *
 * @author Bret Johnson
 * @since 4/8/13 12:39 AM
 */
public abstract class Appender {
    abstract public void append(LoggingEvent loggingEvent);
}
