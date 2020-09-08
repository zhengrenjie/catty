/*
 * Copyright 2020 The Catty Project
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
package pink.catty.test;

import org.junit.Test;
import pink.catty.core.model.ServiceModel;

public class ServiceModelTest {

  @Test
  public void testValidService() {
    ServiceModel.Parse(MockService0.class);
  }

  public interface MockService0 {

    String echo(String str);

    /*
     * some method declared in Object class.
     */

    int hashCode();

    boolean equals(Object obj);

    String toString();

  }

  public interface MockService1 {

    String echo(String str);

    /*
     * some method declared in Object class.
     */

    int hashCode();

    boolean equals(Object obj);

    String toString();

    Object clone();

    void finalize();
  }
}
