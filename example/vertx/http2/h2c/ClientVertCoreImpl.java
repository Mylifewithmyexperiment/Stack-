package com.example.vertx.http2.h2c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.SocketAddress;

public class ClientVertCoreImpl {
	public static AtomicLong no_of_requests = new AtomicLong(0);
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static String ip;
	public static int port;
	public static int max_stream_id;

	
	public static HttpClient client;

	public static boolean setff;

	static Map<SocketAddress, HttpConnection> connections = new HashMap<>();

	public final static Properties configProps = new Properties();
	Future<?> future;
	ScheduledExecutorService exec;

	static HttpConnection conn;
	Vertx vertx = Vertx.vertx();
	Vertx new_vertx = Vertx.vertx();
	EventBus eb = vertx.eventBus();
	// First we will start with client and then toggle to supporting client.
	public boolean flag = true;
	final static List<HttpVersion> alpnVersions = new ArrayList<HttpVersion>();

	 
	public static final Set<String> DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS = new HashSet<>();

	HttpTimeoutRequestHandler exceptionHandler = new HttpTimeoutRequestHandler();

	HttpClientContinueHandler handler17 = new HttpClientContinueHandler();

	HttpClientEndRequestHandler endHandler = new HttpClientEndRequestHandler();

	 /* HttpClientRequestHandler clientReqHandler = new HttpClientRequestHandler();
	  not to be used as http/2 is only 1 way communication
	*/
	HttpClientResponseHandler clientResHandler = new HttpClientResponseHandler(this);
	HttpConnectionCloseHandler closeHandler = new HttpConnectionCloseHandler();
	HttpConnectionShutDownHandler shutDownHandler = new HttpConnectionShutDownHandler();

	 // Http2ConnectionHandler handler17 = new Http2ConnectionHandler();

	public static void main(String[] args) throws Exception {

		
		
		try {
			FileInputStream in = new FileInputStream(
					new File("." + File.separator + "Configuration" + File.separator + "load_test.properties"));
			configProps.load(in);
			in.close();

			FileInputStream IP = new FileInputStream(
					new File("." + File.separator + "Configuration" + File.separator + "config.properties"));
			Properties IPconfig = new Properties();
			IPconfig.load(IP);
			IP.close();

			ip = String.valueOf(IPconfig.getProperty("ip"));
			port = Integer.parseInt(IPconfig.getProperty("port"));
			max_stream_id = Integer.parseInt(IPconfig.getProperty("max_stream_id"));
			 } catch (FileNotFoundException e) {
			 System.out.println("error while reading from properties file.");
			e.printStackTrace();
		}

		ClientVertCoreImpl cl = new ClientVertCoreImpl();
		cl.start();

		Timer timer = new Timer();
		timer.schedule(new LoadListener(no_of_requests, no_of_response), 1000, 1000);

	 
		alpnVersions.add(HttpVersion.HTTP_2);
		//DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS.add("TLSv1.2");
 
	}

