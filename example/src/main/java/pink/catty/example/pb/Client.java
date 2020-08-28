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
package pink.catty.example.pb;

import pink.catty.config.api.ProtocolConfig;
import pink.catty.config.api.Reference;
import pink.catty.config.api.ClientConfig;
import pink.catty.core.extension.ExtensionType.SerializationType;
import pink.catty.example.pb.generated.EchoProtocol;
import pink.catty.example.pb.generated.EchoProtocol.Request;

public class Client {

  public static void main(String[] args) {
    ClientConfig clientConfig = ClientConfig.builder()
        .addAddress("127.0.0.1:20550")
        .build();

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setSerializationType(SerializationType.PROTOBUF);

    Reference<IService> reference = new Reference<>();
    reference.setClientConfig(clientConfig);
    reference.setInterfaceClass(IService.class);
    reference.setProtocolConfig(protocolConfig);

    Request request = EchoProtocol.Request.newBuilder()
        .setValue("123321")
        .build();

    EchoProtocol.Response response = reference.refer().echo(request);
    System.out.println(response.getValue());
  }
}
