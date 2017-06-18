package com.qiyi.rpc.transport.server.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.transport.protocol.context.server.ServerBeanContext;
import com.qiyi.rpc.transport.protocol.message.Message;

/**
 * 
 * @author qiyi 服务端分发
 *
 */
public class MinaDispatcherHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		session.write(invoke(message));
	}

	/**
	 * 执行获取结果
	 * 
	 * @param obj
	 * @return
	 */
	/*
	 * public ResponseMessage<Object> invoke2(Object obj) { String requestString
	 * = (String) obj;
	 * 
	 * RequestMessage requestMessage = JsonUtil.jsonStringToObject(
	 * RequestMessage.class, requestString);
	 * 
	 * Object serviceBean =
	 * QiyiBeanContext.getBean(requestMessage.getClassName());
	 * 
	 * Method method =
	 * QiyiBeanContext.getMethod(requestMessage.getMethodName());
	 * 
	 * Object invokeResult; try { invokeResult = method.invoke(serviceBean,
	 * requestMessage.getArgs()); return new
	 * ResponseMessage<Object>(requestMessage.getRequestId(), invokeResult, 1);
	 * } catch (IllegalAccessException e) { e.printStackTrace(); } catch
	 * (IllegalArgumentException e) { e.printStackTrace(); } catch
	 * (InvocationTargetException e) { e.printStackTrace(); }
	 * 
	 * return new ResponseMessage<Object>(requestMessage.getRequestId(), null,
	 * -1); }
	 */

	/**
	 * 执行获取结果
	 * 
	 * @param obj
	 * @return
	 */
	public Message invoke(Object obj) {
		Message requestMsg = (Message) obj;

		Object[] args = JSON.parseObject(requestMsg.getBody(),Object[].class);

		System.out.println(JSON.toJSONString(requestMsg));
		System.out.println("调用:method:" + requestMsg.getMseq() + ",messageId:" + requestMsg.getMessageId() + "args:" + JSON.toJSONString(args));
		Object invokeResult = ServerBeanContext.invokeMethod(requestMsg.getVersion(), requestMsg.getBseq(), requestMsg.getMseq(), args);
		return new Message(requestMsg.getMessageId(), requestMsg.getMseq(), requestMsg.getBseq(), requestMsg.getVersion(), JSON.toJSONBytes(invokeResult));

	}

}
