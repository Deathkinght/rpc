package com.qiyi.rpc.registry.eureka;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import com.qiyi.rpc.transport.protocol.context.Callable;
import com.qiyi.rpc.transport.server.Acceptor;

public class EurekaUtil {

	private static final Logger logger = LoggerFactory.getLogger(EurekaUtil.class);

	private static DynamicPropertyFactory configInstance = com.netflix.config.DynamicPropertyFactory.getInstance();
	
	private static ApplicationInfoManager serverApplicationInfoManager;
	private static EurekaClient serverEurekaClient;

	private static ApplicationInfoManager clientApplicationInfoManager;
	private static EurekaClient clientEurekaClient;

	private static synchronized ApplicationInfoManager initializeProviderApplicationInfoManager(
			EurekaInstanceConfig instanceConfig) {
		if (serverApplicationInfoManager == null) {
			InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
			serverApplicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
		}

		return serverApplicationInfoManager;
	}

	private static synchronized ApplicationInfoManager initializeConsumerApplicationInfoManager(
			EurekaInstanceConfig instanceConfig) {
		if (clientApplicationInfoManager == null) {
			InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
			clientApplicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);
		}

		return clientApplicationInfoManager;
	}

	private static synchronized EurekaClient initializeConsumerEurekaClient(
			ApplicationInfoManager applicationInfoManager, EurekaClientConfig clientConfig, Callable call) {
		if (clientEurekaClient == null) {
			DiscoveryClientOptionalArgs args = null;
			if (call != null) {
				args = new DiscoveryClientOptionalArgs();
				Set<EurekaEventListener> listeners = new HashSet<>();
				listeners.add(new EurekaEventListener() {

					@Override
					public void onEvent(EurekaEvent event) {
						// TODO Auto-generated method stub
						call.call();
					}
				});
				args.setEventListeners(listeners);
			}

			clientEurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig, args);
		}

		return clientEurekaClient;
	}

	private static synchronized EurekaClient initializeProviderEurekaClient(
			ApplicationInfoManager applicationInfoManager, EurekaClientConfig clientConfig) {
		if (serverEurekaClient == null) {
			serverEurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
		}

		return serverEurekaClient;
	}

	public static void serverStart(String spaceName) {

		System.setProperty("provider.name",System.getProperty("provider.name")+"-provider");
		System.setProperty("provider.vipAddress",System.getProperty("provider.vipAddress")+"-provider");
		
		
		
		ApplicationInfoManager applicationInfoManager = initializeProviderApplicationInfoManager(
				new MyDataCenterInstanceConfig());
		initializeProviderEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());

		ExampleServiceBase exampleServiceBase = new ExampleServiceBase(applicationInfoManager, serverEurekaClient,
				configInstance);
		
		exampleServiceBase.start();
//		try {
//			exampleServiceBase.start();
//		} finally {
//			// the stop calls shutdown on eurekaClient
//			exampleServiceBase.stop();
//		}

	}

	public static void clientStart(String spaceName, Callable call) {
       
		
		System.setProperty("consumer.name",System.getProperty("consumer.name")+"-provider");
		System.setProperty("consumer.vipAddress",System.getProperty("consumer.vipAddress")+"-provider");
		
		
		ApplicationInfoManager applicationInfoManager = initializeConsumerApplicationInfoManager(
				new MyDataCenterInstanceConfig(spaceName));
		initializeConsumerEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig(spaceName),
				call);
		 getServiceUsingEureka("orderCenter2",clientEurekaClient);

	}

	protected static void getServiceUsingEureka(String vipAddress, EurekaClient eurekaClient) {

		List<InstanceInfo> instances =  eurekaClient.getInstancesByVipAddress(vipAddress, false);
		
		for(InstanceInfo ins : instances){
			Map<String,String> metaData = ins.getMetadata();
			//String metaData.get("");
		}


	}

	public static void start()
	{
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				serverStart("provider.");
			}
		}).start();

		new Thread(new Runnable() {

		@Override
			public void run() {
				clientStart("consumer.", new Callable() {

					@Override
					public void call() {
						// TODO Auto-generated method stub
					System.out.println(123);
				}
			});
			}
	}).start();
	}
	
	public static void main(String[] args) {

		//serverStart("eureka.");
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				serverStart("eureka.");
			}
		}).start();

		new Thread(new Runnable() {

		@Override
			public void run() {
				clientStart("eureka2.", new Callable() {

					@Override
					public void call() {
						// TODO Auto-generated method stub
					System.out.println(123);
				}
			});
			}
	}).start();

	}

}

class ExampleServiceBase {

	private  ApplicationInfoManager applicationInfoManager;
	private  EurekaClient eurekaClient;
	private  DynamicPropertyFactory configInstance;

	private static final Logger logger = LoggerFactory.getLogger(ExampleServiceBase.class);

	@Inject
	protected ExampleServiceBase(ApplicationInfoManager applicationInfoManager, EurekaClient eurekaClient,
			DynamicPropertyFactory configInstance) {
		this.applicationInfoManager = applicationInfoManager;
		this.eurekaClient = eurekaClient;
		this.configInstance = configInstance;
	}

	@PostConstruct
	protected void start() {
		// A good practice is to register as STARTING and only change status to
		// UP
		// after the service is ready to receive traffic
		logger.info("Registering service to eureka with STARTING status");
		applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.STARTING);

		logger.info("Simulating service initialization by sleeping for 2 seconds...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// Nothing
		}

		// Now we change our status to UP
		logger.info("Done sleeping, now changing status to UP");
		applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
		// applicationInfoManager.getInfo().getMetadata().put("testT","asdasdqiyi");
		applicationInfoManager.refreshDataCenterInfoIfRequired();
		waitForRegistrationWithEureka(eurekaClient);
		logger.info("Service started and ready to process requests..");
		Acceptor.acceptor();

	}

	@PreDestroy
	protected void stop() {
		if (eurekaClient != null) {
			logger.info("Shutting down server. Demo over.");
			eurekaClient.shutdown();
		}
	}

	private void waitForRegistrationWithEureka(EurekaClient eurekaClient) {
		// my vip address to listen on
		String vipAddress = configInstance.getStringProperty("eureka.vipAddress", "sampleservice.mydomain.net").get();
		InstanceInfo nextServerInfo = null;
		while (nextServerInfo == null) {
			try {
				nextServerInfo = eurekaClient.getNextServerFromEureka(vipAddress, false);
			} catch (Throwable e) {
				logger.info("Waiting ... verifying service registration with eureka ...{}",applicationInfoManager.getInfo().getVIPAddress());

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// protected void getServiceUsingEureka(String vipAddress,EurekaClient
	// eurekaClient) {
	// // initialize the client
	// // this is the vip address for the example service to talk to as defined
	// in conf/sample-eureka-service.properties
	// //eurekaClient.get
	// InstanceInfo nextServerInfo = null;
	// try {
	// eurekaClient.getApplication("").getInstances();
	//
	// nextServerInfo = eurekaClient.getNextServerFromEureka(vipAddress, false);
	// } catch (Exception e) {
	// System.err.println("Cannot get an instance of example service to talk to
	// from eureka");
	// // System.exit(-1);
	// }
	//
	//
	// logger.info("Found an instance of example service to talk to from eureka:
	// "
	// + nextServerInfo.getVIPAddress() + ":" + nextServerInfo.getPort());
	//
	// logger.info("healthCheckUrl: " + nextServerInfo.getHealthCheckUrl());
	// logger.info("override: " + nextServerInfo.getOverriddenStatus());
	//
	// }
}
