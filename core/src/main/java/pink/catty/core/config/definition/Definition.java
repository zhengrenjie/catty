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

import java.util.HashMap;
import java.util.Map;

public class Definition {

  private final Map<String, ConfigDefine> defineMap;

  public Definition() {
    this.defineMap = new HashMap<>();
  }

  public ConfigDefine getConfigDefine(String key) {
    return defineMap.get(key);
  }

  public void addConfigDefine(String key, ConfigDefine define) {
    defineMap.put(key, define);
  }

  public boolean containsName(String name) {
    return defineMap.containsKey(name);
  }

  public void removeDefine(String key) {
    defineMap.remove(key);
  }
}