	public HttpClient initializeClient(String ip, int port) {
		HttpClient client = null;
		try {

			HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2)
					.setDefaultHost(ip).setDefaultPort(port)
			 		 .setAlpnVersions(alpnVersions)
			
			 /**	 
			 		 .setSsl(true)
					.setUseAlpn(true)
					
					
					.setTrustAll(true)	 
					
					 .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("/root/VertServer2/key.pem")
							 .setCertPath("/root/VertServer2/cert.pem"))
							 
					 
					
			 	 	.addEnabledSecureTransportProtocol("SSLv2Hello")
			 		.addEnabledSecureTransportProtocol("SSLv3")
			        .removeEnabledSecureTransportProtocol("TLSv1")
			 		 .removeEnabledSecureTransportProtocol("TLSv1.1")
			 	 	.removeEnabledSecureTransportProtocol("TLSv1.2")
			 	 	
			 	 **/	
			 		
				/*	 .setPfxKeyCertOptions(new PfxOptions().setPath("/root/VertServer2/pfxcertificate")
							 .setPassword("12345678"))
				*/	 
					
				//	.setKeyStoreOptions(new JksOptions().setPath("/root/VertClient2/hopeserver1.csr").setPassword("12345678"))
			 		
			;
	 	
			/**
			
			EventBus eb = vertx.eventBus();

			eb.consumer("news.uk.sport", message -> {
			  System.out.println("I have received a message: " + message.body());
			});
	
			eb.publish("news.uk.sport", "Yay! Someone kicked a ball");
			
			 
			 **/
			 
			/*
			
			DeliveryOptions deloption = new DeliveryOptions();
			deloption.addHeader("some-header", "some-value");
			eb.send("from delivery option", "Yay! Someone kicked a ball", deloption);
			*/
			
			
			/*consumer1.completionHandler(res -> {
				  if (res.succeeded()) {
				    System.out.println("The handler registration has reached all nodes");
				  } else {
				    System.out.println("Registration failed!");
				  }
				});
			
			consumer.unregister(res -> {
				  if (res.succeeded()) {
				    System.out.println("The handler un-registration has reached all nodes");
				  } else {
				    System.out.println("Un-registration failed!");
				  }
				});*/
			
			
			
		
			
			
			//System.out.println(options.getEnabledSecureTransportProtocols());
			
		//	HttpClientOptions new_options = new HttpClientOptions();
		//	new_options.setDefaultHost(ip).setDefaultPort(port).setProtocolVersion(HttpVersion.HTTP_2);
			
			
		//	client = new_vertx.createHttpClient(new_options);
			client = vertx.createHttpClient(options);
			client.connectionHandler(new Handler<HttpConnection>() {
				@Override
				public void handle(HttpConnection event) {
					System.out.println("First Client:connection succesful " + event.localAddress());
					conn = event;
					
					// event.closeHandler(closeHandler);  // Not called
				 //	event.shutdownHandler(shutDownHandler);  // this is also nnot called 
				}
			});

			System.out.println("First client has been initialized.");

			flag = false;

		} catch (IllegalArgumentException e ) {
			e.printStackTrace();
		}
		return client;
	}

	// initializing supporting client

	public HttpClient initializeSupportingClient(String ip, int port) {
		HttpClient supporting_client = null;
		try {

			HttpClientOptions options = new HttpClientOptions()
				//	.setHttp2MultiplexingLimit(2)
					.setProtocolVersion(HttpVersion.HTTP_2).setDefaultHost(ip).setDefaultPort(port)
			  	 .setAlpnVersions(alpnVersions)
	 /**		.setSsl(true)
					.setUseAlpn(true)
					 .setPfxKeyCertOptions(new PfxOptions().setPath("/root/VertServer2/pfxcertificate")
							 .setPassword("12345678"))
					 
					
					
					//.setKeyStoreOptions(new JksOptions().setPath("/root/VertClient2/hopeserver1.csr").setPassword("12345678"))
						 .setTrustAll(true)	 
		 
		 **/
			  	
			  	 /**
			  	 .setSsl(true)
					.setUseAlpn(true)
					
					
					.setTrustAll(true)	 
			 	
					  .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("/root/VertServer2/key.pem")
							 .setCertPath("/root/VertServer2/cert.pem"))
				 		 
			 	.addEnabledSecureTransportProtocol("SSLv2Hello")
		//	 	.addEnabledSecureTransportProtocol("SSLv3")
		 	  .removeEnabledSecureTransportProtocol("TLSv1")
		 	 .removeEnabledSecureTransportProtocol("TLSv1.1")
		 	 	.removeEnabledSecureTransportProtocol("TLSv1.2")
		 		 
			  	 
			  	 **/
			  	 
			;
 
	//		HttpClientOptions new_Options = new HttpClientOptions();
	//		new_Options.setDefaultHost(ip).setDefaultPort(port).setMaxWebsocketFrameSize(5000);
 
			supporting_client = vertx.createHttpClient(options);
		//	supporting_client = new_vertx.createHttpClient(new_Options);
			supporting_client.connectionHandler(new Handler<HttpConnection>() {

				@Override
				public void handle(HttpConnection event) {
					System.out.println("Second Client:connection succesful " + event.localAddress());
					// conn = event;
					// sup_conn=event;
					// connections.put(event.localAddress(), event);

				}
			});

			System.out.println("Supporting client has been initialized.");

			flag = true;
			// HttpResponseHandler handler = new HttpResponseHandler(this);
			// startLoad(supporting_client, handler);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return supporting_client;
	}

	public void start() throws Exception {

		// client = initializeClient("127.0.0.1", 8087);
		client = initializeClient(ip, port);
		startLoad(client, clientResHandler);

	}

	public void toggleMe() {
		try {
			// process when stream Id is about to exhaust
			HttpClient cl = null;
			// Refer the current client
			HttpClient cli = null;
			if (flag) {

				// cl = initializeClient("127.0.0.1", 8087);
				cl = initializeClient(ip, port);

			} else {

				// cl = initializeSupportingClient("127.0.0.1", 8087);
				cl = initializeSupportingClient(ip, port);

			}

			HttpClientResponseHandler.strid = 0;
			System.out.println("\n Stream Id exhausted "
					+ " " + "and now another client will take over the connection.\n");
			// Select the active client (either client or supporting client)
			cli = client;
			client = cl;
			cli.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class RequestSenderTask implements Runnable {

		
		@Override
		public void run() {
			try {
				no_of_requests.incrementAndGet();
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
			
			HttpClientRequest req = client.post("/");
			
		/*	
			String jsonString = "{\"foo\":\"bar\"}";
			JsonObject jobject = new JsonObject(jsonString);
			//jobject.put("foo", "bar").put("num", 123).put("mybool", true);
			*/
	//		Buffer buff = Buffer.buffer("some string", "UTF-16");  // also buffer string sending throws exceptions
		
			/*byte[] bytes = new byte[] {1, 3, 5};
			Buffer buff = Buffer.buffer(bytes);
			*/
			
			req.absoluteURI();
			// System.out.println("The request uri is " +req.absoluteURI());
			req.handler(clientResHandler);
			
			req.headers().add("h1", "50");
			req.putHeader("content-type", "text/plain");
			req.headers().add("shashi","55029327");
			req.putHeader("content-length", "794");
			
		    req.setTimeout(10000);
			req.query();
			req.sendHead();
			// check
			req.continueHandler(handler17);
			req.exceptionHandler(exceptionHandler);
	     	req.write("The first use of a new stream identifier implicitly closes all\r\n"
					+ "streams in the \"idle\" state that might have been initiated by that\r\n"
					+ "peer with a lower-valued stream identifier. For example, if a client\r\n"
					+ "sends a HEADERS frame on stream 7 without ever sending a frame on\r\n"
					+ "stream 5, then stream 5 transitions to the \"closed\" state when the\r\n"
					+ "first frame for stream 7 is sent or received.\r\n"
					+ "Stream identifiers cannot be reused. Long-lived connections can\r\n"
					+ "result in an endpoint exhausting the available range of stream\r\n"
					+ "identifiers. A client that is unable to establish a new stream\r\n"
					+ "identifier can establish a new connection for new streams. A server\r\n"
					+ "that is unable to establish a new stream identifier can send a GOAWAY\r\n"
					+ "frame so that the client is forced to open a new connection for new\r\n" + "streams.");
 
           
        //	req.endHandler(endHandler); // its positioning ?? here only
			req.end();

		}

	}

	public void startLoad(HttpClient client, HttpClientResponseHandler handler) throws IOException {

		this.exec = Executors.newScheduledThreadPool(Integer.parseInt(configProps.getProperty("clientThreads")));

		 
		for (int clients = 0; clients < Integer.parseInt(configProps.getProperty("noOfClients")); clients++)

		 
		 
		{

			 
			future = ((ScheduledExecutorService) exec).scheduleAtFixedRate(new RequestSenderTask(),
					Integer.parseInt(configProps.getProperty("initialdelay")),
					Integer.parseInt(configProps.getProperty("period")),
					TimeUnit.valueOf(configProps.getProperty("timeUnit")));

		}
	}
}