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
package pink.catty.config.api;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.catty.core.Constants;
import pink.catty.core.config.RegistryConfig;
import pink.catty.core.extension.ExtensionFactory;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.spi.EndpointFactory;
import pink.catty.core.extension.spi.Protocol;
import pink.catty.core.invoker.Provider;
import pink.catty.core.invoker.endpoint.Server;
import pink.catty.core.model.ServiceModel;
import pink.catty.core.service.HeartBeatService;
import pink.catty.core.service.HeartBeatServiceImpl;
import pink.catty.core.support.Node;

/**
 * Exporter is the entrance of RPC-server.
 *
 * Exporter instance and service instances are 1 on n.
 */
public class Exporter {

  private static final Logger logger = LoggerFactory.getLogger(Exporter.class);

  private Map<String, Provider> serviceHandlers = new HashMap<>();

  private Server server;

  private ServerConfig serverConfig;

  private RegistryConfig registryConfig;

  private ProtocolConfig protocolConfig;

  public Exporter() {
  }

  public Exporter(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  public void setServerConfig(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  public void setRegistryConfig(RegistryConfig registryConfig) {
    this.registryConfig = registryConfig;
  }

  public void setProtocolConfig(ProtocolConfig protocolConfig) {
    this.protocolConfig = protocolConfig;
  }

  public <T> Exporter registerService(Class<T> interfaceClass, T serviceObject) {
    Node address = serverConfig.getServerAddress();

    ServiceModel serviceModel = ServiceModel.Parse(interfaceClass);
    serviceModel.setTarget(serviceObject);


    Protocol chainBuilder = ExtensionFactory.protocol()
        .getExtension(ProtocolType.CATTY);
    return this;
  }

  public void export() {
    Node address = serverConfig.getServerAddress();

    EndpointFactory factory = ExtensionFactory.endpointFactory()
        .getExtension(protocolConfig.getEndpointType());
    if (server == null) {
      throw new NullPointerException("Server is not exist");
    }

    /*
     * If first open server, register heartbeat service.
     */
    if (!serviceHandlers.containsKey(Constants.HEARTBEAT_SERVICE_NAME)) {
      registerService(HeartBeatService.class, new HeartBeatServiceImpl());
    }

    serviceHandlers.forEach((s, invokerHolder) -> server.registerInvoker(s, invokerHolder));
  }

  public void unexport() {
    serviceHandlers.forEach((s, invokerHolder) -> server.unregisterInvoker(s));
    server.close();
    logger.info("Unexport, port: {}", serverConfig.getPort());
  }

}
