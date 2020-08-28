package pink.catty.config.define;

import java.util.Map;
import java.util.Map.Entry;
import pink.catty.config.define.ConfigDefine.Type;
import pink.catty.core.extension.ExtensionType.ClusterType;
import pink.catty.core.extension.ExtensionType.CodecType;
import pink.catty.core.extension.ExtensionType.EndpointFactoryType;
import pink.catty.core.extension.ExtensionType.LoadBalanceType;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.ExtensionType.SerializationType;

public class ConsumerConfig {

  /* ****************
   *     Client
   * ***************/

  /**
   * Client connect timeout. ms
   */
  public static final String CONNECT_TIMEOUT = "consumer.client.connect_timeout";

  /**
   * Client read timeout. ms
   */
  public static final String READ_TIMEOUT = "consumer.client.read_timeout";

  /**
   * If REGISTRY_CONFIG is set, then use registry to get remote address first, or use direct
   * address.
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
   * WORKER_MIN_NUM indicated the min thread number of worker thread pool.
   */
  public static final String WORKER_MIN_NUM = "consumer.client.worker_min_number";

  /**
   * If USE_IO_THREAD == false, the client will create a worker thread pool to execute tasks.
   * WORKER_MAX_NUM indicated the max thread number of worker thread pool.
   */
  public static final String WORKER_MAX_NUM = "consumer.client.worker_max_number";

  /**
   * @see pink.catty.core.extension.spi.EndpointFactory
   */
  private static final String CLIENT_TYPE = "consumer.client.client_type";


  /* ****************************
   *    Codec & Serialization
   * ***************************/

  /**
   * Which serialization to use.
   *
   * @see pink.catty.core.extension.spi.Serialization
   */
  public static final String SERIALIZATION = "consumer.serialization";

  /**
   * Which codec to use.
   *
   * @see pink.catty.core.extension.spi.Codec
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
   * Period of health check.
   */
  public static final String HEALTH_CHECK_PERIOD = "consumer.health_check_period";

  /**
   * @see pink.catty.core.extension.spi.LoadBalance
   */
  public static final String LOAD_BALANCE = "consumer.load_balance";

  /**
   * @see pink.catty.core.extension.spi.Cluster
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
  public static final String FAIL_BACK_PERIOD = "consumer.fail_back_period";


  /* ****************************
   *          protocol
   * ***************************/

  /**
   * @see pink.catty.core.extension.spi.Protocol
   */
  public static final String PROTOCOL = "consumer.protocol";

  /**
   * Filter list.
   */
  public static final String FILTER_LIST = "consumer.filter_list";


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
  public static final String INTERFACE_TIMEOUT = "consumer.interface.timeout";


  /* ****************************
   *          registry
   * ***************************/

  /**
   * Registry address, if this config is set, the client.direct_address will be ignored.
   */
  public static final String REGISTRY_ADDRESS = "consumer.registry.address";


  private static final Definitions DEFINITIONS;

