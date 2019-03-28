package com.rjil.http2.vertx.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
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

import com.rjil.http2.vertx.clientCallbacks.HttpClientContinueHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpClientEndRequestHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpClientRequestHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpClientResponseHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpClientTimeoutRequestExceptionHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpConnectionCloseHandler;
import com.rjil.http2.vertx.clientCallbacks.HttpConnectionShutDownHandler;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.SocketAddress;

public class ClientHandler {
	public static AtomicLong no_of_requests = new AtomicLong(0);
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static String ip;
	public static int port;
	public static int max_stream_id;
	 
	public static HttpClient client;
	
	public static boolean setff;

	static Map<SocketAddress, HttpConnection> connections = new HashMap<>();

	public final static Properties loadTestPropertyFile = new Properties();
	public final static Properties configPropertyFile = new Properties();

	Future<?> future;
	ScheduledExecutorService exec;

	static HttpConnection conn;
	Vertx vertx = Vertx.vertx();

	// First we will start with client and then toggle to supporting client.
	public boolean flag = true;
	final static List<HttpVersion> alpnVersions = new ArrayList<HttpVersion>();

	public static final Set<String> DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS = new HashSet<>();

	HttpClientTimeoutRequestExceptionHandler exceptionHandler = new HttpClientTimeoutRequestExceptionHandler();
	HttpClientContinueHandler continueHandler = new HttpClientContinueHandler();
	HttpClientEndRequestHandler endHandler = new HttpClientEndRequestHandler();
	HttpClientResponseHandler clientResHandler = new HttpClientResponseHandler(this);
	HttpConnectionCloseHandler closeHandler = new HttpConnectionCloseHandler();
	HttpConnectionShutDownHandler shutDownHandler = new HttpConnectionShutDownHandler();

	public static Http2Settings settings = new Http2Settings();

	HttpClientRequestHandler clientReqHandler = new HttpClientRequestHandler();
	// Not to be used as http/2 is only 1 way communication so client request are
	// not handled only responses are required to handle

	public static void main(String[] args) throws Exception {

		try {
			FileInputStream fileOfLoadTest = new FileInputStream(
					new File("." + File.separator + "Configuration" + File.separator + "load_test.properties"));
			loadTestPropertyFile.load(fileOfLoadTest);
			fileOfLoadTest.close();

			FileInputStream fileOfConfigProperty = new FileInputStream(
					new File("." + File.separator + "Configuration" + File.separator + "config.properties"));
			configPropertyFile.load(fileOfConfigProperty);
			fileOfConfigProperty.close();

			ip = String.valueOf(configPropertyFile.getProperty("ip"));
			port = Integer.parseInt(configPropertyFile.getProperty("port"));
			max_stream_id = Integer.parseInt(configPropertyFile.getProperty("max_stream_id"));

		} catch (FileNotFoundException e) {
			System.out.println("Error while reading data from any  property files.");
			e.printStackTrace();
		}

		
	
		
		
		ClientHandler cl = new ClientHandler();
		cl.start();

		Timer timer = new Timer();
		timer.schedule(new LoadListener(no_of_requests, no_of_response), 1000, 1000);

	}

	public HttpClient initializeClient(String ip, int port) throws IOException {
		HttpClient client = null;
		try {
			HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2)
					.setDefaultHost(ip).setDefaultPort(port);

			client = vertx.createHttpClient(options);

			client.connectionHandler(new Handler<HttpConnection>() {
				@Override
				public void handle(HttpConnection event) {
					System.out.println("First Client:connection succesful " + event.localAddress());
					conn = event;

					
					event.closeHandler(closeHandler);
					event.shutdownHandler(shutDownHandler);
					/*
					 * try { event.goAway(0); } catch (UnsupportedOperationException e) { // TODO
					 * Auto-generated catch block e.printStackTrace(); } // disallow any new stream
					 * to form // connection do not support goaway here it says
					 */
				}
			});

			System.out.println("First client has been initialized.Okay");

			flag = false;

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client;

	}

	public HttpClient initializeSupportingClient(String ip, int supporting_port) {
		HttpClient supporting_client = null;

		try {

			HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2)
					.setDefaultHost(ip).setDefaultPort(port);
			supporting_client = vertx.createHttpClient(options);
			supporting_client.connectionHandler(new Handler<HttpConnection>() {

				@Override
				public void handle(HttpConnection event) {
					System.out.println("Inside Second Client:connection succesful " + event.localAddress());

					event.closeHandler(closeHandler);
					event.shutdownHandler(shutDownHandler);
					/*
					 * event.goAway(0); // disallow any new stream to form // connection do not
					 * support goaway here it says
					 */
				}
			});

			System.out.println("Supporting client has been initialized.okay");

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
			System.out.println(
					"\n Stream Id is about to exhaust and now another client will take over the connection.\n");
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
			req.handler(clientResHandler);
			req.putHeader("content-type", "text/plain");
			req.headers().add("value1", "value2");
			req.putHeader("content-length", "794");
			// req.setTimeout(10000);
			req.query();
			// req.sendHead();

			 
				/*FileInputStream jsonFileFromConfig = new FileInputStream(
						"C:\\Users\\shashi.jaiswal\\eclipse-workspace_new\\VertexHandler\\Configuration\\sample.json");
				*/
				
				
			 
			

			// req.setWriteQueueMaxSize(700); // for flow control (700 byte)
			// req.continueHandler(continueHandler);
			req.exceptionHandler(exceptionHandler);

			//  req.write(jsonByte); // here we will send the buffered data

			  
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
 
			req.endHandler(endHandler);

			req.end();

		}

	}

	public void startLoad(HttpClient client, HttpClientResponseHandler responseHandler) throws IOException {

		this.exec = Executors
				.newScheduledThreadPool(Integer.parseInt(loadTestPropertyFile.getProperty("clientThreads")));

		for (int clients = 0; clients < Integer.parseInt(loadTestPropertyFile.getProperty("noOfClients")); clients++)

		{

			future = ((ScheduledExecutorService) exec).scheduleAtFixedRate(new RequestSenderTask(),
					Integer.parseInt(loadTestPropertyFile.getProperty("initialdelay")),
					Integer.parseInt(loadTestPropertyFile.getProperty("period")),
					TimeUnit.valueOf(loadTestPropertyFile.getProperty("timeUnit")));

		}
	}
}

/**
 * Aim of this prioject is to activate all handlers
 * 
 * Close Handler when supporting client is initialized the close handler is
 * called of initial client.
 * 
 * $$ We can add different close handler for checking supporting client closing
 * which would reflect in 2nd initialization of 1st (initial client) . but not
 * necessary as it is understood.
 * 
 * ShutDown Handler on a similar line of close handler we can conclude this too
 * .
 * 
 * Goaway it says it is not supported for http/1.x.. but why we are explicitely
 * sending goaway as it is automatically sent by shutdownhandler when it is
 * called.
 * 
 * continue Handler need to chheck continue handler which work synchronously
 * with sendHead api
 * 
 * Exception Handler come to effect when request not being sent(for any reason)
 * till particular timeout time.
 * 
 * EndHandler Once the reading of entire request is done then this handler is
 * called.
 * 
 * 
 * 
 * 
 * 
 */
