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
package pink.catty.core.invoker.endpoint;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import pink.catty.core.config.ConsumerConfig;
import pink.catty.core.invoker.frame.Response;

public abstract class AbstractClient extends AbstractEndpoint implements Client {

  private final ConsumerConfig config;
  private final Map<Long, Response> currentTask = new ConcurrentHashMap<>();
  private final InetSocketAddress remote;

  public AbstractClient(ConsumerConfig config, InetSocketAddress remote) {
    super(config);
    this.config = config;
    this.remote = remote;
  }

  public Response getResponseFuture(long requestId) {
    return currentTask.remove(requestId);
  }

  public void addCurrentTask(long requestId, Response response) {
    currentTask.putIfAbsent(requestId, response);
  }

  @Override
  public ConsumerConfig config() {
    return config;
  }

  @Override
  public InetSocketAddress remoteAddress() {
    return remote;
  }

  protected abstract void doOpen();

  protected abstract void doClose();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractClient that = (AbstractClient) o;
    return Objects.equals(config, that.config);
  }

  @Override
  public int hashCode() {
    return Objects.hash(config);
  }
}
