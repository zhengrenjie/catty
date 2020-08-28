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
package pink.catty.core.eventbus;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventBus {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers =
      new ConcurrentHashMap<>();

  public abstract void post(Object event);

  public void register(Object listener) {
    Map<Class<?>, List<Subscriber>> listenerMethods = findAllSubscriber(listener);

    for (Entry<Class<?>, List<Subscriber>> entry : listenerMethods.entrySet()) {
      Class<?> eventType = entry.getKey();
      Collection<Subscriber> eventMethodsInListener = entry.getValue();
      subscribers
          .computeIfAbsent(eventType, (k) -> new CopyOnWriteArraySet<>(eventMethodsInListener))
          .addAll(eventMethodsInListener);
    }
  }

  public void unregister(Object listener) {
    Map<Class<?>, List<Subscriber>> listenerMethods = findAllSubscriber(listener);

    for (Entry<Class<?>, List<Subscriber>> entry : listenerMethods.entrySet()) {
      Class<?> eventType = entry.getKey();
      Collection<Subscriber> eventMethodsInListener = entry.getValue();
      subscribers.
          getOrDefault(eventType, new CopyOnWriteArraySet<>())
          .removeAll(eventMethodsInListener);
    }
  }

  private Map<Class<?>, List<Subscriber>> findAllSubscriber(Object target) {
    Map<Class<?>, List<Subscriber>> subscriberMap = new HashMap<>();

    for (Method method : target.getClass().getMethods()) {
      if (method.isAnnotationPresent(Subscribe.class) && !method.isSynthetic()) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
          throw new IllegalArgumentException(
              "Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.");
        }

        Class<?> eventType = parameterTypes[0];

        subscriberMap
            .computeIfAbsent(eventType, (k) -> new LinkedList<>())
            .add(new Subscriber(eventType, target, method));
      }
    }

    return subscriberMap;
  }

  static class Subscriber {

    private static final Logger logger = LoggerFactory.getLogger(Object.class);

    private final Class<?> eventType;
    private final Object target;
    private final Method method;

    public Subscriber(Class<?> eventType, Object target, Method method) {
      this.eventType = eventType;
      this.target = target;
      this.method = method;
    }

    public void invoke(Object event) {
      try {
        method.invoke(target, event);
      } catch (Throwable e) {
        logger.error("Error occurred when event " + eventType + " is posted, listener: " + method);
      }
    }
  }
}
