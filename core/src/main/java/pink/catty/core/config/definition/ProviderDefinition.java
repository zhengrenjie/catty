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

import pink.catty.core.config.definition.ConfigDefine.Type;
import pink.catty.core.extension.ExtensionType.CodecType;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.ExtensionType.SerializationType;

public class ProviderDefinition {

  /* ****************
   *     Server
   * ***************/

  /**
   * Listen port of server.
   */
  public static final String LISTEN_PORT = "provider.server.port";

  /**
   * The io thread number of server. If not use I/O multiplexing like netty(select, epoll, etc.),
   * this config will be ignored.
   */
  public static final String IO_THREAD_NUMBER = "provider.server.io_thread_number";

  /**
   * If use a worker thread pool to fire a request, otherwise use io thread.
   */
  public static final String USE_WORKER_THREAD = "provider.server.use_worker_thread";

  /**
   * If USE_IO_THREAD == false, the server will create a worker thread pool to execute tasks.
   * WORKER_MIN_NUM indicated the min thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  public static final String WORKER_MIN_NUM = "provider.server.worker_min_number";

  /**
   * If USE_IO_THREAD == false, the server will create a worker thread pool to execute tasks.
   * WORKER_MAX_NUM indicated the max thread number of worker thread pool. Otherwise this config
   * will be ignored.
   */
  public static final String WORKER_MAX_NUM = "provider.server.worker_max_number";
  
  /**
   * @see pink.catty.core.extension.spi.EndpointFactory
   * @see pink.catty.core.extension.ExtensionType.EndpointFactoryType
   */
  public static final String SERVER_TYPE = "provider.server.type";


  /* ****************************
   *    Codec & Serialization
   * ***************************/

  /**
   * Which serialization to use.
   *
   * @see pink.catty.core.extension.spi.Serialization
   * @see SerializationType
   */
  public static final String SERIALIZATION = "provider.serialization";

  /**
   * Which codec to use.
   *
   * @see pink.catty.core.extension.spi.Codec
   * @see CodecType
   */
  public static final String CODEC = "provider.codec";

  /* ****************************
   *          protocol
   * ***************************/

  /**
   * @see pink.catty.core.extension.spi.Protocol
   * @see ProtocolType
   */
  public static final String PROTOCOL = "provider.protocol";

  /**
   * Filter list.
   */
  public static final String FILTER_LIST = "provider.filter";

  
  /* ****************************
   * interface & method(not support yet)
   * ***************************/

  /**
   * The interface of service.
   */
  public static final String INTERFACE_CLASS = "provider.interface.class";

  /**
   * Name of the interface, if not set, interface' class name will be used.
   */
  public static final String INTERFACE_NAME = "provider.interface.name";

  /**
   * Version of the interface.
   */
  public static final String INTERFACE_VERSION = "provider.interface.version";

  /**
   * The timeout of interface, every methods in this interface have the same timeout unless method
   * has specified its own timeout.
   */
  public static final String INTERFACE_TIMEOUT = "provider.interface.timeout.ms";


  /* ****************************
   *          registry
   * ***************************/

  /**
   * Registry address, if this config is set, the server.direct_address will be ignored.
   */
  public static final String REGISTRY_ADDRESS = "provider.registry.address";

  private static final Definition DEFINITION;

  static {
    DEFINITION = new Definition();

    // LISTEN_PORT
    ConfigDefine.define(DEFINITION)
        .withName(LISTEN_PORT)
        .withType(Type.INT)
        .withDefaultValue(9527)
        .withValidator(Validators.MUST_POSITIVE)
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

  public static Definition getDefinition() {
    return DEFINITION;
  }

  public static ConfigDefine getConfigDefine(String key) {
    return DEFINITION.getConfigDefine(key);
  }

  public static void addConfigDefine(String key, ConfigDefine define) {
    DEFINITION.addConfigDefine(key, define);
  }

  public static boolean containsName(String name) {
    return DEFINITION.containsName(name);
  }

  public static void removeDefine(String key) {
    DEFINITION.removeDefine(key);
  }
}
