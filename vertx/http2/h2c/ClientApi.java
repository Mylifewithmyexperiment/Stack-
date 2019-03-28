package com.example.vertx.http2.h2c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import io.vertx.core.http.Http2Settings;
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

/**
 * 
 * @author Shashi.Jaiswal
 * 
 *         clientApi  class is used to send request.
 */
public class ClientApi {
	//variables declarations
	public static AtomicLong no_of_requests = new AtomicLong(0);
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static String ip;
	public static int port;
	public static int max_stream_id;
	public static String jsonDataAsStringSending;
	public static HttpClient client;
	public static boolean setff;
	public String byteToStringDataSend;
	public boolean flag = true;
	static HttpConnection conn;
	public static Buffer byteFormatJsonDataFromConfigSend ;
	public static String StringFormatJsonFileFromConfig ;

	//properties file declarations
	public final static Properties configProps = new Properties();
	
	Future<?> future;
	ScheduledExecutorService exec;

	
	Vertx vertx = Vertx.vertx();
	EventBus eb = vertx.eventBus();
	final static List<HttpVersion> alpnVersions = new ArrayList<HttpVersion>();
	public static final Set<String> DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS = new HashSet<>();

	//Handlers declarations
	HttpTimeoutRequestHandler exceptionHandler = new HttpTimeoutRequestHandler();
	HttpClientContinueHandler handler17 = new HttpClientContinueHandler();
	HttpClientEndRequestHandler endHandler = new HttpClientEndRequestHandler();
	HttpClientSendHeadHandler sendHeadHandler = new HttpClientSendHeadHandler();
	HttpClientResponseHandler clientResHandler = new HttpClientResponseHandler(this);
	HttpConnectionCloseHandler closeHandler = new HttpConnectionCloseHandler();
	HttpConnectionShutDownHandler shutDownHandler = new HttpConnectionShutDownHandler();

	
	/*
	 * HttpClientRequestHandler clientReqHandler = new HttpClientRequestHandler();
	 * not to be used as http/2 is only 1 way communication
	 */
	
	public Http2Settings settings = new Http2Settings();
 
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
		} 
		catch (FileNotFoundException e) {
			System.out.println("error while reading from properties file.");
			e.printStackTrace();
		}

		try {
			  StringFormatJsonFileFromConfig = new String(Files.readAllBytes(Paths.get(
					"C:\\Users\\shashi.jaiswal\\eclipse-workspace_new\\VertexHandler\\Configuration\\sample.json")));


			byte[] jsonByte = StringFormatJsonFileFromConfig.getBytes();
			//  abc = (Buffer) ByteBuffer.wrap(jsonByte);
			  byteFormatJsonDataFromConfigSend = Buffer.buffer(jsonByte);
		  
			
		} catch (ClassCastException e  ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		ClientApi cl = new ClientApi();
		cl.start();

		try {
			String jsonString = "{\"foo\":\"bar\"}";
			JsonObject jobject = new JsonObject(jsonString);
			jobject.put("foo", "bar").put("num", 123).put("mybool", true);
			jsonDataAsStringSending = jobject.toString();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Timer timer = new Timer();
		timer.schedule(new LoadListener(no_of_requests, no_of_response), 1000, 1000);

	}

	public HttpClient initializeClient(String ip, int port) {
		HttpClient client = null;
		try {

			HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2)
					.setDefaultHost(ip).setDefaultPort(port).setAlpnVersions(alpnVersions).setConnectTimeout(4000) // for
																													// initial
																													// phase
					// .setIdleTimeout(4000)
					// .setKeepAliveTimeout(4000)
					.setLogActivity(true).setHttp2ClearTextUpgrade(true).setInitialSettings(settings)

			;
			client = vertx.createHttpClient(options);
			client.connectionHandler(new Handler<HttpConnection>() {
				@Override
				public void handle(HttpConnection event) {
					System.out.println("First Client:connection succesful " + event.localAddress());
					conn = event;

					// event.closeHandler(closeHandler); // Not called
					// event.shutdownHandler(shutDownHandler); // this is also nnot called
				}
			});

			System.out.println("First client has been initialized.");

			flag = false;

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return client;
	}

	// initializing supporting client

	public HttpClient initializeSupportingClient(String ip, int port) {
		HttpClient supporting_client = null;
		try {

			HttpClientOptions options = new HttpClientOptions()

					.setDefaultHost(ip).setDefaultPort(port).setAlpnVersions(alpnVersions).setConnectTimeout(4000) // for
																													// initial
																													// phase
					// .setIdleTimeout(4000)
					// .setKeepAliveTimeout(4000)
					.setLogActivity(true).setHttp2ClearTextUpgrade(true).setInitialSettings(settings);
			supporting_client = vertx.createHttpClient(options);
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

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return supporting_client;
	}

	public void start() throws Exception {

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

				cl = initializeClient(ip, port);

			} else {

				cl = initializeSupportingClient(ip, port);

			}

			HttpClientResponseHandler.strid = 0;
			System.out.println("\n Stream Id exhausted  " + "and now another client will take over the connection.\n");
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

			// byte data sending successful

			byte[] bytes = new byte[] { 1, 3, 5, 8 };
			Buffer byteDataSend = Buffer.buffer(bytes);
			// System.out.println("byte length " +buff.length());
			  byteToStringDataSend = byteDataSend.toString();

			req.absoluteURI();
			// System.out.println("The request uri is " +req.absoluteURI());
			req.handler(clientResHandler);
			req.headers().add("h1", "4");
			req.putHeader("content-type", "text/plain");
			req.headers().add("MultimapHeaderName", "MultimapHeaderValue");
			req.putHeader("content-length", "4"); // imp api without this request wouldnot be sent #794
			req.setTimeout(10000);
			req.query();
			// req.sendHead();
			req.sendHead(sendHeadHandler);
			
		/*	
			int frameType = 40;
			int frameStatus = 10;
			Buffer payload = Buffer.buffer("some data");

			// Sending a frame to the client
			req.writeCustomFrame(frameType, frameStatus, payload);
			
			System.out.println("after send head ");
			
		 */
			// req.continueHandler(handler17);
			req.exceptionHandler(exceptionHandler);
			
			//fetching data from config file and sending 
   //         req.write(byteFormatJsonDataFromConfigSend);  // as a buffers
			
			//req.write(JsonFileFromConfig); //as string 
			 
			

			// req.write(jsonDataAsStringSending);
			req.write(byteDataSend);
			// req.write(byteToStringDataSend);

			/*req.write("The first use of a new stream identifier implicitly closes all\r\n"
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
			
			
*/
			// after connection no data is being sent ..

			// req.setWriteQueueMaxSize(700); // for flow control it will depend according
			// to data length we transfer

			// req.endHandler(endHandler);
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

/**
 * 
 * Documentation comment
 * 
 * implementation in 280 -300 line no added json data as string in main method
 * api added in clients method
 * 
 * jar added sendhead handler added issue of blocking threads
 * for testing purpose we will send only string data as jars are not available in other peer project 
 * 
 **/