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
package pink.catty.core.invoker.frame;

import pink.catty.core.Attribute;
import pink.catty.core.model.MethodModel;
import pink.catty.core.model.ServiceModel;

public interface Request extends Attribute {

  long getRequestId();

  void setRequestId(long requestId);

  String getInterfaceName();

  void setInterfaceName(String interfaceName);

  String getMethodName();

  void setMethodName(String methodName);

  Object[] getArgsValue();

  void setArgsValue(Object[] argsValue);

  Object getTarget();

  MethodModel getInvokedMethod();

  ServiceModel getServiceModel();

}
