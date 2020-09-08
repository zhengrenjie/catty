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
package pink.catty.extension.protocol;

import pink.catty.core.Node;
import pink.catty.core.extension.Extension;
import pink.catty.core.extension.ExtensionFactory;
import pink.catty.core.extension.ExtensionType.ProtocolType;
import pink.catty.core.extension.spi.Cluster;
import pink.catty.core.extension.spi.EndpointFactory;
import pink.catty.core.extension.spi.Filter;
import pink.catty.core.extension.spi.LoadBalance;
import pink.catty.core.extension.spi.Protocol;
import pink.catty.core.extension.spi.Serialization;
import pink.catty.core.invoker.Consumer;
import pink.catty.core.invoker.Invoker;
import pink.catty.core.invoker.Provider;
import pink.catty.core.invoker.endpoint.Client;
import pink.catty.core.invoker.frame.Request;
import pink.catty.core.invoker.frame.Response;
import pink.catty.core.meta.ConsumerMeta;
import pink.catty.core.meta.MetaInfo;
import pink.catty.core.meta.ProviderMeta;
import pink.catty.invokers.consumer.ConsumerClient;
import pink.catty.invokers.consumer.ConsumerCluster;
import pink.catty.invokers.consumer.ConsumerHealthCheck;
import pink.catty.invokers.consumer.ConsumerSerialization;
import pink.catty.invokers.provider.ProviderInvoker;
import pink.catty.invokers.provider.ProviderSerialization;

@Extension(ProtocolType.CATTY)
public class CattyProtocol implements Protocol {

  @Override
  public Consumer buildConsumer(ConsumerMeta meta) {

    /*
     * If only one remote address is specified, CattyProtocol will not create cluster.
     */
    if (meta.getDirectAddress() != null && meta.getDirectAddress().size() == 1) {

      // copy meta
      String metaString = meta.toString();
      ConsumerMeta newMetaInfo = MetaInfo.parseOf(metaString, ConsumerMeta.class);

      // set address
      Node address = meta.getDirectAddress().get(0);
      newMetaInfo.setRemoteIp(address.getIp());
      newMetaInfo.setRemotePort(address.getPort());

      return createConsumerLink(newMetaInfo);
    }

    /*
     * Else, if more than one remote address is specified, CattyProtocol will create a cluster.
     */

    // 1.Create Cluster
    Cluster cluster = ExtensionFactory.cluster().getExtension(meta.getCluster());

    // 2.Create LoadBalance
    LoadBalance loadBalance = ExtensionFactory.loadBalance().getExtension(meta.getLoadBalance());

    // 3.Create ClusterInvoker
    Consumer consumer = new ConsumerCluster(meta, cluster, loadBalance);

    // 4.Check if direct address set. Build Cluster.
    if (meta.getDirectAddress() != null && meta.getDirectAddress().size() > 0) {
      String metaString = meta.toString();
      for (Node address : meta.getDirectAddress()) {
        Consumer toRegister;
        ConsumerMeta newMetaInfo = MetaInfo.parseOf(metaString, ConsumerMeta.class);
        newMetaInfo.setRemoteIp(address.getIp());
        newMetaInfo.setRemotePort(address.getPort());

        toRegister = createConsumerLink(newMetaInfo);

        // Wrap Filter
        if (newMetaInfo.getFilterNames() != null && newMetaInfo.getFilterNames().size() > 0) {
          for (int i = newMetaInfo.getFilterNames().size() - 1; i >= 0; i--) {
            String filterName = newMetaInfo.getFilterNames().get(i);
            Filter filter = ExtensionFactory
                .filter()
                .getExtension(filterName);

            final Consumer last = toRegister;
            toRegister = new Consumer() {
              @Override
              public ConsumerMeta getMeta() {
                return last.getMeta();
              }

              @Override
              public Invoker getNext() {
                return last.getNext();
              }

              @Override
              public Response invoke(Request request) {
                return filter.filter(last, request);
              }

              @Override
              public void setNext(Invoker invoker) {
                last.setNext(invoker);
              }
            };
          }
        }

        // Register to Cluster
        cluster.registerInvoker(newMetaInfo.toString(), toRegister);
      }
    }

    return consumer;
  }

  @Override
  public Provider buildProvider(ProviderMeta meta) {
    Provider provider;
    Serialization serialization = ExtensionFactory
        .serialization()
        .getExtension(meta.getSerialization());
    provider = new ProviderInvoker(meta);

    // Wrap Filter
    if (meta.getFilterNames() != null && meta.getFilterNames().size() > 0) {
      for (int i = meta.getFilterNames().size() - 1; i >= 0; i--) {
        String filterName = meta.getFilterNames().get(i);
        Filter filter = ExtensionFactory
            .filter()
            .getExtension(filterName);

        final Provider last = provider;
        provider = new Provider() {
          @Override
          public ProviderMeta getMeta() {
            return last.getMeta();
          }

          @Override
          public Invoker getNext() {
            return last.getNext();
          }

          @Override
          public Response invoke(Request request) {
            return filter.filter(last, request);
          }

          @Override
          public void setNext(Invoker invoker) {
            last.setNext(invoker);
          }
        };
      }
    }

    return new ProviderSerialization(provider, serialization);
  }

  private Consumer createConsumerLink(ConsumerMeta consumerMeta) {
    // Create ConsumerClient.
    EndpointFactory factory = ExtensionFactory
        .endpointFactory()
        .getExtension(consumerMeta.getEndpoint());
    Client client = factory.getClient(consumerMeta);
    Consumer consumer = new ConsumerClient(client, consumerMeta);

    // Create SerializationConsumer
    Serialization serialization = ExtensionFactory
        .serialization()
        .getExtension(consumerMeta.getSerialization());
    consumer = new ConsumerSerialization(consumer, serialization);

    // Create HealthCheckConsumer
    if (consumerMeta.getHealthCheckPeriod() > 0) {
      consumer = new ConsumerHealthCheck(consumer);
    }
    return consumer;
  }

}
