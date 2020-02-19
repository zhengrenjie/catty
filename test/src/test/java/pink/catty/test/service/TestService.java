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
package pink.catty.test.service;

import java.util.concurrent.CompletableFuture;

public interface TestService {

  String echo(String name);

  CompletableFuture<String> asyncEcho(String name);

  String checkedException() throws Test1CheckedException;

  String multiCheckedException() throws Test1CheckedException, Test2CheckedException;

  /**
   * @throws NullPointerException
   */
  String runtimeException();

  /**
   * Completed with {@link Exception}
   */
  CompletableFuture<String> asyncException0(String name);

  /**
   * Completed with {@link Error}
   */
  CompletableFuture<String> asyncException1(String name);

  /**
   * Completed with {@link RuntimeException}
   */
  CompletableFuture<String> asyncException2(String name);
}
