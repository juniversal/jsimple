/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, "Third Party Code"). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
 *
 * Copyright 2006, 2010 The Apache Software Foundation.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jsimple.util;

/**
 * {@code Map.Entry} is a key/value mapping contained in a {@code Map}.
 */
public interface MapEntry<K,V> {
    /**
     * Compares the specified object to this {@code Map.Entry} and returns if they
     * are equal. To be equal, the object must be an instance of {@code Map.Entry} and have the
     * same key and value.
     *
     * @param object
     *            the {@code Object} to compare with this {@code Object}.
     * @return {@code true} if the specified {@code Object} is equal to this
     *         {@code Map.Entry}, {@code false} otherwise.
     * @see #hashCode()
     */
    public boolean equals(Object object);

    /**
     * Returns the key.
     *
     * @return the key
     */
    public K getKey();

    /**
     * Returns the value.
     *
     * @return the value
     */
    public V getValue();

    /**
     * Returns an integer hash code for the receiver. {@code Object} which are
     * equal return the same value for this method.
     *
     * @return the receiver's hash code.
     * @see #equals(Object)
     */
    public int hashCode();

    /**
     * Sets the value of this entry to the specified value, replacing any
     * existing value.
     *
     * @param object
     *            the new value to set.
     * @return object the replaced value of this entry.
     */
    public V setValue(V object);
}
