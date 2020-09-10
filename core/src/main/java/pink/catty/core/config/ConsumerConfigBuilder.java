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
package pink.catty.core.config;

import java.util.LinkedList;
import java.util.List;
import pink.catty.core.Node;
import pink.catty.core.config.definition.ConsumerDefinition;
import pink.catty.core.config.definition.Define;

public final class ConsumerConfigBuilder<T> {

  @Define(ConsumerDefinition.CONNECT_TIMEOUT)
  private int connectTimeout;

  @Define(ConsumerDefinition.READ_TIMEOUT)
  private int readTimeout;

  @Define(ConsumerDefinition.DIRECT_ADDRESSES)
  private List<Node> directAddress;

  @Define(ConsumerDefinition.IO_THREAD_NUMBER)
  private int ioThreadNum;

  @Define(ConsumerDefinition.USE_WORKER_THREAD)
  private boolean useWorkerThread;

  @Define(ConsumerDefinition.WORKER_MIN_NUM)
  private int workerMinNum;

  @Define(ConsumerDefinition.WORKER_MAX_NUM)
  private int workerMaxNum;

  @Define(ConsumerDefinition.CLIENT_TYPE)
  private String clientType;

  @Define(ConsumerDefinition.SERIALIZATION)
  private String serialization;

  @Define(ConsumerDefinition.CODEC)
  private String codec;

  @Define(ConsumerDefinition.USE_HEALTH_CHECK)
  private boolean useHealthCheck;

  @Define(ConsumerDefinition.HEALTH_CHECK_PERIOD)
  private int healthCheckPeriod;

  @Define(ConsumerDefinition.LOAD_BALANCE)
  private String loadBalance;

  @Define(ConsumerDefinition.HA_STRATEGY)
  private String cluster;

  @Define(ConsumerDefinition.RETRY_TIMES)
  private int retryTimes;

  @Define(ConsumerDefinition.FAIL_BACK_PERIOD)
  private int failbackPeriod;

  @Define(ConsumerDefinition.PROTOCOL)
  private String protocol;

  @Define(ConsumerDefinition.FILTER_LIST)
  private List<String> filterList;

  @Define(ConsumerDefinition.INTERFACE_CLASS)
  private Class<T> interfaceClass;

  @Define(ConsumerDefinition.INTERFACE_NAME)
  private String interfaceName;

  @Define(ConsumerDefinition.INTERFACE_VERSION)
  private String interfaceVersion;

  @Define(ConsumerDefinition.INTERFACE_TIMEOUT)
  private String interfaceTimeout;

  @Define(ConsumerDefinition.REGISTRY_ADDRESS)
  private String registryAddress;

  ConsumerConfigBuilder() {
    ConfigBuilderHelper
        .PrepareBuilder(this, ConsumerDefinition.GetDefinition());
  }

  public ConsumerConfigBuilder<T> setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public ConsumerConfigBuilder<T> setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public ConsumerConfigBuilder<T> addDirectAddress(Node directAddress) {
    if (this.directAddress == null) {
      this.directAddress = new LinkedList<>();
    }
    this.directAddress.add(directAddress);
    return this;
  }

  public ConsumerConfigBuilder<T> setDirectAddress(List<Node> directAddress) {
    this.directAddress = directAddress;
    return this;
  }

  public ConsumerConfigBuilder<T> setIoThreadNum(int ioThreadNum) {
    this.ioThreadNum = ioThreadNum;
    return this;
  }

  public ConsumerConfigBuilder<T> setUseWorkerThread(boolean useWorkerThread) {
    this.useWorkerThread = useWorkerThread;
    return this;
  }

  public ConsumerConfigBuilder<T> setWorkerMinNum(int workerMinNum) {
    this.workerMinNum = workerMinNum;
    return this;
  }

  public ConsumerConfigBuilder<T> setWorkerMaxNum(int workerMaxNum) {
    this.workerMaxNum = workerMaxNum;
    return this;
  }

  public ConsumerConfigBuilder<T> setClientType(String clientType) {
    this.clientType = clientType;
    return this;
  }

  public ConsumerConfigBuilder<T> setSerialization(String serialization) {
    this.serialization = serialization;
    return this;
  }

  public ConsumerConfigBuilder<T> setCodec(String codec) {
    this.codec = codec;
    return this;
  }

  public ConsumerConfigBuilder<T> setUseHealthCheck(boolean useHealthCheck) {
    this.useHealthCheck = useHealthCheck;
    return this;
  }

  public ConsumerConfigBuilder<T> setHealthCheckPeriod(int healthCheckPeriod) {
    this.healthCheckPeriod = healthCheckPeriod;
    return this;
  }

  public ConsumerConfigBuilder<T> setLoadBalance(String loadBalance) {
    this.loadBalance = loadBalance;
    return this;
  }

  public ConsumerConfigBuilder<T> setCluster(String cluster) {
    this.cluster = cluster;
    return this;
  }

  public ConsumerConfigBuilder<T> setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
    return this;
  }

  public ConsumerConfigBuilder<T> setFailbackPeriod(int failbackPeriod) {
    this.failbackPeriod = failbackPeriod;
    return this;
  }

  public ConsumerConfigBuilder<T> setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public ConsumerConfigBuilder<T> addFilterList(String filter) {
    if (this.filterList == null) {
      this.filterList = new LinkedList<>();
    }
    this.filterList.add(filter);
    return this;
  }

  public ConsumerConfigBuilder<T> setFilterList(List<String> filterList) {
    this.filterList = filterList;
    return this;
  }

  public ConsumerConfigBuilder<T> setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
    return this;
  }

  public ConsumerConfigBuilder<T> setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public ConsumerConfigBuilder<T> setInterfaceVersion(String interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
    return this;
  }

  public ConsumerConfigBuilder<T> setInterfaceTimeout(String interfaceTimeout) {
    this.interfaceTimeout = interfaceTimeout;
    return this;
  }

  public ConsumerConfigBuilder<T> setRegistryAddress(String registryAddress) {
    this.registryAddress = registryAddress;
    return this;
  }

  public ConsumerConfig<T> build() {
    return new ConsumerConfig<>(connectTimeout,
        readTimeout,
        directAddress,
        ioThreadNum,
        useWorkerThread,
        workerMinNum,
        workerMaxNum,
        clientType,
        serialization,
        codec,
        useHealthCheck,
        healthCheckPeriod,
        loadBalance,
        cluster,
        retryTimes,
        failbackPeriod,
        protocol,
        filterList,
        interfaceClass,
        interfaceName,
        interfaceVersion,
        interfaceTimeout,
        registryAddress);
  }
}
