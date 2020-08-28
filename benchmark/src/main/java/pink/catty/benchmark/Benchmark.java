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
package pink.catty.benchmark;

import pink.catty.benchmark.common.PojoWrkGateway;
import pink.catty.benchmark.service.PojoService;
import pink.catty.benchmark.service.PojoServiceImpl;
import pink.catty.config.api.ClientConfig;
import pink.catty.config.api.Exporter;
import pink.catty.config.api.ProtocolConfig;
import pink.catty.config.api.Reference;
import pink.catty.config.api.ServerConfig;

public class Benchmark {

  public static void main(String[] args) {
    ServerConfig serverConfig = ServerConfig.builder()
        .minWorkerThreadNum(200)
        .maxWorkerThreadNum(400)
        .port(25500)
        .build();

    ProtocolConfig protocolConfig = ProtocolConfig.defaultConfig();

    Exporter exporter = new Exporter(serverConfig);
    exporter.setProtocolConfig(protocolConfig);
    exporter.registerService(PojoService.class, new PojoServiceImpl());
    exporter.export();

    ClientConfig clientConfig = ClientConfig.builder()
        .addAddress("127.0.0.1:25500")
        .build();

    Reference<PojoService> reference = new Reference<>();
    reference.setClientConfig(clientConfig);
    reference.setProtocolConfig(protocolConfig);
    reference.setInterfaceClass(PojoService.class);
    PojoWrkGateway gateway = new PojoWrkGateway();
    gateway.start(reference.refer());
  }

}
