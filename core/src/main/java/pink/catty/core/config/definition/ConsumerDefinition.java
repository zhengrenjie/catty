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
package pink.catty.core.config.definition;

import java.util.Map;
import java.util.Map.Entry;
import pink.catty.core.config.definition.ConfigDefine.Type;
import pink.catty.core.extension.ExtensionType.ClusterType;
import pink.catty.core.extension.ExtensionType.CodecType;
import pink.catty.core.extension.ExtensionType.EndpointFactoryType;
import pink.catty.core.extension.ExtensionType.LoadBalanceType;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.ExtensionType.SerializationType;

public class ConsumerDefinition {

  /* ****************
   *     Client
   * ***************/

  /**
   * Client connect timeout. ms
   */
  public static final String CONNECT_TIMEOUT = "consumer.client.connect_timeout.ms";

  /**
   * Client read timeout. ms
   */
  public static final String READ_TIMEOUT = "consumer.client.read_timeout.ms";

  /**
   * If registry is set, then use registry to get remote address first, or use direct address.
   */
  public static final String DIRECT_ADDRESSES = "consumer.client.direct_addresses";

  /**
   * The io thread number of client. If not use I/O multiplexing like netty(select, epoll, etc.),
   * this config will be ignored.
   */
  public static final String IO_THREAD_NUMBER = "consumer.client.io_thread_number";

  /**
   * If use a worker thread pool to fire a request, otherwise use io thread.
   */
  public static final String USE_WORKER_THREAD = "consumer.client.use_worker_thread";

  /**
   * If USE_IO_THREAD == false, the client will create a worker thread pool to execute tasks.
   * WORKER_MIN_NUM indicated the min thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  public static final String WORKER_MIN_NUM = "consumer.client.worker_min_number";

  /**
   * If USE_IO_THREAD == false, the client will create a worker thread pool to execute tasks.
   * WORKER_MAX_NUM indicated the max thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  public static final String WORKER_MAX_NUM = "consumer.client.worker_max_number";

  /**
   * @see pink.catty.core.extension.spi.EndpointFactory
   * @see EndpointFactoryType
   */
  public static final String CLIENT_TYPE = "consumer.client.type";


  /* ****************************
   *    Codec & Serialization
   * ***************************/

  /**
   * Which serialization to use.
   *
   * @see pink.catty.core.extension.spi.Serialization
   * @see SerializationType
   */
  public static final String SERIALIZATION = "consumer.serialization";

  /**
   * Which codec to use.
   *
   * @see pink.catty.core.extension.spi.Codec
   * @see CodecType
   */
  public static final String CODEC = "consumer.codec";


  /* ****************************
   *           Cluster
   * ***************************/

  /**
   * If periodically testing the server is alive.
   */
  public static final String USE_HEALTH_CHECK = "consumer.use_health_check";

  /**
   * Period of health check. If useHealthCheck == false, this config will be ignored.
   */
  public static final String HEALTH_CHECK_PERIOD = "consumer.health_check_period";

  /**
   * @see pink.catty.core.extension.spi.LoadBalance
   * @see LoadBalanceType
   */
  public static final String LOAD_BALANCE = "consumer.load_balance";

  /**
   * @see pink.catty.core.extension.spi.Cluster
   * @see ClusterType
   */
  public static final String HA_STRATEGY = "consumer.ha_strategy";

  /**
   * If cluster type is fail-over, this config indicates the times of retry. If cluster type is NOT
   * fail-over, this config will be ignored.
   */
  public static final String RETRY_TIMES = "consumer.retry_times";

  /**
   * If cluster type is fail-back, this config indicates the period of reconnecting. If cluster type
   * is NOT fail-back, this config will be ignored.
   */
  public static final String FAIL_BACK_PERIOD = "consumer.fail_back_period.ms";


  /* ****************************
   *          protocol
   * ***************************/

  /**
   * @see pink.catty.core.extension.spi.Protocol
   * @see ProtocolType
   */
  public static final String PROTOCOL = "consumer.protocol";

  /**
   * Filter list.
   */
  public static final String FILTER_LIST = "consumer.filter";


  /* ****************************
   * interface & method(not support yet)
   * ***************************/

  /**
   * The interface of service.
   */
  public static final String INTERFACE_CLASS = "consumer.interface.class";

  /**
   * Name of the interface, if not set, interface' class name will be used.
   */
  public static final String INTERFACE_NAME = "consumer.interface.name";

  /**
   * Version of the interface.
   */
  public static final String INTERFACE_VERSION = "consumer.interface.version";

  /**
   * The timeout of interface, every methods in this interface have the same timeout unless method
   * has specified its own timeout.
   */
  public static final String INTERFACE_TIMEOUT = "consumer.interface.timeout.ms";


  /* ****************************
   *          registry
   * ***************************/

  /**
   * Registry address, if this config is set, the client.direct_address will be ignored.
   */
  public static final String REGISTRY_ADDRESS = "consumer.registry.address";


  private static final Definition DEFINITION;

