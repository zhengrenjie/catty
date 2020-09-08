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

import pink.catty.core.config.definition.ConsumerDefinition;
import pink.catty.core.config.definition.Define;
import pink.catty.core.extension.ExtensionType.EndpointFactoryType;
import pink.catty.core.extension.ExtensionType.LoadBalanceType;
import pink.catty.core.extension.ExtensionType.ProtocolType;

/**
 * Read-Only.
 */
public final class ConsumerConfig {

  /**
   * Builder, the only entry to construct ConsumerConfig.
   *
   * @return ConsumerConfigBuilder instance
   */
  public static ConsumerConfigBuilder builder() {
    ConsumerConfigBuilder builder = new ConsumerConfigBuilder();
    ConfigBuilderHelper
        .PrepareBuilder(builder, ConsumerConfig.class, ConsumerDefinition.GetDefinition());
    return builder;
  }

  /* ****************
   *     Client
   * ***************/

  /**
   * Client connect timeout. ms
   */
  @Define(ConsumerDefinition.CONNECT_TIMEOUT)
  private final int connectTimeout;

  /**
   * Client read timeout. ms
   */
  @Define(ConsumerDefinition.READ_TIMEOUT)
  private final int readTimeout;

  /**
   * If registry is set, then use registry to get remote address first, or use direct address.
   */
  @Define(ConsumerDefinition.DIRECT_ADDRESSES)
  private final String directAddress;

  /**
   * The io thread number of client. If not use I/O multiplexing like netty(select, epoll, etc.),
   * this config will be ignored.
   */
  @Define(ConsumerDefinition.IO_THREAD_NUMBER)
  private final int ioThreadNum;

  /**
   * If use a worker thread pool to fire a request, otherwise use io thread.
   */
  @Define(ConsumerDefinition.USE_WORKER_THREAD)
  private final boolean useWorkerThread;

  /**
   * If USE_IO_THREAD == false, the client will create a worker thread pool to execute tasks.
   * WORKER_MIN_NUM indicated the min thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  @Define(ConsumerDefinition.WORKER_MIN_NUM)
  private final int workerMinNum;

  /**
   * If USE_IO_THREAD == false, the client will create a worker thread pool to execute tasks.
   * WORKER_MAX_NUM indicated the max thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  @Define(ConsumerDefinition.WORKER_MAX_NUM)
  private final int workerMaxNum;

  /**
   * @see pink.catty.core.extension.spi.EndpointFactory
   * @see EndpointFactoryType
   */
  @Define(ConsumerDefinition.CLIENT_TYPE)
  private final String clientType;

  /* ****************************
   *    Codec & Serialization
   * ***************************/

  /**
   * Which serialization to use.
   *
   * @see pink.catty.core.extension.spi.Serialization
   * @see pink.catty.core.extension.ExtensionType.SerializationType
   */
  @Define(ConsumerDefinition.SERIALIZATION)
  private final String serialization;

  /**
   * Which codec to use.
   *
   * @see pink.catty.core.extension.spi.Codec
   * @see pink.catty.core.extension.ExtensionType.CodecType
   */
  @Define(ConsumerDefinition.CODEC)
  private final String codec;

  /* ****************************
   *           Cluster
   * ***************************/

  /**
   * If periodically testing the server is alive.
   */
  @Define(ConsumerDefinition.USE_HEALTH_CHECK)
  private final boolean useHealthCheck;

  /**
   * Period of health check. If useHealthCheck == false, this config will be ignored.
   */
  @Define(ConsumerDefinition.HEALTH_CHECK_PERIOD)
  private final int healthCheckPeriod;

  /**
   * @see pink.catty.core.extension.spi.LoadBalance
   * @see LoadBalanceType
   */
  @Define(ConsumerDefinition.LOAD_BALANCE)
  private final String loadBalance;

  /**
   * @see pink.catty.core.extension.spi.Cluster
   * @see pink.catty.core.extension.ExtensionType.ClusterType
   */
  @Define(ConsumerDefinition.HA_STRATEGY)
  private final String cluster;

