/*
 * Portions copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from SLF4J http://www.slf4j.org
 * (collectively, “Third Party Code”). Microsoft Mobile is not the original
 * author of the Third Party Code.  The original copyright notice and the
 * license, under which Microsoft Mobile received such Third Party Code, are set
 * forth below.
 *
 *
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package jsimple.logging;

/**
 * Logging level.  Conceptually this is an enum, but creating it as a class works better across all languages.
 *
 * @author Bret Johnson
 * @since 4/7/13 11:42 PM
 */
public class Level {
    public static final Level OFF = new Level(0, "OFF");
    public static final Level TRACE = new Level(1, "TRACE");
    public static final Level DEBUG = new Level(2, "DEBUG");
    public static final Level INFO = new Level(3, "INFO");
    public static final Level WARN = new Level(4, "WARN");
    public static final Level ERROR = new Level(5, "ERROR");
    private final int value;
    private final String defaultDisplayName;

    private Level(int value, String defaultDisplayName) {
        this.value = value;
        this.defaultDisplayName = defaultDisplayName;
    }

    public int getIntValue() {
        return value;
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }
}
