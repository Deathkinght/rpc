package com.qiyi.rpc.spring.schema;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.qiyi.rpc.constant.Constant;
import com.qiyi.rpc.registry.zookeeper.ZkRegistry;
import com.qiyi.rpc.spring.QpcApplicationListener;
import com.qiyi.rpc.spring.bean.ClientFactoryBean;
import com.qiyi.rpc.spring.bean.ConfigFactoryBean;
import com.qiyi.rpc.spring.bean.ServerFactoryBean;
import com.qiyi.rpc.transport.protocol.context.client.ClientBeanContext;
import com.qiyi.rpc.transport.protocol.context.server.ServerBeanContext;


/**
 * 解析xml
 * @author qiyi
 *
 */
public class QpcBeanParser extends AbstractSimpleBeanDefinitionParser {

	private Logger logger = LoggerFactory.getLogger(QpcBeanParser.class);
	
	private Class<?> cla;
	
	public QpcBeanParser(Class<?> cla)
	{
		this.cla = cla;
	}
	
	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		// 从标签中取出对应的属性值
		
		String group = element.getAttribute("group");
		logger.debug("get group:{} from xml", group);
		builder.addPropertyValue("group", group);
		
		String id = element.getAttribute("id");
		logger.debug("get id:{} from xml", id);
		
		String version = element.getAttribute("version");
		logger.debug("get version:{} from xml", version);
		
		String interfaceName = element.getAttribute("interfaceName");
		logger.debug("get interfaceName:{} from xml", interfaceName);
		
		/** 服务端**/
		if (ServerFactoryBean.class.equals(cla)) {
			String ref = element.getAttribute("refer");
			logger.debug("get ref:{} from xml", ref);
			if (StringUtils.isBlank(ref)) {
				// TODO
				logger.error("ref canot be null");
				return;
			}
			
			RuntimeBeanReference refer = new RuntimeBeanReference(ref);
			
			version = StringUtils.isBlank(version)?Constant.DEFAULT_VERSION:version;
			
			//TODO
			ServerBeanContext.pubVersionBean(version, interfaceName,id);
			
			//QiyiApplicationListener.addBeanName(id);
			builder.addPropertyValue("refer", refer);
			
			builder.addPropertyValue("interfaceName", interfaceName);
			
			builder.addPropertyValue("version", version);
		}else if(ConfigFactoryBean.class.equals(cla))
		{/**配置**/
			
			String address = element.getAttribute("address");
			logger.debug("get zkIp:{} from xml", address);
			
			String sessionTimeoutMsStr = element.getAttribute("sessionTimeoutMs");
			logger.debug("get sessionTimeoutMs:{} from xml",sessionTimeoutMsStr);
			
			String retrySleepTimeMsStr = element.getAttribute("retrySleepTimeMs");
			logger.debug("get retrySleepTimeMs:{} from xml",retrySleepTimeMsStr);
			
			String retryMaxRetriesStr = element.getAttribute("retryMaxRetries");
			logger.debug("get retryMaxRetries:{} from xml",retryMaxRetriesStr);
			
			setConfig(address, sessionTimeoutMsStr, retrySleepTimeMsStr, retryMaxRetriesStr);
			
			/**手动注册listener**/
			parserContext.getRegistry().registerBeanDefinition("qiyiApplicationListener", new AnnotatedGenericBeanDefinition(QpcApplicationListener.class));
			
			
		}else 	if (ClientFactoryBean.class.equals(cla)) {
			/**客户端**/
			
			builder.addPropertyValue("interfaceName", interfaceName);
			
			String checkStr = element.getAttribute("check");
			logger.debug("get check:{} from xml",checkStr);
			boolean check = StringUtils.isBlank(checkStr)?false:Boolean.parseBoolean(checkStr);
			
			builder.addPropertyValue("check", check);
			
			version = StringUtils.isBlank(version)?Constant.DEFAULT_VERSION:version;
			builder.addPropertyValue("version", version);
			
			try {

				Object proxy = ClientBeanContext.initBeanProxy(version, interfaceName,check);
				builder.addPropertyValue("proxy", proxy);
				
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}

		//super.doParse(element, parserContext, builder);
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return cla;
	}

	
	private void setConfig(String address,String sessionTimeoutMsStr,String retrySleepTimeMsStr,String retryMaxRetriesStr)
	{
		
		if(!ZkRegistry.getIsInit())
		{

			if(StringUtils.isNotBlank(sessionTimeoutMsStr))
			{
				ZkRegistry.setSessionTimeoutMs(Integer.parseInt(sessionTimeoutMsStr));
			}
			
			if(StringUtils.isNotBlank(retrySleepTimeMsStr))
			{

				ZkRegistry.setRetrySleepTimeMs(Integer.parseInt(retrySleepTimeMsStr));
			}
			
			if(StringUtils.isNotBlank(retryMaxRetriesStr))
			{
				ZkRegistry.setRetrySleepTimeMs(Integer.parseInt(retryMaxRetriesStr));
			}
			
			
			ZkRegistry.setZkAddress(address);
			
		}
		
	}
	
}
