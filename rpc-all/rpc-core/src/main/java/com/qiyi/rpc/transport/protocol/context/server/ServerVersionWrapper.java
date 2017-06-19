package com.qiyi.rpc.transport.protocol.context.server;

import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;
import com.qiyi.rpc.utils.Function;

public class ServerVersionWrapper {

	private String version;

	private Map<Integer, ServerBeanWrapper> context = new HashMap<>();
	
	private Map<Integer,BeanNodeWrapperDto> nodeWrapMap = new HashMap<>();

	public void pushBeanNodeDatas(Function<BeanNodeWrapperDto, Object> fun) throws Exception
	{
		for(Integer seq:context.keySet())
		{
			BeanNodeWrapperDto nodeWrap = nodeWrapMap.get(seq);
			fun.apply(nodeWrap);
		}
//		context.forEach((seq,beanWrap)->{
//			
//		});
	}
	
	public String getVersion() {
		return version;
	}
	
	public Object invokeMethod(Integer beanSeq,Integer methodId,Object[] args)
	{
		ServerBeanWrapper wrap = context.get(beanSeq);
		if(wrap == null){
			//TODO
		}
		return wrap.invokeMethod(methodId, args);
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	public ServerVersionWrapper(String version) {
		super();
		this.version = version;
	}


	public void putBean(Integer beanSeq,ServerBeanWrapper wrapper)
	{
		BeanNodeWrapperDto nodeWrap = new BeanNodeWrapperDto(wrapper.getInterfaceName());
		wrapper.setBeanNodeWrap(nodeWrap);
		context.put(beanSeq, wrapper);
		nodeWrapMap.put(beanSeq,nodeWrap);
	}
	
//	public ServerBeanWrapper initVersionBean(Integer beanSeq, String interfaceName, Object bean) {
//
//		ServerBeanWrapper wrap = context.get(beanSeq);
//
//		if (wrap == null) {
//			
//			BeanNodeWrapperDto nodeWrap = new BeanNodeWrapperDto(interfaceName);
//			wrap = new ServerBeanWrapper(beanSeq,this.version,interfaceName, bean,nodeWrap);
//			context.put(beanSeq, wrap);
//			
//			nodeWrapMap.put(beanSeq,nodeWrap);
//		}
//
//		return wrap;
//	}

}
