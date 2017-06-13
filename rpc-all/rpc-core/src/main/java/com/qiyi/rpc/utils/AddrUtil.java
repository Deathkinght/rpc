package com.qiyi.rpc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddrUtil {

	public  static String getLocalAddr()
	{
		try {
			return  InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
