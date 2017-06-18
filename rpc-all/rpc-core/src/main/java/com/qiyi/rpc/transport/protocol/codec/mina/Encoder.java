package com.qiyi.rpc.transport.protocol.codec.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiyi.rpc.transport.protocol.message.Message;

/**
 * Created by qiyi
 */
public class Encoder extends ProtocolEncoderAdapter {

	private Logger logger = LoggerFactory.getLogger(Encoder.class);

	private Charset charset;

	public Encoder(Charset charset) {
		this.charset = charset;
	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(1024).setAutoExpand(true);
		Message msg = (Message) message;

		/**
		 * 数据传输格式 buffer requestId:length 2字节 requestId:内容 length字节
		 * 
		 * className:length 2字节 className 内容 length字节
		 * 
		 * methodName length 2字节 mehtodName 内容 length字节
		 *
		 * args length 2字节 args 内容 length字节
		 *
		 */
		byte[] versionBytes = msg.getVersion().getBytes(this.charset);
		int versionLen = versionBytes.length;

		byte[] requestIdBytes = msg.getMessageId().getBytes(this.charset);
		int requestIdLength = requestIdBytes.length;

		int argsLength = msg.getBody().length;

		int totalLength = 2 + requestIdLength + 2 + 2 + 2 + versionLen + 2 + argsLength;
		/** 总长度 **/
		buffer.putUnsignedShort(totalLength);

		/** 请求id **/
		buffer.putUnsignedShort(requestIdLength);
		buffer.put(requestIdBytes);

		/** 方法编号 **/
		buffer.putUnsignedShort(msg.getMseq());

		/** service编号 **/
		buffer.putUnsignedShort(msg.getBseq());

		/** service version **/

		buffer.putUnsignedShort(versionLen);
		buffer.put(versionBytes);

		/** 参数或者结果数组 **/
		buffer.putUnsignedShort(argsLength);
		buffer.put(msg.getBody());

		buffer.flip();
		logger.debug("send limit:{},msg:{}", buffer.limit(), msg);

		/*
		 * byte[] b = msg.getBytes(charset); short length = (short)b.length;
		 * logger.debug("send length:{}",length);
		 */

		/*
		 * buffer.putShort(length); buffer.put(b);
		 * 
		 * buffer.flip();
		 */

		out.write(buffer);
	}
}