  static {
    DEFINITION = new Definition();

    // CONNECT_TIMEOUT
    ConfigDefine.define(DEFINITION)
        .withName(CONNECT_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // READ_TIMEOUT
    ConfigDefine.define(DEFINITION)
        .withName(READ_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // DIRECT_ADDRESS
    ConfigDefine.define(DEFINITION)
        .withName(DIRECT_ADDRESSES)
        .withType(Type.OBJECT)
        .withDefaultValue(Validators.MUST_LIST)
        .done();

    // IO_THREAD_NUMBER
    ConfigDefine.define(DEFINITION)
        .withName(IO_THREAD_NUMBER)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() + 1)
        .done();

    // USE_WORKER_THREAD
    ConfigDefine.define(DEFINITION)
        .withName(USE_WORKER_THREAD)
        .withType(Type.BOOLEAN)
        .withDefaultValue(Boolean.FALSE)
        .done();

    // WORKER_MIN_NUM
    ConfigDefine.define(DEFINITION)
        .withName(WORKER_MIN_NUM)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() * 2)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // WORKER_MAX_NUM
    ConfigDefine.define(DEFINITION)
        .withName(WORKER_MAX_NUM)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() * 4)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // CLIENT_TYPE
    ConfigDefine.define(DEFINITION)
        .withName(CLIENT_TYPE)
        .withType(Type.STRING)
        .withDefaultValue(EndpointFactoryType.NETTY)
        .done();

    // SERIALIZATION
    ConfigDefine.define(DEFINITION)
        .withName(SERIALIZATION)
        .withType(Type.STRING)
        .withDefaultValue(SerializationType.HESSIAN2)
        .done();

    // CODEC
    ConfigDefine.define(DEFINITION)
        .withName(CODEC)
        .withType(Type.STRING)
        .withDefaultValue(CodecType.CATTY)
        .done();

    // USE_HEALTH_CHECK
    ConfigDefine.define(DEFINITION)
        .withName(USE_HEALTH_CHECK)
        .withType(Type.BOOLEAN)
        .withDefaultValue(Boolean.TRUE)
        .done();

    // HEALTH_CHECK_PERIOD
    ConfigDefine.define(DEFINITION)
        .withName(HEALTH_CHECK_PERIOD)
        .withType(Type.INT)
        .withDefaultValue(5000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // LOAD_BALANCE
    ConfigDefine.define(DEFINITION)
        .withName(LOAD_BALANCE)
        .withType(Type.STRING)
        .withDefaultValue(LoadBalanceType.WEIGHTED_RANDOM)
        .done();

    // HA_STRATEGY
    ConfigDefine.define(DEFINITION)
        .withName(HA_STRATEGY)
        .withType(Type.STRING)
        .withDefaultValue(ClusterType.FAIL_FAST)
        .done();

    // RETRY_TIMES
    ConfigDefine.define(DEFINITION)
        .withName(RETRY_TIMES)
        .withType(Type.INT)
        .withDefaultValue(3)
        .done();

    // FAIL_BACK_PERIOD
    ConfigDefine.define(DEFINITION)
        .withName(FAIL_BACK_PERIOD)
        .withType(Type.INT)
        .withDefaultValue(3000)
        .done();

    // PROTOCOL
    ConfigDefine.define(DEFINITION)
        .withName(PROTOCOL)
        .withType(Type.STRING)
        .withDefaultValue(ProtocolType.CATTY)
        .done();

    // FILTER_LIST
    ConfigDefine.define(DEFINITION)
        .withName(FILTER_LIST)
        .withType(Type.OBJECT)
        .withDefaultValue(Validators.MUST_LIST)
        .done();

    // INTERFACE_CLASS
    ConfigDefine.define(DEFINITION)
        .withName(INTERFACE_CLASS)
        .withType(Type.OBJECT)
        .withValidator(Validators.MUST_CLASS)
        .done();

    // INTERFACE_NAME
    ConfigDefine.define(DEFINITION)
        .withName(INTERFACE_NAME)
        .withType(Type.STRING)
        .done();

    // INTERFACE_VERSION
    ConfigDefine.define(DEFINITION)
        .withName(INTERFACE_VERSION)
        .withType(Type.STRING)
        .done();

    // INTERFACE_TIMEOUT
    ConfigDefine.define(DEFINITION)
        .withName(INTERFACE_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .done();

    // REGISTRY_ADDRESS
    ConfigDefine.define(DEFINITION)
        .withName(REGISTRY_ADDRESS)
        .withType(Type.STRING)
        .done();
  }

  public static Definition GetDefinition() {
    return DEFINITION;
  }

  public static ConfigDefine GetConfigDefine(String key) {
    return DEFINITION.getConfigDefine(key);
  }

  public static void AddConfigDefine(String key, ConfigDefine define) {
    DEFINITION.addConfigDefine(key, define);
  }

  public static boolean ContainsName(String name) {
    return DEFINITION.containsName(name);
  }

  public static void RemoveDefine(String key) {
    DEFINITION.removeDefine(key);
  }

  /**
   * Valid if each config in "configs" is valid.
   *
   * @param configs configs
   */
  public static void valid(Map<Object, Object> configs) {
    if (configs == null) {
      throw new NullPointerException("configs arg is null.");
    }

    // 0. registry address & direct address shouldn't both be null.
    Object registryAddress = configs.get(REGISTRY_ADDRESS);
    Object directAddresses = configs.get(DIRECT_ADDRESSES);
    if (registryAddress == null && directAddresses == null) {
      throw new ValidException("registry address & direct address shouldn't both be null.");
    }

    // 1. valid every config.
    for (Entry<Object, Object> config : configs.entrySet()) {
      if (!(config.getKey() instanceof String)) {
        // this should not happen.
        throw new IllegalArgumentException("Setting's key must be String");
      }

      ConfigDefine define = DEFINITION.getConfigDefine((String) config.getKey());
      if (define == null) {
        throw new ValidException("Setting was not defined, setting: " + (String) config.getKey());
      }

      define.valid(config.getValue());
    }
  }

}
