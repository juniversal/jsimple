/*
 * Copyright (c) 2012-2015, Microsoft Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package jsimple.json.readerwriter;

import jsimple.json.JsonException;
import jsimple.util.HashMap;
import org.jetbrains.annotations.Nullable;

/**
 * @author Bret Johnson
 * @since 5/19/13 3:10 PM
 */
public class JsonObjectType {
    private @Nullable JsonObjectType superclass;
    private HashMap<String, JsonProperty> properties = new HashMap<String, JsonProperty>();

    public JsonObjectType(@Nullable JsonObjectType superclass) {
        this.superclass = superclass;
    }

    public JsonObjectType() {
        this(null);
    }

    public @Nullable JsonProperty getProperty(String name) {
        @Nullable JsonProperty value = properties.get(name);
        if (value != null)
            return value;
        if (superclass != null)
            return superclass.getProperty(name);
        return null;
    }

    public JsonProperty createProperty(String name, int id) {
        JsonProperty property = properties.get(name);
        if (property != null)
            throw new JsonException("Property for '{}' has already been added to atoms list", name);

        property = new JsonProperty(name, id);
        properties.put(name, property);
        return property;
    }

    private <T extends JsonProperty> T addNewProperty(T property) {
        String name = property.getName();
        if (properties.get(name) != null)
            throw new JsonException("Property for '{}' has already been added to atoms list", name);

        properties.put(name, property);
        return property;
    }

    public JsonStringProperty createStringProperty(String name, int id) {
        return addNewProperty(new JsonStringProperty(name, id));
    }

    public JsonIntProperty createIntProperty(String name, int id) {
        return addNewProperty(new JsonIntProperty(name, id));
    }

    public JsonLongProperty createLongProperty(String name, int id) {
        return addNewProperty(new JsonLongProperty(name, id));
    }

    public JsonDoubleProperty createDoubleProperty(String name, int id) {
        return addNewProperty(new JsonDoubleProperty(name, id));
    }

    public JsonBooleanProperty createBooleanProperty(String name, int id) {
        return addNewProperty(new JsonBooleanProperty(name, id));
    }

    public JsonObjectProperty createObjectProperty(String name, int id) {
        return addNewProperty(new JsonObjectProperty(name, id));
    }

    public JsonArrayProperty createArrayProperty(String name, int id) {
        return addNewProperty(new JsonArrayProperty(name, id));
    }
}
