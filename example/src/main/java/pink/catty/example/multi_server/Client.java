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
package pink.catty.example.multi_server;

import java.util.concurrent.TimeUnit;
import pink.catty.config.api.ClientConfig;
import pink.catty.config.api.ProtocolConfig;
import pink.catty.config.api.Reference;
import pink.catty.core.extension.ExtensionType.ClusterType;

public class Client {

  public static void main(String[] args) {
    ClientConfig clientConfig = ClientConfig.builder()
        .addAddress("127.0.0.1:20550")
        .addAddress("127.0.0.1:20551")
//        .addAddress("127.0.0.1:20552")
        .build();

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setClusterType(ClusterType.FAIL_BACK);
    protocolConfig.setRecoveryPeriod(3000);
    protocolConfig.setRetryTimes(1);

    Reference<IService> reference = new Reference<>();
    reference.setClientConfig(clientConfig);
    reference.setProtocolConfig(protocolConfig);
    reference.setInterfaceClass(IService.class);

    IService service = reference.refer();

    for (int i = 0; i < 10000; i++) {
      sleep();
      System.out.println(service.say());
    }
  }

  private static void sleep() {
    try {
      TimeUnit.MILLISECONDS.sleep(500);
    } catch (Exception e) {
      // ignore
    }
  }

}
