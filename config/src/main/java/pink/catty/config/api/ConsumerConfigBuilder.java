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

public final class ConsumerConfigBuilder {

  private int connectTimeout;
  private int readTimeout;
  private String directAddress;
  private int ioThreadNum;
  private boolean useWorkerThread;
  private int workerMinNum;
  private int workerMaxNum;
  private String clientType;
  private String serialization;
  private String codec;
  private boolean useHealthCheck;
  private int healthCheckPeriod;
  private String loadBalance;
  private String cluster;
  private int retryTimes;
  private int failbackPeriod;
  private String protocol;
  private String filterList;
  private Class<?> interfaceClass;
  private String interfaceName;
  private String interfaceVersion;
  private String interfaceTimeout;
  private String registryAddress;

  ConsumerConfigBuilder() {
  }

  public ConsumerConfigBuilder setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public ConsumerConfigBuilder setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public ConsumerConfigBuilder setDirectAddress(String directAddress) {
    this.directAddress = directAddress;
    return this;
  }

  public ConsumerConfigBuilder setIoThreadNum(int ioThreadNum) {
    this.ioThreadNum = ioThreadNum;
    return this;
  }

  public ConsumerConfigBuilder setUseWorkerThread(boolean useWorkerThread) {
    this.useWorkerThread = useWorkerThread;
    return this;
  }

  public ConsumerConfigBuilder setWorkerMinNum(int workerMinNum) {
    this.workerMinNum = workerMinNum;
    return this;
  }

  public ConsumerConfigBuilder setWorkerMaxNum(int workerMaxNum) {
    this.workerMaxNum = workerMaxNum;
    return this;
  }

  public ConsumerConfigBuilder setClientType(String clientType) {
    this.clientType = clientType;
    return this;
  }

  public ConsumerConfigBuilder setSerialization(String serialization) {
    this.serialization = serialization;
    return this;
  }

  public ConsumerConfigBuilder setCodec(String codec) {
    this.codec = codec;
    return this;
  }

  public ConsumerConfigBuilder setUseHealthCheck(boolean useHealthCheck) {
    this.useHealthCheck = useHealthCheck;
    return this;
  }

  public ConsumerConfigBuilder setHealthCheckPeriod(int healthCheckPeriod) {
    this.healthCheckPeriod = healthCheckPeriod;
    return this;
  }

  public ConsumerConfigBuilder setLoadBalance(String loadBalance) {
    this.loadBalance = loadBalance;
    return this;
  }

  public ConsumerConfigBuilder setCluster(String cluster) {
    this.cluster = cluster;
    return this;
  }

  public ConsumerConfigBuilder setRetryTimes(int retryTimes) {
    this.retryTimes = retryTimes;
    return this;
  }

  public ConsumerConfigBuilder setFailbackPeriod(int failbackPeriod) {
    this.failbackPeriod = failbackPeriod;
    return this;
  }

  public ConsumerConfigBuilder setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public ConsumerConfigBuilder setFilterList(String filterList) {
    this.filterList = filterList;
    return this;
  }

  public ConsumerConfigBuilder setInterfaceClass(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
    return this;
  }

  public ConsumerConfigBuilder setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public ConsumerConfigBuilder setInterfaceVersion(String interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
    return this;
  }

  public ConsumerConfigBuilder setInterfaceTimeout(String interfaceTimeout) {
    this.interfaceTimeout = interfaceTimeout;
    return this;
  }

  public ConsumerConfigBuilder setRegistryAddress(String registryAddress) {
    this.registryAddress = registryAddress;
    return this;
  }

  public ConsumerConfig build() {
    return new ConsumerConfig(connectTimeout,
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
