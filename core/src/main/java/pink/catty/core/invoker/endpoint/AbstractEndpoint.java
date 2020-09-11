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
package pink.catty.core.invoker.endpoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.catty.core.EndpointIllegalStateException;
import pink.catty.core.config.EndpointConfig;
import pink.catty.core.extension.ExtensionFactory;
import pink.catty.core.extension.spi.Codec;
import pink.catty.core.support.worker.HashableExecutor;
import pink.catty.core.support.worker.StandardThreadExecutor;

public abstract class AbstractEndpoint implements Endpoint {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected static final int NEW = 0;
  protected static final int CONNECTED = 1;
  protected static final int DISCONNECTED = 2;

  private final Codec codec;
  private AtomicInteger status;
  private ExecutorService executor;
  private final EndpointConfig config;

  public AbstractEndpoint(EndpointConfig config) {
    this.status = new AtomicInteger(NEW);
    this.config = config;
    this.codec = ExtensionFactory.codec().getExtension(config.getCodec());
    createExecutor();
  }

  @Override
  public Codec getCodec() {
    return codec;
  }

  @Override
  public boolean isAvailable() {
    return status.get() == CONNECTED;
  }

  @Override
  public boolean isClosed() {
    return status.get() == DISCONNECTED;
  }

  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

  private void createExecutor() {
    if (config.isUseWorkerThread()) {
      int minWorkerNum = config.getWorkerMinNum();
      int maxWorkerNum = config.getWorkerMaxNum();
      executor = new StandardThreadExecutor(minWorkerNum, maxWorkerNum);
      ((StandardThreadExecutor) executor).prestartAllCoreThreads();
    }
  }

  @Override
  public void open() {
    if (status.compareAndSet(NEW, CONNECTED)) {
      doOpen();
      logger.info("Opened an endpoint, {}", toString());
    } else {
      throw new EndpointIllegalStateException(
          "Endpoint's status is illegal, status: " + status + " config: " + toString());
    }
  }

  @Override
  public void close() {
    if (status.get() == DISCONNECTED) {
      return;
    }
    if (status.compareAndSet(CONNECTED, DISCONNECTED)) {
      if (executor instanceof HashableExecutor) {
        ((HashableExecutor) executor).shutdownGracefully();
      } else {
        executor.shutdown();
      }
      doClose();
      logger.info("Closed an endpoint, {}", toString());
    } else {
      throw new EndpointIllegalStateException(
          "Endpoint's status is illegal, status: " + status + " config: " + toString());
    }
  }

  protected abstract void doOpen();

  protected abstract void doClose();

}
