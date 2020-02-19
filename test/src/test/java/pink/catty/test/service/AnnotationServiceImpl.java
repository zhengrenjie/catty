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

import pink.catty.core.utils.MD5Utils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AnnotationServiceImpl implements AnnotationService {

  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Override
  public String timeout0(String name) {
    return name;
  }

  @Override
  public CompletableFuture<String> asyncTimeout0(String name) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executorService.submit(() -> future.complete(name));
    return future;
  }

  @Override
  public String timeout1(String name) {
    try {
      TimeUnit.MILLISECONDS.sleep(300);
    } catch (Exception e) {
      // ignore
    }
    return name;
  }

  @Override
  public CompletableFuture<String> asyncTimeout1(String name) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executorService.submit(() -> {
      try {
        TimeUnit.MILLISECONDS.sleep(300);
        future.complete(name);
      } catch (Exception e) {
        future.complete(MD5Utils.md5(name));
      }
    });
    return future;
  }

}
