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

import java.net.InetSocketAddress;
import java.util.List;
import pink.catty.core.config.ConsumerConfig;
import pink.catty.core.config.ProviderConfig;
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
import pink.catty.core.support.Node;
import pink.catty.invokers.consumer.ConsumerClient;
import pink.catty.invokers.consumer.ConsumerCluster;
import pink.catty.invokers.consumer.ConsumerHealthCheck;
import pink.catty.invokers.consumer.ConsumerSerialization;
import pink.catty.invokers.provider.ProviderInvoker;
import pink.catty.invokers.provider.ProviderSerialization;

@Extension(ProtocolType.CATTY)
public class CattyProtocol implements Protocol {

  @Override
  public Consumer buildConsumer(ConsumerConfig config) {

    // 1.Create Cluster
    Cluster cluster = ExtensionFactory.cluster().getExtension(config.getCluster());

    // 2.Create LoadBalance
    LoadBalance loadBalance = ExtensionFactory.loadBalance().getExtension(config.getLoadBalance());

    // 3.Create ClusterInvoker
    Consumer consumer = new ConsumerCluster(config, cluster, loadBalance);

    // 4.Check if direct address set. Build Cluster.
    if (config.getDirectAddress() != null && config.getDirectAddress().size() > 0) {
      List<Node> nodeList = config.getDirectAddress();
      for (Node address : nodeList) {
        Consumer toRegister;

        // Create ConsumerClient.
        EndpointFactory factory = ExtensionFactory
            .endpointFactory()
            .getExtension(config.getClientType());
        Client client = factory
            .getClient(config, new InetSocketAddress(address.getIp(), address.getPort()));
        toRegister = new ConsumerClient(client, config);

        // Create SerializationConsumer
        Serialization serialization = ExtensionFactory
            .serialization()
            .getExtension(config.getSerialization());
        toRegister = new ConsumerSerialization(toRegister, serialization);

        // Create HealthCheckConsumer
        if (config.getHealthCheckPeriod() > 0) {
          toRegister = new ConsumerHealthCheck(toRegister);
        }

        // Wrap Filter
        if (config.getFilterList() != null && config.getFilterList().size() > 0) {
          for (int i = config.getFilterList().size() - 1; i >= 0; i--) {
            String filterName = (String) config.getFilterList().get(i);
            Filter filter = ExtensionFactory
                .filter()
                .getExtension(filterName);

            final Consumer last = toRegister;
            toRegister = new Consumer() {
              @Override
              public ConsumerConfig config() {
                return last.config();
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
        cluster.registerInvoker(address.toString(), toRegister);
      }
    }

    return consumer;
  }

  @Override
  public Provider buildProvider(ProviderConfig config) {
    Provider provider;
    Serialization serialization = ExtensionFactory
        .serialization()
        .getExtension(config.getSerialization());
    provider = new ProviderInvoker(config);

    // Wrap Filter
    if (config.getFilterList() != null && config.getFilterList().size() > 0) {
      for (int i = config.getFilterList().size() - 1; i >= 0; i--) {
        String filterName = (String) config.getFilterList().get(i);
        Filter filter = ExtensionFactory
            .filter()
            .getExtension(filterName);

        final Provider last = provider;
        provider = new Provider() {
          @Override
          public ProviderConfig config() {
            return last.config();
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

}
