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
import pink.catty.core.eventbus.EventBuses;

public class EventBusCapabilityTest {

  @Test
  public void testSync() {
    EventBuses.SYNC.register(new Listener1());
    Event1 evnet1 = new Event1();
    evnet1.setName("test");
    EventBuses.SYNC.post(evnet1);
    Assert.assertEquals(Listener1.AFTER_EVENT_POST, evnet1.getName());
  }

}
