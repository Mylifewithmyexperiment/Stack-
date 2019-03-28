package com.example.vertx.http2.h2c;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.Http2Settings;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;

 //import io.netty:netty-tcnative-boringssl-static;
 

public class ServerApi extends AbstractVerticle {
	public static AtomicLong no_of_request = new AtomicLong(0); // for better operations (read write long data)
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static int streamID;
	public static String server_ip;
	public static int server_port;
    public Properties ipconfig = new Properties(); 
	  
    public  Http2Settings settings = new Http2Settings();
    
	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(ServerApi.class);
	}

	@Override
	public void start() throws Exception {
	
		
		try {
			FileInputStream propFile= new FileInputStream(new File("." + File.separator + "Configuration" + File.separator + 
					"propertyFile.properties"));

			
			ipconfig.load(propFile);
			propFile.close();
			
 
			server_ip= String.valueOf(ipconfig.getProperty("server_ip"));
			server_port = Integer.parseInt(ipconfig.getProperty("server_port"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	
	 	HttpServer server = vertx.createHttpServer(new HttpServerOptions());
	 
		
		HttpServerOptions options =  new HttpServerOptions()
				 
			 .setPort(server_port)
			 .setHost(server_ip)
			 .setIdleTimeout(4000)
			 .setIdleTimeoutUnit(TimeUnit.SECONDS)
			 .setLogActivity(true)
			 .setInitialSettings(settings)
			  .setCompressionLevel(6)
			 
		;
		 
		
	    server = vertx.createHttpServer(options);
		server.connectionHandler(new Handler<HttpConnection>() {
			
			@Override
			public void handle(HttpConnection event) {
           System.out.println("Incoming connection with:"+event.remoteAddress());
           
			}
		});
		 
		server.requestHandler(
				    res -> {
						no_of_request.incrementAndGet();
						streamID = res.response().streamId();
						res.response().putHeader("content-type", "text/html")
						.putHeader("resheader", "headerValue").end("<html><body>" + 
				             "<h1>Hello from vert.x!</h1>"
								+ "<p>version = " + res.version() + "</p>" + "</body></html>");
						no_of_response.incrementAndGet();;

					}) 
	 
		.listen(Integer.parseInt(ipconfig.getProperty("server_port")), 
							String.valueOf(ipconfig.getProperty("server_ip")) );
					 
		
  

		Timer timer = new Timer();
		timer.schedule(new LoadListener(no_of_request, no_of_response), 1000, 1000);

	}
}