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
package pink.catty.core.config.definition;

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

  private static final Object NO_DEFAULT_VALUE = new Object();

  private final Definition definition;
  private String name;
  private String description;
  private Type type;
  private Object defaultValue = NO_DEFAULT_VALUE;
  private Validator[] validators;

  private ConfigDefine(Definition definition) {
    this.definition = definition;
  }

  public static ConfigDefine define(Definition definition) {
    return new ConfigDefine(definition);
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

  public ConfigDefine withValidator(Validator... validators) {
    this.validators = validators;
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
    if (defaultValue != NO_DEFAULT_VALUE && defaultValue != null) {
      checkValueType(defaultValue);
    }

    if (definition.containsName(name)) {
      throw new NullPointerException("Config named '" + name + "' is already defined.");
    }
    definition.addConfigDefine(name, this);
  }

  public Definition getDefinition() {
    return definition;
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

    if (validators != null) {
      for(Validator validator : validators) {
        validator.check(this, value);
      }
    }
  }

  public void destroy() {
    definition.removeDefine(name);
  }

  public boolean hasDefaultValue() {
    return defaultValue != NO_DEFAULT_VALUE;
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
