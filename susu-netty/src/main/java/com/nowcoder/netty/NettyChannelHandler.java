//package com.nowcoder.netty;
//
//import com.nowcoder.codec.Codec;
//import com.nowcoder.exception.SusuException;
//import com.nowcoder.remote.Request;
//import com.nowcoder.remote.Response;
//import com.nowcoder.transport.Handler;
//import io.netty.channel.ChannelDuplexHandler;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandlerContext;
//
///**
// * @author zrj CreateDate: 2019/9/5
// */
//public class NettyChannelHandler extends ChannelDuplexHandler {
//
//  private Codec codec;
//
//  private Handler handler;
//
//  public NettyChannelHandler(Codec codec, Handler handler) {
//    this.codec = codec;
//    this.handler = handler;
//  }
//
//  @Override
//  public void channelRead(ChannelHandlerContext ctx, Object msg) {
//    Object object = codec.decode((byte[]) msg);
//    if (!(object instanceof Request) && !(object instanceof Response)) {
//      throw new SusuException("NettyChannelHandler: unsupported message type when encode: " + object.getClass());
//    }
//    if (object instanceof Request) {
//      processRequest(ctx, (Request) object);
//    } else {
//      processResponse(ctx, (Response) object);
//    }
//  }
//
//  private void processRequest(ChannelHandlerContext ctx, Request msg) {
//    Object result = handler.handle(msg);
//    Response response = new Response();
//    response.setRequestId(msg.getRequestId());
//
//    if(result instanceof Exception) {
//      response.setException((Exception) result);
//    } else {
//      response.setReturnValue(result);
//    }
//    sendResponse(ctx, response);
//  }
//
//  private void processResponse(ChannelHandlerContext ctx, Response msg) {
//    handler.handle(msg);
//  }
//
//  private ChannelFuture sendResponse(ChannelHandlerContext ctx, Response response) {
//    byte[] msg = codec.encode(response);
//    if (ctx.channel().isActive()) {
//      return ctx.channel().writeAndFlush(msg);
//    }
//
//    return null;
//  }
//
//
//}