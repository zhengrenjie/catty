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
package pink.catty.test.sdk;

import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.catty.config.api.ClientConfig;
import pink.catty.config.api.Exporter;
import pink.catty.config.api.ProtocolConfig;
import pink.catty.config.api.Reference;
import pink.catty.config.api.ServerConfig;
import pink.catty.core.extension.ExtensionType.ClusterType;
import pink.catty.test.service.AService;
import pink.catty.test.service.AServiceImpl;
import pink.catty.test.service.BService;
import pink.catty.test.service.BServiceImpl;

public class SdkMultiSourceTest {

  private static final Logger logger = LoggerFactory.getLogger(SdkMultiSourceTest.class);

  private static ProtocolConfig providerProtocol;

  private static AService aService;
  private static BService bService;

  private static Exporter exporter0;
  private static Exporter exporter1;
  //  private static Exporter exporter2;

  private static Reference<AService> reference0;
  private static Reference<BService> reference1;

  @BeforeClass
  public static void init() {
    providerProtocol = ProtocolConfig.defaultConfig();
    ServerConfig serverConfig = ServerConfig.builder()
        .port(20550)
        .build();
    exporter0 = new Exporter(serverConfig);
    exporter0.setProtocolConfig(providerProtocol);
    exporter0.registerService(AService.class, new AServiceImpl());
    exporter0.registerService(BService.class, new BServiceImpl());
    exporter0.export();
    serverConfig = ServerConfig.builder()
        .port(20551)
        .build();
    exporter1 = new Exporter(serverConfig);
    exporter1.setProtocolConfig(providerProtocol);
    exporter1.registerService(AService.class, new AServiceImpl());
    exporter1.registerService(BService.class, new BServiceImpl());
    exporter1.export();
//    serverConfig = ServerConfig.builder()
//        .port(20552)
//        .build();
//    exporter2 = new Exporter(serverConfig);
//    exporter2.setProtocolConfig(providerProtocol);
//    exporter2.registerService(AService.class, new AServiceImpl());
//    exporter2.registerService(BService.class, new BServiceImpl());
//    exporter2.export();

    ClientConfig clientConfig = ClientConfig.builder()
        .addAddress("127.0.0.1:20550")
        .addAddress("127.0.0.1:20551")
//        .addAddress("127.0.0.1:20552")
        .build();

    ProtocolConfig protocolConfig = new ProtocolConfig();
    protocolConfig.setClusterType(ClusterType.FAIL_BACK);
    protocolConfig.setRecoveryPeriod(400);
    protocolConfig.setRetryTimes(1);

    reference0 = new Reference<>();
    reference0.setClientConfig(clientConfig);
    reference0.setProtocolConfig(protocolConfig);
    reference0.setInterfaceClass(AService.class);
    aService = reference0.refer();

    reference1 = new Reference<>();
    reference1.setClientConfig(clientConfig);
    reference1.setProtocolConfig(protocolConfig);
    reference1.setInterfaceClass(BService.class);
    bService = reference1.refer();
  }

  @AfterClass
  public static void destroy() {
    reference0.derefer();
    reference1.derefer();
    exporter0.unexport();
    exporter1.unexport();
  }

  @Test
  public void test0() {
    String a = "a";
    String b = "b";
    for(int i = 0; i < 100; i++) {
      String a0 = aService.echo(a);
      String b0 = bService.echo(b);
      Assert.assertEquals(a0, a);
      Assert.assertEquals(b0, b);
    }
  }

  @Test
  public void switchSourceTest() {
    String a = "a";
    String b = "b";


    int maxRound = 2000;
    int round = 0;

    while (round++ <= maxRound) {
      String a0 = aService.echo(a);
      String b0 = bService.echo(b);
      Assert.assertEquals(a0, a);
      Assert.assertEquals(b0, b);

      if(round == 100) {
        exporter0.unexport();
        logger.info("Shutdown export0...");
      }

      if(round == 300) {
        export0();
        logger.info("Reopen export0...");
      }

      if(round == 500) {
        exporter1.unexport();
        logger.info("Shutdown export1...");
      }

      if(round == 700) {
        export1();
        logger.info("Reopen export1...");
      }

      if(round == 900) {
        exporter0.unexport();
        logger.info("Shutdown export0...");
      }

      if(round == 1100) {
        export0();
        logger.info("Reopen export0...");
      }

      if(round == 1300) {
        exporter1.unexport();
        logger.info("Shutdown export1...");
      }

      if(round == 1500) {
        export1();
        logger.info("Reopen export1...");
      }
      sleep();
    }
  }

  private void export0() {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(20550)
        .build();
    exporter0 = new Exporter(serverConfig);
    exporter0.setProtocolConfig(providerProtocol);
    exporter0.registerService(AService.class, new AServiceImpl());
    exporter0.registerService(BService.class, new BServiceImpl());
    exporter0.export();
  }

  private void export1() {
    ServerConfig serverConfig = ServerConfig.builder()
        .port(20551)
        .build();
    exporter1 = new Exporter(serverConfig);
    exporter1.setProtocolConfig(providerProtocol);
    exporter1.registerService(AService.class, new AServiceImpl());
    exporter1.registerService(BService.class, new BServiceImpl());
    exporter1.export();
  }

  private static void sleep() {
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (Exception e) {
      // ignore
    }
  }
}
