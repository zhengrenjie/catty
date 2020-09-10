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
import pink.catty.core.config.definition.Define;
import pink.catty.core.config.definition.ProviderDefinition;

public final class ProviderConfigBuilder<T> {

  @Define(ProviderDefinition.LISTEN_PORT)
  private int port;

  @Define(ProviderDefinition.IO_THREAD_NUMBER)
  private int ioThreadNum;

  @Define(ProviderDefinition.USE_WORKER_THREAD)
  private boolean useWorkerThread;

  @Define(ProviderDefinition.WORKER_MIN_NUM)
  private int workerMinNum;

  @Define(ProviderDefinition.WORKER_MAX_NUM)
  private int workerMaxNum;

  @Define(ProviderDefinition.SERVER_TYPE)
  private String serverType;

  @Define(ProviderDefinition.SERIALIZATION)
  private String serialization;

  @Define(ProviderDefinition.CODEC)
  private String codec;

  @Define(ProviderDefinition.PROTOCOL)
  private String protocol;

  @Define(ProviderDefinition.FILTER_LIST)
  private List<String> filterList;

  @Define(ProviderDefinition.INTERFACE_CLASS)
  private Class<T> interfaceClass;

  @Define(ProviderDefinition.INTERFACE_NAME)
  private String interfaceName;

  @Define(ProviderDefinition.INTERFACE_VERSION)
  private String interfaceVersion;

  @Define(ProviderDefinition.INTERFACE_VERSION)
  private String interfaceTimeout;

  @Define(ProviderDefinition.REGISTRY_ADDRESS)
  private String registryAddress;

  ProviderConfigBuilder() {
    ConfigBuilderHelper
        .PrepareBuilder(this, ProviderDefinition.GetDefinition());
  }

  public ProviderConfigBuilder<T> setPort(int port) {
    this.port = port;
    return this;
  }

  public ProviderConfigBuilder<T> setIoThreadNum(int ioThreadNum) {
    this.ioThreadNum = ioThreadNum;
    return this;
  }

  public ProviderConfigBuilder<T> setUseWorkerThread(boolean useWorkerThread) {
    this.useWorkerThread = useWorkerThread;
    return this;
  }

  public ProviderConfigBuilder<T> setWorkerMinNum(int workerMinNum) {
    this.workerMinNum = workerMinNum;
    return this;
  }

  public ProviderConfigBuilder<T> setWorkerMaxNum(int workerMaxNum) {
    this.workerMaxNum = workerMaxNum;
    return this;
  }

  public ProviderConfigBuilder<T> setServerType(String serverType) {
    this.serverType = serverType;
    return this;
  }

  public ProviderConfigBuilder<T> setSerialization(String serialization) {
    this.serialization = serialization;
    return this;
  }

  public ProviderConfigBuilder<T> setCodec(String codec) {
    this.codec = codec;
    return this;
  }

  public ProviderConfigBuilder<T> setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  public ProviderConfigBuilder<T> addFilterList(String filter) {
    if (this.filterList == null) {
      this.filterList = new LinkedList<>();
    }
    this.filterList.add(filter);
    return this;
  }

  public ProviderConfigBuilder<T> setFilterList(List<String> filterList) {
    this.filterList = filterList;
    return this;
  }

  public ProviderConfigBuilder<T> setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
    return this;
  }

  public ProviderConfigBuilder<T> setInterfaceName(String interfaceName) {
    this.interfaceName = interfaceName;
    return this;
  }

  public ProviderConfigBuilder<T> setInterfaceVersion(String interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
    return this;
  }

  public ProviderConfigBuilder<T> setInterfaceTimeout(String interfaceTimeout) {
    this.interfaceTimeout = interfaceTimeout;
    return this;
  }

  public ProviderConfigBuilder<T> setRegistryAddress(String registryAddress) {
    this.registryAddress = registryAddress;
    return this;
  }

  public ProviderConfig<T> build() {
    return new ProviderConfig<>(port,
        ioThreadNum,
        useWorkerThread,
        workerMinNum,
        workerMaxNum,
        serverType,
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
