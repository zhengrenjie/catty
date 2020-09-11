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
package pink.catty.core.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.catty.core.AbstractAttribute;

public class Config extends AbstractAttribute {

  protected static Logger logger = LoggerFactory.getLogger(Config.class);

  private static final String CUSTOM_CONFIG_MAP = "customConfig";

  private final Map<String, String> customConfig = new HashMap<>();

  public void addCustomConfig(String key, String value) {
    customConfig.put(key, String.valueOf(value));
  }

  public boolean containsCustomConfig(String key) {
    return customConfig.containsKey(key);
  }

  public String removeCustomConfig(String key) {
    return customConfig.remove(key);
  }

  public Map<String, String> getCustomConfig() {
    return customConfig;
  }

  public byte getByte(String key) {
    return getByteDef(key, (byte) 0);
  }

  public short getShort(String key) {
    return getShortDef(key, (short) 0);
  }

  public int getInt(String key) {
    return getIntDef(key, 0);
  }

  public long getLong(String key) {
    return getLongDef(key, 0L);
  }

  public String getString(String key) {
    return getStringDef(key, "");
  }

  public boolean getBool(String key) {
    return getBoolDef(key, false);
  }

  public double getDouble(String key) {
    return getDoubleDef(key, 0.0);
  }

  public byte getByteDef(String key, byte def) {
    if (customConfig.containsKey(key)) {
      return Byte.parseByte(customConfig.get(key));
    }
    return def;
  }

  public short getShortDef(String key, short def) {
    if (customConfig.containsKey(key)) {
      return Short.parseShort(customConfig.get(key));
    }
    return def;
  }

  public int getIntDef(String key, int def) {
    if (customConfig.containsKey(key)) {
      return Integer.parseInt(customConfig.get(key));
    }
    return def;
  }

  public long getLongDef(String key, long def) {
    if (customConfig.containsKey(key)) {
      return Long.parseLong(customConfig.get(key));
    }
    return def;
  }

  public String getStringDef(String key, String def) {
    if (customConfig.containsKey(key)) {
      return customConfig.get(key);
    }
    return def;
  }

  public boolean getBoolDef(String key, boolean def) {
    if (customConfig.containsKey(key)) {
      return Boolean.parseBoolean(customConfig.get(key));
    }
    return def;
  }

  public double getDoubleDef(String key, double def) {
    if (customConfig.containsKey(key)) {
      return Double.parseDouble(customConfig.get(key));
    }
    return def;
  }

  @Override
  public String toString() {
    Class<?> clazz = getClass();

    /*
     * get all parent's fields.
     */
    StringBuilder sb = new StringBuilder();
    List<Field> fieldList = new LinkedList<>();
    Class<?> parent = clazz;
    while (parent != null && parent != Object.class) {
      Field[] fields = parent.getDeclaredFields();
      fieldList.addAll(Arrays.asList(fields));
      parent = parent.getSuperclass();
    }

    /*
     * ignore static, transient, synthetic(this).
     */
    for (Field field : fieldList) {
      if (Modifier.isStatic(field.getModifiers())
          || Modifier.isTransient(field.getModifiers())
          || field.isSynthetic()) {
        continue;
      }
      if (field.getName().equals(CUSTOM_CONFIG_MAP)) {
        continue;
      }
      try {
        sb.append(field.getName()).append("=");
        if (field.get(this) != null) {
          sb.append(field.get(this));
        }
        sb.append(";");
      } catch (IllegalAccessException e) {
        logger.error("MetaInfo toString access control error.", e);
      }
    }

    /*
     * append custom config.
     */
    for (Entry<String, String> entry : getCustomConfig().entrySet()) {
      sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
    }

    if (sb.length() <= 0) {
      return "";
    } else {
      return sb.toString();
    }
  }
}
