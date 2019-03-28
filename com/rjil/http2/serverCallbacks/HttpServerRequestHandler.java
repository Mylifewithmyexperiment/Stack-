package com.rjil.http2.serverCallbacks;



import com.rjil.http2.vertx.main.ServerPushRequest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class HttpServerRequestHandler  implements Handler<HttpServerRequest>{
 
	HttpServerRequestExceptionHandler serverRequestExceptionHandler = new HttpServerRequestExceptionHandler();
	HttpServerRequestEndHandler serverRequestEndHandler = new HttpServerRequestEndHandler();
	ServerPushRequest sj;
	
 
	@Override
	public void handle(HttpServerRequest serverReqHandler) {
		// TODO Auto-generated method stub
	 
	serverReqHandler.endHandler(serverRequestEndHandler);
		//serverReqHandler.exceptionHandler(serverRequestExceptionHandler);
		
		System.out.println("i am here at server request handler");
				ServerPushRequest.no_of_request.incrementAndGet();
				ServerPushRequest.streamID = serverReqHandler.response().streamId();
		// 
			//	fetching stream id from requestreceivedataServer then its response which is sent back to client 
		
				
				// for server push explicitily 
	  serverReqHandler.response().push(HttpMethod.POST,  "Data from server push handler",
					 
						serResHander -> { 
							if(serResHander.succeeded())
							{
							System.out.println( "i am in server response handler to send  push resources to client " );
								HttpServerResponse pushingdataResponse = serResHander.result();
			 	pushingdataResponse.putHeader("content-type", "text/html").write("server push data from server side")
								.end();	
							}
							else {
								System.out.println("Could not push the resources to client 'coz the cause is " +serResHander.cause());
							}
							
						} );
	 
	 
	 // for sending files 
	// serverReqHandler.response().sendFile("C:\\\\Users\\\\shashi.jaiswal\\\\eclipse-workspace_new\\\\VertexServerPush\\\\Configuration\\\\sample.json");
	 
 //  for just sending responses
	 serverReqHandler.response().putHeader("content-type", "text/html").putHeader("resheader", "headerValue")
						.end("<html><body>" + "<h1>Hello from vert.x!</h1>" + "<p>version = " + 
								serverReqHandler.version() + "</p>"
								+ "</body></html>");
						
			  
					 
			//	System.out.println(	"Gettting server method"+ serverReqHandler.method());
				
			//reqRecAtServer.getHeader(headerName);
			//		  System.out.println("getting uri " +serverReqHandler.absoluteURI() );
		//	System.out.println( " checking ssl "+   serverReqHandler.isSSL());
				ServerPushRequest.no_of_response.incrementAndGet();
				

		 	}
	}

 /**

added server push concept



**/