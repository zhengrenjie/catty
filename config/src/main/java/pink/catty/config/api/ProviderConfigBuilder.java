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

public final class ProviderConfigBuilder {

  private int port;
  private int ioThreadNum;
  private boolean useWorkerThread;
  private int workerMinNum;
  private int workerMaxNum;
  private String clientType;
  private String serialization;
  private String codec;
  private String protocol;
  private String filterList;
  private Class<?> interfaceClass;
  private String interfaceName;
  private String interfaceVersion;
  private String interfaceTimeout;
  private String registryAddress;

  ProviderConfigBuilder() {
  }

  public ProviderConfigBuilder setPort(int port) {
    this.port = port;
    return this;
  }

  public ProviderConfigBuilder setIoThreadNum(int ioThreadNum) {
    this.ioThreadNum = ioThreadNum;
    return this;
  }

  public ProviderConfigBuilder setUseWorkerThread(boolean useWorkerThread) {
    this.useWorkerThread = useWorkerThread;
    return this;
  }

  public ProviderConfigBuilder setWorkerMinNum(int workerMinNum) {
    this.workerMinNum = workerMinNum;
    return this;
  }

  public ProviderConfigBuilder setWorkerMaxNum(int workerMaxNum) {
    this.workerMaxNum = workerMaxNum;
    return this;
  }

  public ProviderConfigBuilder setClientType(String clientType) {
    this.clientType = clientType;
    return this;
  }

  public ProviderConfigBuilder setSerialization(String serialization) {
    this.serialization = serialization;
    return this;
  }

  public ProviderConfigBuilder setCodec(String codec) {
    this.codec = codec;
    return this;
  }

  public ProviderConfigBuilder setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public ProviderConfigBuilder setFilterList(String filterList) {
    this.filterList = filterList;
    return this;
  }

  public ProviderConfigBuilder setInterfaceClass(Class<?> interfaceClass) {
    this.interfaceClass = interfaceClass;
    return this;
  }

  public ProviderConfigBuilder setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public ProviderConfigBuilder setInterfaceVersion(String interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
    return this;
  }

  public ProviderConfigBuilder setInterfaceTimeout(String interfaceTimeout) {
    this.interfaceTimeout = interfaceTimeout;
    return this;
  }

  public ProviderConfigBuilder setRegistryAddress(String registryAddress) {
    this.registryAddress = registryAddress;
    return this;
  }

  public ProviderConfig build() {
    return new ProviderConfig(port,
        ioThreadNum,
        useWorkerThread,
        workerMinNum,
        workerMaxNum,
        clientType,
        serialization,
        codec,
        protocol,
        filterList,
        interfaceClass,
        interfaceName,
        interfaceVersion,
        interfaceTimeout,
        registryAddress);
  }
}
