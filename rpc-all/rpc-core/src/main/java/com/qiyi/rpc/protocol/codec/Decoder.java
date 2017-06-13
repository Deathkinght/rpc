package com.qiyi.rpc.protocol.codec;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiyi.rpc.protocol.message.Message;

/**
 * Created by qiyi
 */
public class Decoder extends CumulativeProtocolDecoder{

    private Charset charset ;

    private Logger logger = LoggerFactory.getLogger(Decoder.class);

    public Decoder(Charset charset) {
        this.charset = charset;
    }


    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        logger.debug("remaining:{}",in.remaining());

        if(in.remaining()> 2)
        {
            in.mark();

            int length = in.getUnsignedShort();
            logger.debug("message received legnth:{}",length);
            if(in.remaining()>=length)
            {
               // byte[] body = new byte[length];
                //in.get(body);
                Message message = decode(in);
                //String str = new String(body,charset);
                out.write(message);
                if(in.remaining()>0)
                {
                    logger.debug("has remaining");
                    return  true;
                }
                return false;
            }else {
                in.reset();
                return false;
            }

        }

        return false;
    }
    
    
    private Message decode(IoBuffer buffer) throws UnsupportedEncodingException
    {
    	
    	 Message message = new Message();
    	
    	 /**请求id**/
		 int requestIdLength = buffer.getUnsignedShort();
		 byte[] requestIdBytes = new byte[requestIdLength];
		 buffer.get(requestIdBytes);
		 String requestId = new String(requestIdBytes,charset);
		 
		 /**方法编号**/
		 int methodSeq = buffer.getUnsignedShort();
		 
		 /**服务编号**/
		 int  beanSeq = buffer.getUnsignedShort();
		 
		 int versionLen = buffer.getUnsignedShort();
		 byte[] versinBytes = new byte[versionLen];
		 buffer.get(versinBytes);
		 
		 /**参数或者结果数组**/
		 int bodyLength = buffer.getUnsignedShort();
		 byte[] body = new byte[bodyLength];
		 buffer.get(body);
		 
		// buffer.free();
		 
		 
		 message.setMessageId(requestId);
		 message.setMseq(methodSeq);
		 message.setBseq(beanSeq);
		 message.setVersion(new String(versinBytes,charset));
		 message.setBody(body);
		 return message;
		 
    }
}
