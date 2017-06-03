package com.qiyi.rpc.protocol.context.server;

import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.protocol.context.BeanNodeWrapperDto;
import com.qiyi.rpc.utils.Function;

public class VersionWrapper {

	private String version;

	private Map<Integer, BeanWrapper> context = new HashMap<>();
	
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
		BeanWrapper wrap = context.get(beanSeq);
		if(wrap == null){
			//TODO
		}
		return wrap.invokeMethod(methodId, args);
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	public VersionWrapper(String version) {
		super();
		this.version = version;
	}

	public BeanWrapper initVersionBean(Integer beanSeq, String interfaceName, Object bean) {

		BeanWrapper wrap = context.get(beanSeq);

		if (wrap == null) {
			
			BeanNodeWrapperDto nodeWrap = new BeanNodeWrapperDto(interfaceName);
			wrap = new BeanWrapper(beanSeq, interfaceName, bean,nodeWrap);
			context.put(beanSeq, wrap);
			
			nodeWrapMap.put(beanSeq,nodeWrap);
		}

		return wrap;
	}

}
