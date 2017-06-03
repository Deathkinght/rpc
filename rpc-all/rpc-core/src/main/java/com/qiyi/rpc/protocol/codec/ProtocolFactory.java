package com.qiyi.rpc.protocol.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

/**
 * Created by qiyi 
 */
public class ProtocolFactory implements ProtocolCodecFactory{

    private Decoder decoder;

    private Encoder encoder;

    public ProtocolFactory() {
        this(Charset.defaultCharset());
    }

    public ProtocolFactory(Charset charset) {
        this.decoder = new Decoder(charset);
        this.encoder = new Encoder(charset);
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
