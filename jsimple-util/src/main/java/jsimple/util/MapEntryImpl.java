/*
 * Copyright (c) 2012-2014 Microsoft Mobile.  All Rights Reserved.
 *
 * This file is based on or incorporates material from Apache Harmony
 * http://harmony.apache.org (collectively, “Third Party Code”). Microsoft Mobile
 * is not the original author of the Third Party Code. The original copyright
 * notice and the license, under which Microsoft Mobile received such Third Party
 * Code, are set forth below.
 *
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
 * MapEntry is an internal class which provides an implementation of Map.Entry.
 */
class MapEntryImpl<K, V> implements MapEntry<K, V>, Cloneable {
    K key;
    V value;

    /*
    interface Type<RT, KT, VT> {
        RT get(MapEntry<KT, VT> entry);
    }
    */

    MapEntryImpl(K theKey) {
        key = theKey;
    }

    MapEntryImpl(K theKey, V theValue) {
        key = theKey;
        value = theValue;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override public boolean equals(Object object) {
        throw new ProgrammerError("equals method not supported for HashMap MapEntry");
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return (key == null ? 0 : key.hashCode())
                ^ (value == null ? 0 : value.hashCode());
    }

    public V setValue(V object) {
        V result = value;
        value = object;
        return result;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
