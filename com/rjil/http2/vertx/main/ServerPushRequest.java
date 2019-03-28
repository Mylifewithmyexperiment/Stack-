package com.rjil.http2.vertx.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

import org.omg.CORBA.Request;

import com.rjil.http2.serverCallbacks.HttpServerConnectionHandler;
import com.rjil.http2.serverCallbacks.HttpServerRequestHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class ServerPushRequest extends AbstractVerticle {

	public static AtomicLong no_of_request = new AtomicLong(0); // for better operations (read write long data)
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static int streamID;
	public static String server_ip;
	public static int server_port;
 
	 
  HttpServerConnectionHandler serverConnectionHandler = new HttpServerConnectionHandler();
  HttpServerRequestHandler serverRequestHandler = new HttpServerRequestHandler();

	public Properties propertyFile = new Properties();

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(ServerPushRequest.class);

	}

	public void start() throws Exception {
		try {
		FileInputStream PropFileInputStream = new FileInputStream(
				new File("." + File.separator + "Configuration" + File.separator + "propertyFile.properties"));

		propertyFile.load(PropFileInputStream);
		PropFileInputStream.close();
		

			server_ip = String.valueOf(propertyFile.getProperty("server_ip"));
			server_port = Integer.parseInt(propertyFile.getProperty("server_port"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// now creating http server
		// SelfSignedCertificate certificate = SelfSignedCertificate.create();


	 	HttpServer server = vertx.createHttpServer(new HttpServerOptions());
	 
		
		HttpServerOptions options =  new HttpServerOptions()
				// .setSslEngineOptions(engine)   will do it later right now only true 
			 .setPort(server_port)
			 .setHost(server_ip);


	    server = vertx.createHttpServer(options);
		server.connectionHandler(new Handler<HttpConnection>() {
			
			@Override
			public void handle(HttpConnection event) {
           System.out.println("Incoming connection with:"+event.remoteAddress());
           
			}
		});
		

		 
 	// server.requestHandler(serverRequestHandler);
		
	/*server.requestHandler(
				    res -> {
						no_of_request.incrementAndGet();
						streamID = res.response().streamId();
						res.response().putHeader("content-type", "text/html")
						.putHeader("resheader", "headerValue").end("<html><body>" + 
				             "<h1>Hello from vert.x!</h1>"
								+ "<p>version = " + res.version() + "</p>" + "</body></html>");
						no_of_response.incrementAndGet();

					}) 
	 
		.listen(Integer.parseInt(propertyFile.getProperty("server_port")), 
							String.valueOf(propertyFile.getProperty("server_ip")) );
					*/ 
		
  // server.connectionHandler(serverConnectionHandler);
		
   server.requestHandler(serverRequestHandler)
   .listen(Integer.parseInt(propertyFile.getProperty("server_port")),
	String.valueOf(propertyFile.getProperty("server_ip")));
	
   Timer timer = new Timer();
   timer.schedule(new LoadListener(no_of_request, no_of_response), 1000, 1000);

	}

}

/**
 * Aim of this project is to activate all handlers.
 * 
 * 
 *  need to check serverRequestHandler :- it throws timeout exceptions
 *  it is resolved now . it was due to client side request sending content whose length was inappropriately applied.
 
 * server connection handler is not required as it is used for http/1.x
 * 
 * 
 * 
 * server push
 * 
 */
