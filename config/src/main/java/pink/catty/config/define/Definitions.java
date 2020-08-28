package pink.catty.config.define;

import java.util.HashMap;
import java.util.Map;

public final class Definitions {

  private final Map<String, ConfigDefine> defineMap;

  public Definitions() {
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