  /**
   * If cluster type is fail-over, this config indicates the times of retry. If cluster type is NOT
   * fail-over, this config will be ignored.
   */
  @Define(ConsumerDefinition.RETRY_TIMES)
  private final int retryTimes;

  /**
   * If cluster type is fail-back, this config indicates the period of reconnecting. If cluster type
   * is NOT fail-back, this config will be ignored.
   */
  @Define(ConsumerDefinition.FAIL_BACK_PERIOD)
  private final int failbackPeriod;

  /* ****************************
   *          protocol
   * ***************************/

  /**
   * @see pink.catty.core.extension.spi.Protocol
   * @see ProtocolType
   */
  @Define(ConsumerDefinition.PROTOCOL)
  private final String protocol;

  /**
   * Filter list.
   */
  @Define(ConsumerDefinition.FILTER_LIST)
  private final String filterList;

  /* ****************************
   * interface & method(not support yet)
   * ***************************/

  /**
   * The interface of service.
   */
  @Define(ConsumerDefinition.INTERFACE_CLASS)
  private final Class<?> interfaceClass;

  /**
   * Name of the interface, if not set, interface' class name will be used.
   */
  @Define(ConsumerDefinition.INTERFACE_NAME)
  private final String interfaceName;

  /**
   * Version of the interface.
   */
  @Define(ConsumerDefinition.INTERFACE_VERSION)
  private final String interfaceVersion;

  /**
   * The timeout of interface, every methods in this interface have the same timeout unless method
   * has specified its own timeout.
   */
  @Define(ConsumerDefinition.INTERFACE_TIMEOUT)
  private final String interfaceTimeout;

  /* ****************************
   *          registry
   * ***************************/

  /**
   * Registry address, if this config is set, the client.direct_address will be ignored.
   */
  @Define(ConsumerDefinition.REGISTRY_ADDRESS)
  private final String registryAddress;

  ConsumerConfig(int connectTimeout,
      int readTimeout,
      String directAddress,
      int ioThreadNum,
      boolean useWorkerThread,
      int workerMinNum,
      int workerMaxNum,
      String clientType,
      String serialization,
      String codec,
      boolean useHealthCheck,
      int healthCheckPeriod,
      String loadBalance,
      String cluster,
      int retryTimes,
      int failbackPeriod,
      String protocol,
      String filterList,
      Class<?> interfaceClass,
      String interfaceName,
      String interfaceVersion,
      String interfaceTimeout,
      String registryAddress) {
    this.connectTimeout = connectTimeout;
    this.readTimeout = readTimeout;
    this.directAddress = directAddress;
    this.ioThreadNum = ioThreadNum;
    this.useWorkerThread = useWorkerThread;
    this.workerMinNum = workerMinNum;
    this.workerMaxNum = workerMaxNum;
    this.clientType = clientType;
    this.serialization = serialization;
    this.codec = codec;
    this.useHealthCheck = useHealthCheck;
    this.healthCheckPeriod = healthCheckPeriod;
    this.loadBalance = loadBalance;
    this.cluster = cluster;
    this.retryTimes = retryTimes;
    this.failbackPeriod = failbackPeriod;
    this.protocol = protocol;
    this.filterList = filterList;
    this.interfaceClass = interfaceClass;
    this.interfaceName = interfaceName;
    this.interfaceVersion = interfaceVersion;
    this.interfaceTimeout = interfaceTimeout;
    this.registryAddress = registryAddress;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public String getDirectAddress() {
    return directAddress;
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

  public String getClientType() {
    return clientType;
  }

  public String getSerialization() {
    return serialization;
  }

  public String getCodec() {
    return codec;
  }

  public boolean isUseHealthCheck() {
    return useHealthCheck;
  }

  public int getHealthCheckPeriod() {
    return healthCheckPeriod;
  }

  public String getLoadBalance() {
    return loadBalance;
  }

  public String getCluster() {
    return cluster;
  }

  public int getRetryTimes() {
    return retryTimes;
  }

  public int getFailbackPeriod() {
    return failbackPeriod;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getFilterList() {
    return filterList;
  }

  public Class<?> getInterfaceClass() {
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
