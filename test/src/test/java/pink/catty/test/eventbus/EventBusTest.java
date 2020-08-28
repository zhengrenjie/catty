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
package pink.catty.test.eventbus;

import org.junit.Assert;
import org.junit.Test;
import pink.catty.core.eventbus.EventBus;

public class EventBusTest extends EventBus {

  @Test
  public void Test1() {
    register(new Listener1());
    Assert.assertEquals(1, subscribers.size());
    Assert.assertTrue(subscribers.containsKey(Event1.class));
    Assert.assertEquals(1, subscribers.get(Event1.class).size());
  }

  @Test
  public void Test2() {
    Listener1 listener1 = new Listener1();
    register(listener1);
    Assert.assertEquals(1, subscribers.size());
    Assert.assertTrue(subscribers.containsKey(Event1.class));
    Assert.assertEquals(1, subscribers.get(Event1.class).size());

    unregister(listener1);
    Assert.assertEquals(0, subscribers.size());
  }

  @Override
  public void post(Object event) {

  }
}
