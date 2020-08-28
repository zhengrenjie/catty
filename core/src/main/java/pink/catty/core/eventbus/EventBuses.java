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
package pink.catty.core.eventbus;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.catty.core.Constants;

public final class EventBuses {

  private static final Logger logger = LoggerFactory.getLogger(EventBuses.class);

  private static final int MAX_QUEUE_LENGTH = 64_000;
  private static final int ALIVE_TIME_LONG = 60 * 1000;
  private static final int ALIVE_TIME_SHORT = 1000;
  private static final Executor ONE_BY_ONE;
  private static final Executor WHAT_A_MESS;

  static {
    ONE_BY_ONE = new ThreadPoolExecutor(Constants.THREAD_NUMBER,
        Constants.THREAD_NUMBER * 4,
        ALIVE_TIME_LONG,
        TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(MAX_QUEUE_LENGTH),
        EventBusThreadFactory.getInstance(),
        EventBusRejectedExecutionHandler.getInstance()
    );

    WHAT_A_MESS = new ThreadPoolExecutor(Constants.THREAD_NUMBER * 2,
        Constants.THREAD_NUMBER * 16,
        ALIVE_TIME_SHORT,
        TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(MAX_QUEUE_LENGTH),
        EventBusThreadFactory.getInstance(),
        EventBusRejectedExecutionHandler.getInstance()
    );
  }

  public static EventBus SYNC = new EventBus() {
    @Override
    public void post(Object event) {
      subscribers.getOrDefault(event.getClass(), new CopyOnWriteArraySet<>())
          .forEach(subscriber -> subscriber.invoke(event));
    }
  };

  public static EventBus ASYNC_ONE_BY_ONE = new EventBus() {
    @Override
    public void post(Object event) {
      ONE_BY_ONE.execute(() ->
          subscribers.getOrDefault(event.getClass(), new CopyOnWriteArraySet<>())
              .forEach(subscriber -> subscriber.invoke(event)));
    }
  };

  public static EventBus ASYNC_SEIZE = new EventBus() {
    @Override
    public void post(Object event) {
      subscribers.getOrDefault(event.getClass(), new CopyOnWriteArraySet<>())
          .forEach(subscriber -> WHAT_A_MESS.execute(() -> subscriber.invoke(event)));
    }
  };

  private static class EventBusThreadFactory implements ThreadFactory {

    private static final EventBusThreadFactory INSTANCE = new EventBusThreadFactory();

    static EventBusThreadFactory getInstance() {
      return INSTANCE;
    }

    private EventBusThreadFactory() {
    }

    private final AtomicInteger id = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
      return new Thread(r, "eventbus-" + id.getAndIncrement());
    }
  }

  private static class EventBusRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final EventBusRejectedExecutionHandler INSTANCE = new EventBusRejectedExecutionHandler();

    static EventBusRejectedExecutionHandler getInstance() {
      return INSTANCE;
    }

    private EventBusRejectedExecutionHandler() {
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      logger.error("EventBus is exhausted!");
    }
  }

}
