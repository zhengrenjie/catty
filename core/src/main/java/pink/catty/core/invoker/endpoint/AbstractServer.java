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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import pink.catty.core.CattyException;
import pink.catty.core.config.ProviderConfig;
import pink.catty.core.invoker.Provider;
import pink.catty.core.invoker.frame.DefaultRequest;
import pink.catty.core.invoker.frame.DefaultResponse;
import pink.catty.core.invoker.frame.Request;
import pink.catty.core.invoker.frame.Response;
import pink.catty.core.model.MethodModel;
import pink.catty.core.model.ServiceModel;

public abstract class AbstractServer extends AbstractEndpoint implements Server {

  private final Map<String, Provider> invokerMap = new ConcurrentHashMap<>();
  private final ProviderConfig config;

  public AbstractServer(ProviderConfig config) {
    super(config);
    this.config = config;
  }

  @Override
  public void registerInvoker(String serviceIdentify, Provider provider) {
    invokerMap.put(serviceIdentify, provider);
  }

  @Override
  public Provider unregisterInvoker(String serviceIdentify) {
    return invokerMap.remove(serviceIdentify);
  }

  @Override
  public Provider getInvoker(String invokerIdentify) {
    return invokerMap.get(invokerIdentify);
  }

  @Override
  public ProviderConfig config() {
    return config;
  }

  @Override
  public Response invoke(Request request) {
    String serviceName = request.getInterfaceName();
    Provider provider = getInvoker(serviceName);
    if (provider == null) {
      throw new CattyException(
          "No such provider found! RpcService name: " + request.getInterfaceName());
    }

    ServiceModel serviceModel = ServiceModel.Of(provider.config().getInterfaceName());
    MethodModel methodModel = serviceModel.getMethodMetaByName(request.getMethodName());

    if (methodModel == null) {
      Response response = new DefaultResponse(request.getRequestId());
      response.setValue(
          new CattyException("ServiceInvoker: can't find method: " + request.getMethodName()));
      return response;
    }

    return provider.invoke(new DefaultRequest(request.getRequestId(),
        request.getInterfaceName(),
        request.getMethodName(),
        request.getArgsValue(),
        serviceModel,
        methodModel,
        serviceModel.getTarget()
    ));
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
    AbstractServer that = (AbstractServer) o;
    return Objects.equals(config, that.config);
  }

  @Override
  public int hashCode() {
    return Objects.hash(config);
  }
}
