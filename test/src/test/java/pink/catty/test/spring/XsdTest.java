/*
 * Copyright 2020 The Catty Project
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
package pink.catty.test.spring;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pink.catty.config.api.ClientConfig;
import pink.catty.config.api.ProtocolConfig;
import pink.catty.config.api.ServerConfig;
import pink.catty.core.Node;

public class XsdTest {

  @Test
  public void testProtocol() {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("xsd_test.xml");
    ProtocolConfig protocolConfig = context.getBean(ProtocolConfig.class);
    Assert.assertEquals(protocolConfig.getClusterType(), "fail-fast");
    Assert.assertEquals(protocolConfig.getCodecType(), "catty");
    Assert.assertEquals(protocolConfig.getSerializationType(), "hessian2");
    Assert.assertEquals(protocolConfig.getEndpointType(), "netty");
    Assert.assertEquals(protocolConfig.getLoadBalanceType(), "random");
    Assert.assertEquals(protocolConfig.getRetryTimes(), 3);
    Assert.assertEquals(protocolConfig.getRecoveryPeriod(), 5000);
    Assert.assertEquals(protocolConfig.getHeartbeatPeriod(), 30000);
  }

  @Test
  public void testClientConfig() {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("xsd_test.xml");
    ClientConfig clientConfig = context.getBean(ClientConfig.class);
    Assert.assertEquals(clientConfig.getTimeout(), 3000);
    List<Node> addressList = clientConfig.getAddresses();
    Assert.assertEquals(addressList.size(), 2);
    Assert.assertEquals(addressList.get(0), new Node("127.0.0.1", 8080));
    Assert.assertEquals(addressList.get(1), new Node("127.0.0.1", 8081));
  }

  @Test
  public void testServerConfig() {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("xsd_test.xml");
    ServerConfig serverConfig1 = (ServerConfig) context.getBean("server1");
    Assert.assertEquals(serverConfig1.getPort(), 8080);
    Assert.assertEquals(serverConfig1.getWorkerThreadNum(), 2);
    Assert.assertEquals(serverConfig1.getMinWorkerThreadNum(), 1);
    Assert.assertEquals(serverConfig1.getMaxWorkerThreadNum(), 2);

    ServerConfig serverConfig2 = (ServerConfig) context.getBean("server2");
    Assert.assertEquals(serverConfig2.getPort(), 8081);
    Assert.assertEquals(serverConfig2.getWorkerThreadNum(), 2);
    Assert.assertEquals(serverConfig2.getMinWorkerThreadNum(), 1);
    Assert.assertEquals(serverConfig2.getMaxWorkerThreadNum(), 2);
  }

}
