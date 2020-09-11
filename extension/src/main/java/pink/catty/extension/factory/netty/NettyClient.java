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
package pink.catty.extension.factory.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import pink.catty.core.Constants;
import pink.catty.core.EndpointInvalidException;
import pink.catty.core.config.ConsumerConfig;
import pink.catty.core.extension.spi.Codec.DataTypeEnum;
import pink.catty.core.invoker.endpoint.AbstractClient;
import pink.catty.core.invoker.frame.DefaultResponse;
import pink.catty.core.invoker.frame.Request;
import pink.catty.core.invoker.frame.Response;
import pink.catty.core.model.MethodModel;

public class NettyClient extends AbstractClient {

  private Channel clientChannel;
  private NioEventLoopGroup nioEventLoopGroup;

  public NettyClient(ConsumerConfig config, InetSocketAddress remote) {
    super(config, remote);
    nioEventLoopGroup = new NioEventLoopGroup(Constants.THREAD_NUMBER + 1);
  }

  @Override
  protected void doOpen() {
    Bootstrap bootstrap = new Bootstrap();
    int connectTimeoutMillis = config().getConnectTimeout() > 0 ? config().getConnectTimeout()
        : Constants.DEFAULT_CLIENT_TIMEOUT;
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMillis);
    bootstrap.option(ChannelOption.TCP_NODELAY, true);
    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    bootstrap.group(nioEventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new NettyDecoder(getCodec()));
            pipeline.addLast("handler", new ClientChannelHandler(NettyClient.this));
          }
        });
    ChannelFuture future;
    try {
      future = bootstrap
          .connect(remoteAddress().getAddress(), remoteAddress().getPort())
          .sync();
      clientChannel = future.channel();
    } catch (InterruptedException i) {
      close();
      throw new EndpointInvalidException("NettyClient: connect().sync() interrupted", i);
    }
  }

  @Override
  protected void doClose() {
    if (clientChannel != null && clientChannel.isActive()) {
      clientChannel.close();
    }
    if (nioEventLoopGroup != null && !nioEventLoopGroup.isShutdown()) {
      nioEventLoopGroup.shutdownGracefully();
    }
  }

  @Override
  public InetSocketAddress localAddress() {
    return (InetSocketAddress) clientChannel.localAddress();
  }

  @Override
  public Response invoke(Request request) {
    if (!clientChannel.isActive()) {
      throw new EndpointInvalidException("ClientChannel closed");
    }
    MethodModel methodModel = request.getInvokedMethod();
    try {
      Response response = new DefaultResponse(request.getRequestId());

      /*
       * if the invoking method is not need return from provider, than should not listen this
       * response, or will cause OOM.
       */
      if (methodModel.isNeedReturn() || methodModel.getReturnType() != Void.TYPE) {
        addCurrentTask(request.getRequestId(), response);
      }
      byte[] msg = getCodec().encode(request, DataTypeEnum.REQUEST);
      ByteBuf byteBuf = clientChannel.alloc().heapBuffer();
      byteBuf.writeBytes(msg);
      clientChannel.writeAndFlush(byteBuf).addListener(future -> {
        if (!future.isSuccess()) {
          if (future.cause() != null) {
            logger.error("Client send request failed. ", future.cause());
          } else {
            logger.error("Client send request failed without cause. ");
          }
        }
      });
      return response;
    } catch (Exception e) {
      logger.error("ClientChannel invoke error", e);

      /*
       * Auto close.
       */
      close();

      throw new EndpointInvalidException("ClientChannel invoke error", e);
    }
  }
}
