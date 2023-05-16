package kr.co.bizframe.boot.nio.socket.decoder;

import java.io.UnsupportedEncodingException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MDecoder extends ByteToMessageDecoder {

  private static int MAX_LENGTH = 1000000;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    int dataLength = in.readInt();

    if (dataLength > MAX_LENGTH) {
      throw new RuntimeException("data is too large size=[" + dataLength + "]");
    }
    if (in.readableBytes() < dataLength) {
      in.resetReaderIndex();
      return;
    }

    // Convert the received data into a new BigInteger.
    byte[] decoded = new byte[dataLength];
    in.readBytes(decoded);

    try {
      // TODO 클라리언트 소켓 데이터는 모두 utf-8인코딩 처리
      out.add(new String(decoded, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("date decode error, utf-8 " + e.getMessage(), e);
    }

  }

}
