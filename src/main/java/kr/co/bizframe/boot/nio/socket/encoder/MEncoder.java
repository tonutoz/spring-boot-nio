package kr.co.bizframe.boot.nio.socket.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MEncoder extends MessageToByteEncoder<String> {

  @Override
  protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
    byte[] data = msg.getBytes();
    int dataLength = data.length;

    // Write a message.
    out.writeInt(dataLength); // data length
    out.writeBytes(data); // data
  }

}
