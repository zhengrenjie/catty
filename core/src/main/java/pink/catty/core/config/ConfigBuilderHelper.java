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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import pink.catty.core.config.definition.Define;
import pink.catty.core.config.definition.Definition;

public final class ConfigBuilderHelper {

  static void PrepareBuilder(Object builder, Definition definition) {
    Class<?> builderClz = builder.getClass();

    /*
     * prepare setting-methods of builder.
     */
    Map<String, Method> methodMap = new HashMap<>();
    for (Method method : builderClz.getMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("set")) {
        methodMap.put(methodName, method);
      }
    }

    /*
     * invoke setting-methods to set default value
     */
    for (Field field : builderClz.getFields()) {
      if (!field.isAnnotationPresent(Define.class)) {
        continue;
      }

      /*
       * get setting-method
       */
      String methodName =
          "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
      if (!methodMap.containsKey(methodName)) {
        throw new ConfigException("Set-Method not in Builder: " + methodName);
      }
      Method setMethod = methodMap.get(methodName);

      /*
       * get default value;
       */
      Define define = field.getAnnotation(Define.class);
      String configDefinitionName = define.value();
      if (!definition.getConfigDefine(configDefinitionName).hasDefaultValue()) {
        continue;
      }
      Object defaultValue = definition
          .getConfigDefine(configDefinitionName)
          .getDefaultValue();

      /*
       * invoke setting-method of builder
       */
      try {
        setMethod.invoke(builder, defaultValue);
      } catch (Exception e) {
        throw new ConfigException(
            "Error occurred when invoking set-method of builder: " + setMethod, e);
      }
    }
  }
}
