package kr.co.bizframe.boot.nio.socket.handler;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import kr.co.bizframe.boot.nio.model.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {

  public static final AttributeKey<String> ATTR_KEY_DEVICEID = AttributeKey.newInstance("deviceId");

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    String deviceId = ctx.channel().attr(ATTR_KEY_DEVICEID).get();
    log.debug("connection opened", (deviceId == null ? "" : ", " + deviceId));
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    String deviceId = ctx.channel().attr(ATTR_KEY_DEVICEID).get();
    log.debug("connection closed", (deviceId == null ? "" : ", " + deviceId));
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
    String deviceId = ctx.channel().attr(ATTR_KEY_DEVICEID).get().trim();
    // logger.debug("=== receive");

    Map<String, String> headers = new HashMap<String, String>();
    headers.put(MessageHeaders.PACKET_TYPE.name(), PacketType.Ack.name());

    Message response = Message.builder().deviceId("Server").body("result").header(headers).build();

    send(ctx, response);
  }

  private synchronized void send(ChannelHandlerContext ctx, Message data) throws Exception {
    if (!ctx.channel().isWritable()) {
      throw new Exception("데이터 전송 불가 상태입니다.");
    }

    try {
      Gson gson = new Gson();
      ctx.writeAndFlush(gson.toJson(data));
    } finally {

    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.close();
  }

  public enum MessageHeaders {
    PACKET_TYPE, SEND_DATE
  }

  public enum PacketType {
    Data, DeviceResources, Script, ModuleUpdate, Ack
  }

}
