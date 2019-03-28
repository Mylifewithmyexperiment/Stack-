package com.example.vertx.http2.h2c;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;

 //import io.netty:netty-tcnative-boringssl-static;
 

public class ServerVertCoreImpl extends AbstractVerticle {
	public static AtomicLong no_of_request = new AtomicLong(0); // for better operations (read write long data)
	public static AtomicLong no_of_response = new AtomicLong(0);
	public static int streamID;
	public static String server_ip;
	public static int server_port;
    public Properties ipconfig = new Properties(); 
	final static List<HttpVersion> alpnVersions = new ArrayList<HttpVersion>();

    public static final Set<String> DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS = new HashSet<>();
    
    HttpServerRequestHandler serverRequestHandler = new HttpServerRequestHandler();
    HttpServerConnectionHandler serverConnectionHandler = new HttpServerConnectionHandler();
     // Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(ServerVertCoreImpl.class);
	alpnVersions.add(HttpVersion.HTTP_2);
   // DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS.add("TLSv1.2");

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
				// .setSslEngineOptions(engine)   will do it later right now only true 
			 .setPort(server_port)
			 .setHost(server_ip)
		// .getAlpnVersions()
		
			 // keystore Attempt
			 
		 /**	 	 
		   	 .setAlpnVersions(alpnVersions)
		    	.setSsl(true)
			//	 .setEnabledSecureTransportProtocols(DEFAULT_ENABLED_SECURE_TRANSPORT_PROTOCOLS)
				 .setUseAlpn(true) 
				//.setKeyStoreOptions(new JksOptions().setPath("/root/VertServer2/serverSidekeystore"))
			 	
			.setKeyStoreOptions(new JksOptions().setPath("/root/VertServer2/hopeserver1.csr").setPassword("12345678"))
			 
			 
	 **/
			 
			 
		/**	 
			 // pem File Attempt
			 .setSsl(true)
			 .setUseAlpn(true)
			 .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("/root/VertServer2/key.pem")
					 .setCertPath("/root/VertServer2/cert.pem"))
			 
			    .addEnabledSecureTransportProtocol("SSLv2Hello")
			  //  .addEnabledSecureTransportProtocol("SSLv3")
		 		 .removeEnabledSecureTransportProtocol("TLSv1")
		 		 .removeEnabledSecureTransportProtocol("TLSv1.1")
		 	 	.removeEnabledSecureTransportProtocol("TLSv1.2")
		 	 	
		 	 	**/
		 	 	
		 
		 	 	/**
			 // pfx file attempt
			 
			// "/root/VertServer2/pfxcertificate"
			 
			 .setSsl(true)
			 .setUseAlpn(true)
			 .setPfxKeyCertOptions(new PfxOptions().setPath("/root/VertServer2/pfxcertificate")
					 .setPassword("12345678"))
			 **/
				 ;
					
		 
		
	    server = vertx.createHttpServer(options);
		server.connectionHandler(new Handler<HttpConnection>() {
			
			@Override
			public void handle(HttpConnection event) {
           System.out.println("Incoming connection with:"+event.remoteAddress());
           
			}
		});
		 
//		server.requestHandler(serverRequestHandler);
		
	 
		
		
		server.requestHandler(
				    res -> {
						no_of_request.incrementAndGet();
						streamID = res.response().streamId();
			         	res.response().putHeader("content-type", "text/html")
						.putHeader("resheader", "headerValue")
						.end("<html><body> <h1>Hello from vert.x!</h1> <p>version = " + 
						res.version() + "</p>" + "</body></html>");
			         	
						System.out.println(" absolute uri "+res.absoluteURI());
						System.out.println("path " +res.path() );
						System.out.println("param " +res.params().entries().toString() );
						System.out.println(" headers" +res.headers().entries().toString() );
						System.out.println("methods " +res.method() );
						System.out.println("only uri " +res.uri() );   // by default uri is relative uri
						System.out.println("host " +res.host());
						no_of_response.incrementAndGet();

					}) 
	 
		.listen(Integer.parseInt(ipconfig.getProperty("server_port")), 
							String.valueOf(ipconfig.getProperty("server_ip")) );
					 
		
  

		Timer timer = new Timer();
		timer.schedule(new LoadListener(no_of_request, no_of_response), 1000, 1000);

	}
}


/**   Documentation comments 
 some syso results:-
 param []
 headers[:method=POST, :path=/, :scheme=http, :authority=127.0.0.1:8087,
  h1=50, content-type=text/plain, shashi=55029327, content-length=794]
methods POST
 absolute uri http://127.0.0.1:8087/
path /
only uri /
  host 127.0.0.1:8087
  
  no params are set.
  headers are displayed in hash code  but after .entries .tostring it is displayed in string format.
  
  absolute uri are displayed in ip address and port no 
  
  :authority=127.0.0.1:8087 can be related to absolute uri 
  :authority = ip and port.
  
  and path i guess context  which is equal to relative uri ??? question 
  
  concept :-
  multimap data are fetched from entries which are then converted to string to display in syso else they r hashcoded formed
  
  
 **/ 
 

