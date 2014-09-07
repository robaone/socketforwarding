package com.citizensrx.sockets;
/** 
 * This program is an example from the book "Internet 
 * programming with Java" by Svetlin Nakov. It is freeware. 
 * For more information: http://www.nakov.com/books/inetjava/ 
 */ 
import java.io.*; 
import java.net.*; 
import java.util.Properties;

/** 
 * TCPForwardServer is a simple TCP bridging software that 
 * allows a TCP port on some host to be transparently forwarded 
 * to some other TCP port on some other host. TCPForwardServer 
 * continuously accepts client connections on the listening TCP 
 * port (source port) and starts a thread (ClientThread) that 
 * connects to the destination host and starts forwarding the 
 * data between the client socket and destination socket. 
 */ 
public class TCPForwardServer implements Runnable { 
	private int SOURCE_PORT = 0; 
	private String DESTINATION_HOST = null; 
	private int DESTINATION_PORT = 0;
	private Properties props;
	private String outputFile;
	private String inputFile;
	private ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		try{
			if(args.length > 0){
				TCPForwardServer server = new TCPForwardServer();
				server.config(args[0]);
				server.execute();
			}else{
				throw new Exception("You must supply a config file as the first command line parameter");
			}
		}catch(Exception e){
			System.out.println("Error: "+e.getMessage());
			System.out.println("Usage: TCPForwardServer [configFile]");
			e.printStackTrace();
		}
	}

	void config(String string) throws Exception {
		props = new Properties();
		props.load(new FileReader(new File(string)));
		SOURCE_PORT = new Integer(props.getProperty("source.port"));
		DESTINATION_PORT = new Integer(props.getProperty("destination.port"));
		DESTINATION_HOST = props.getProperty("destination.host");
		outputFile = props.getProperty("output.file");
		inputFile = props.getProperty("input.file");
		serverSocket = new ServerSocket(SOURCE_PORT); 
	}
	public void run() {
		try{
			this.execute();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void execute() throws Exception {
		while (true) { 
			Socket clientSocket = serverSocket.accept(); 
			ClientThread clientThread = 
					new ClientThread(clientSocket); 
			clientThread.setDestinationHost(DESTINATION_HOST);
			clientThread.setDestinationPort(DESTINATION_PORT);
			clientThread.setOutputFile(outputFile);
			clientThread.setInputFile(inputFile);
			clientThread.start(); 
		}
	} 
}