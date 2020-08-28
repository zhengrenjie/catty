/*
 * Copyright 2019 The Catty Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pink.catty.config.define;

import java.util.Objects;

/**
 * ConfigDefine is a class to define how one config looks like. For example, an option has an unique
 * name, the description about it, the data type of it, the validator on it, and a default value.
 *
 * Usage:
 * <code>
 *   ConfigDefine.define(definition)
 *       .withName("config_A")
 *       .withDescription("this is an example")
 *       .withType(Type.INT)
 *       .withDefaultValue(0)
 *       .withValidator((name, value) -> return;)
 *       .done();
 * </code>
 *
 * After config defined, you can invoke getConfigDefine to get an ConfigDefine.
 */
public final class ConfigDefine {

  private final Definitions definitions;
  private String name;
  private String description;
  private Type type;
  private Object defaultValue;
  private Validator validator;

  private ConfigDefine(Definitions definitions) {
    this.definitions = definitions;
  }

  public static ConfigDefine define(Definitions definitions) {
    return new ConfigDefine(definitions);
  }

  public ConfigDefine withName(String name) {
    this.name = name;
    return this;
  }

  public ConfigDefine withDescription(String desc) {
    this.description = desc;
    return this;
  }

  public ConfigDefine withType(Type type) {
    this.type = type;
    return this;
  }

  public ConfigDefine withDefaultValue(Object def) {
    this.defaultValue = def;
    return this;
  }

  public ConfigDefine withValidator(Validator validator) {
    this.validator = validator;
    return this;
  }

  public void done() {
    if (name == null) {
      throw new NullPointerException("Name should not be null.");
    }
    if (type == null) {
      throw new NullPointerException("Type should not be null.");
    }

    /*
     * check if type of default value is correct
     */
    if (defaultValue != null) {
      checkValueType(defaultValue);
    }

    if (definitions.containsName(name)) {
      throw new NullPointerException("Config named '" + name + "' is already defined.");
    }
    definitions.addConfigDefine(name, this);
  }

  public Definitions getDefinitions() {
    return definitions;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Type getType() {
    return type;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public void valid(Object value) {
    if (value == null) {
      throw new NullPointerException("Value is null");
    }
    checkValueType(value);

    if (validator != null) {
      validator.check(this, value);
    }
  }

  public void destroy() {
    definitions.removeDefine(name);
  }

  private void checkValueType(Object value) {
    switch (type) {
      case BOOLEAN:
        if (value instanceof Boolean) {
          return;
        }
        break;
      case INT:
        if (value instanceof Integer || value instanceof Long) {
          return;
        }
        break;
      case FLOAT:
        if (value instanceof Float || value instanceof Double) {
          return;
        }
        break;
      case STRING:
        if (value instanceof String) {
          return;
        }
        break;
      case OBJECT:
        break;
      default:
        throw new IllegalArgumentException("Type not support: " + type);
    }

    throw new IllegalArgumentException(
        "Type of value is illegal, required: " + type + " actual: " + value
            .getClass());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigDefine that = (ConfigDefine) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  /**********************
   *    static class
   **********************/

  public enum Type {
    BOOLEAN,
    INT,
    FLOAT,
    STRING,
    OBJECT,

    ;
  }

  public interface Validator {

    /**
     * Check if property value valid.
     *
     * @param definition property definition
     * @param value property value
     * @throws ValidException may be other exceptions
     */
    void check(ConfigDefine definition, Object value);
  }

}
