package com.citizensrx.sockets;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GWTProxy {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		try{
			ExecutorService service = Executors.newFixedThreadPool(2);
			String[] args1 = new String[1];
			String[] args2 = new String[1];
			args1[0] = args[0];
			args2[0] = args[1];
			TCPForwardServer http = new TCPForwardServer();
			http.config(args[0]);
			TCPForwardServer code = new TCPForwardServer();
			code.config(args[1]);
			service.execute(http);
			service.execute(code);
			service.shutdown();
		}catch(Exception e){
			System.out.println("Exception: "+e.getMessage());
			GWTProxy.showUsage();
			e.printStackTrace();
		}
	}

	private static void showUsage() {
		System.out.println("Usage: [httpconfig] [codesrvconfig]");
	}

}