  static {
    DEFINITIONS = new Definitions();

    // CONNECT_TIMEOUT
    ConfigDefine.define(DEFINITIONS)
        .withName(CONNECT_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // READ_TIMEOUT
    ConfigDefine.define(DEFINITIONS)
        .withName(READ_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // DIRECT_ADDRESS
    ConfigDefine.define(DEFINITIONS)
        .withName(DIRECT_ADDRESSES)
        .withType(Type.OBJECT)
        .withDefaultValue(Validators.MUST_LIST)
        .done();

    // IO_THREAD_NUMBER
    ConfigDefine.define(DEFINITIONS)
        .withName(IO_THREAD_NUMBER)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() + 1)
        .done();

    // USE_WORKER_THREAD
    ConfigDefine.define(DEFINITIONS)
        .withName(USE_WORKER_THREAD)
        .withType(Type.BOOLEAN)
        .withDefaultValue(Boolean.FALSE)
        .done();

    // WORKER_MIN_NUM
    ConfigDefine.define(DEFINITIONS)
        .withName(WORKER_MIN_NUM)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() * 2)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // WORKER_MAX_NUM
    ConfigDefine.define(DEFINITIONS)
        .withName(WORKER_MAX_NUM)
        .withType(Type.INT)
        .withDefaultValue(Runtime.getRuntime().availableProcessors() * 4)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // CLIENT_TYPE
    ConfigDefine.define(DEFINITIONS)
        .withName(CLIENT_TYPE)
        .withType(Type.STRING)
        .withDefaultValue(EndpointFactoryType.NETTY)
        .done();

    // SERIALIZATION
    ConfigDefine.define(DEFINITIONS)
        .withName(SERIALIZATION)
        .withType(Type.STRING)
        .withDefaultValue(SerializationType.HESSIAN2)
        .done();

    // CODEC
    ConfigDefine.define(DEFINITIONS)
        .withName(CODEC)
        .withType(Type.STRING)
        .withDefaultValue(CodecType.CATTY)
        .done();

    // USE_HEALTH_CHECK
    ConfigDefine.define(DEFINITIONS)
        .withName(USE_HEALTH_CHECK)
        .withType(Type.BOOLEAN)
        .withDefaultValue(Boolean.TRUE)
        .done();

    // HEALTH_CHECK_PERIOD
    ConfigDefine.define(DEFINITIONS)
        .withName(HEALTH_CHECK_PERIOD)
        .withType(Type.INT)
        .withDefaultValue(5000)
        .withValidator(Validators.MUST_POSITIVE)
        .done();

    // LOAD_BALANCE
    ConfigDefine.define(DEFINITIONS)
        .withName(LOAD_BALANCE)
        .withType(Type.STRING)
        .withDefaultValue(LoadBalanceType.WEIGHTED_RANDOM)
        .done();

    // HA_STRATEGY
    ConfigDefine.define(DEFINITIONS)
        .withName(HA_STRATEGY)
        .withType(Type.STRING)
        .withDefaultValue(ClusterType.FAIL_FAST)
        .done();

    // RETRY_TIMES
    ConfigDefine.define(DEFINITIONS)
        .withName(RETRY_TIMES)
        .withType(Type.INT)
        .withDefaultValue(3)
        .done();

    // FAIL_BACK_PERIOD
    ConfigDefine.define(DEFINITIONS)
        .withName(FAIL_BACK_PERIOD)
        .withType(Type.INT)
        .withDefaultValue(3000)
        .done();

    // PROTOCOL
    ConfigDefine.define(DEFINITIONS)
        .withName(PROTOCOL)
        .withType(Type.STRING)
        .withDefaultValue(ProtocolType.CATTY)
        .done();

    // FILTER_LIST
    ConfigDefine.define(DEFINITIONS)
        .withName(FILTER_LIST)
        .withType(Type.OBJECT)
        .withDefaultValue(Validators.MUST_LIST)
        .done();

    // INTERFACE_CLASS
    ConfigDefine.define(DEFINITIONS)
        .withName(INTERFACE_CLASS)
        .withType(Type.OBJECT)
        .withValidator(Validators.MUST_CLASS)
        .done();

    // INTERFACE_NAME
    ConfigDefine.define(DEFINITIONS)
        .withName(INTERFACE_NAME)
        .withType(Type.STRING)
        .done();

    // INTERFACE_VERSION
    ConfigDefine.define(DEFINITIONS)
        .withName(INTERFACE_VERSION)
        .withType(Type.STRING)
        .done();

    // INTERFACE_TIMEOUT
    ConfigDefine.define(DEFINITIONS)
        .withName(INTERFACE_TIMEOUT)
        .withType(Type.INT)
        .withDefaultValue(1000)
        .done();

    // REGISTRY_ADDRESS
    ConfigDefine.define(DEFINITIONS)
        .withName(REGISTRY_ADDRESS)
        .withType(Type.STRING)
        .done();
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

      ConfigDefine define = DEFINITIONS.getConfigDefine((String) config.getKey());
      if (define == null) {
        throw new ValidException("Setting was not defined, setting: " + (String) config.getKey());
      }

      define.valid(config.getValue());
    }
  }

}
