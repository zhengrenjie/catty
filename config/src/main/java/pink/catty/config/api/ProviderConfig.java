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

import pink.catty.core.config.definition.ProviderDefinition;
import pink.catty.core.extension.ExtensionType.ProtocolType;

/**
 * Read-Only.
 */
public final class ProviderConfig<T> {

  /**
   * Builder, the only entry to construct ProviderConfig.
   *
   * @return ProviderConfigBuilder instance
   */
  public static <T> ProviderConfigBuilder<T> builder(Class<T> service) {
    ProviderConfigBuilder<T> builder = new ProviderConfigBuilder<>();
    ConfigBuilderHelper
        .PrepareBuilder(builder, ProviderConfig.class, ProviderDefinition.GetDefinition());
    builder.setInterfaceClass(service);
    return builder;
  }

  /* ****************
   *     Server
   * ***************/

  /**
   * server connect timeout. ms
   */
  @Define(ProviderDefinition.LISTEN_PORT)
  private final int port;

  /**
   * The io thread number of server. If not use I/O multiplexing like netty(select, epoll, etc.),
   * this config will be ignored.
   */
  @Define(ProviderDefinition.IO_THREAD_NUMBER)
  private final int ioThreadNum;

  /**
   * If use a worker thread pool to fire a request, otherwise use io thread.
   */
  @Define(ProviderDefinition.USE_WORKER_THREAD)
  private final boolean useWorkerThread;

  /**
   * If USE_IO_THREAD == false, the server will create a worker thread pool to execute tasks.
   * WORKER_MIN_NUM indicated the min thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  @Define(ProviderDefinition.WORKER_MIN_NUM)
  private final int workerMinNum;

  /**
   * If USE_IO_THREAD == false, the server will create a worker thread pool to execute tasks.
   * WORKER_MAX_NUM indicated the max thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  @Define(ProviderDefinition.WORKER_MAX_NUM)
  private final int workerMaxNum;

  /**
   * @see pink.catty.core.extension.spi.EndpointFactory
   * @see pink.catty.core.extension.ExtensionType.EndpointFactoryType
   */
  @Define(ProviderDefinition.SERVER_TYPE)
  private final String serverType;

  /* ****************************
   *    Codec & Serialization
   * ***************************/

  /**
   * Which serialization to use.
   *
   * @see pink.catty.core.extension.spi.Serialization
   * @see pink.catty.core.extension.ExtensionType.SerializationType
   */
  @Define(ProviderDefinition.SERIALIZATION)
  private final String serialization;

  /**
   * Which codec to use.
   *
   * @see pink.catty.core.extension.spi.Codec
   * @see pink.catty.core.extension.ExtensionType.CodecType
   */
  @Define(ProviderDefinition.CODEC)
  private final String codec;

  /* ****************************
   *          protocol
   * ***************************/

  /**
   * @see pink.catty.core.extension.spi.Protocol
   * @see ProtocolType
   */
  @Define(ProviderDefinition.PROTOCOL)
  private final String protocol;

  /**
   * Filter list.
   */
  @Define(ProviderDefinition.FILTER_LIST)
  private final String filterList;

  /* ****************************
   * interface & method(not support yet)
   * ***************************/

  /**
   * The interface of service.
   */
  @Define(ProviderDefinition.INTERFACE_CLASS)
  private final Class<T> interfaceClass;

  /**
   * Name of the interface, if not set, interface' class name will be used.
   */
  @Define(ProviderDefinition.INTERFACE_NAME)
  private final String interfaceName;

  /**
   * Version of the interface.
   */
  @Define(ProviderDefinition.INTERFACE_VERSION)
  private final String interfaceVersion;

  /**
   * The timeout of interface, every methods in this interface have the same timeout unless method
   * has specified its own timeout.
   */
  @Define(ProviderDefinition.INTERFACE_TIMEOUT)
  private final String interfaceTimeout;

  /* ****************************
   *          registry
   * ***************************/

  /**
   * Registry address, if this config is set, the server.direct_address will be ignored.
   */
  @Define(ProviderDefinition.REGISTRY_ADDRESS)
  private final String registryAddress;

  ProviderConfig(int port,
      int ioThreadNum,
      boolean useWorkerThread,
      int workerMinNum,
      int workerMaxNum,
      String serverType,
      String serialization,
      String codec,
      String protocol,
      String filterList,
      Class<T> interfaceClass,
      String interfaceName,
      String interfaceVersion,
      String interfaceTimeout,
      String registryAddress) {
    this.port = port;
    this.ioThreadNum = ioThreadNum;
    this.useWorkerThread = useWorkerThread;
    this.workerMinNum = workerMinNum;
    this.workerMaxNum = workerMaxNum;
    this.serverType = serverType;
    this.serialization = serialization;
    this.codec = codec;
    this.protocol = protocol;
    this.filterList = filterList;
    this.interfaceClass = interfaceClass;
    this.interfaceName = interfaceName;
    this.interfaceVersion = interfaceVersion;
    this.interfaceTimeout = interfaceTimeout;
    this.registryAddress = registryAddress;
  }

  public int getPort() {
    return port;
  }

  public int getIoThreadNum() {
    return ioThreadNum;
  }

  public boolean isUseWorkerThread() {
    return useWorkerThread;
  }

  public int getWorkerMinNum() {
    return workerMinNum;
  }

  public int getWorkerMaxNum() {
    return workerMaxNum;
  }

  public String getServerType() {
    return serverType;
  }

  public String getSerialization() {
    return serialization;
  }

  public String getCodec() {
    return codec;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getFilterList() {
    return filterList;
  }

  public Class<T> getInterfaceClass() {
    return interfaceClass;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public String getInterfaceVersion() {
    return interfaceVersion;
  }

  public String getInterfaceTimeout() {
    return interfaceTimeout;
  }

  public String getRegistryAddress() {
    return registryAddress;
  }
}
