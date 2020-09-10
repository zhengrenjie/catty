package pink.catty.config;

import pink.catty.core.config.ConsumerConfig;
import pink.catty.core.extension.ExtensionFactory;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.spi.Protocol;
import pink.catty.core.invoker.Consumer;
import pink.catty.core.meta.ConsumerMeta;
import pink.catty.core.model.ServiceModel;
import pink.catty.invokers.consumer.ConsumerHandler;

public class Catty<T> {

  private T ref;

  public T refer(ConsumerConfig<T> config) {
    if (ref == null) {
      ServiceModel<T> serviceModel = ServiceModel.Parse(config.getInterfaceClass());

      ConsumerMeta consumerMeta = new ConsumerMeta();
      consumerMeta.setSerialization(config.getSerialization());
      consumerMeta.setCodec(config.getCodec());
      consumerMeta.setEndpoint(config.getClientType());
      consumerMeta.setHealthCheckPeriod(config.getHealthCheckPeriod());
      consumerMeta.setCluster(config.getCluster());
      consumerMeta.setLoadBalance(config.getLoadBalance());
      consumerMeta.setRetryTimes(config.getRetryTimes());
      consumerMeta.setRecoveryPeriod(config.getFailbackPeriod());
      consumerMeta.setDirectAddress(config.getDirectAddress());
      consumerMeta.setTimeout(config.getReadTimeout());
      consumerMeta.setServiceName(serviceModel.getServiceName());

      Protocol protocol = ExtensionFactory.protocol().getExtension(ProtocolType.CATTY);
      Consumer consumer = protocol.buildConsumer(consumerMeta);
      ref = ConsumerHandler.getProxy(consumer, serviceModel);
    }
    return ref;
  }

}
