package com.rjil.http2.serverCallbacks;

import com.rjil.http2.vertx.main.ServerHandler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerRequestHandler  implements Handler<HttpServerRequest>{
 
	HttpServerRequestExceptionHandler serverRequestExceptionHandler = new HttpServerRequestExceptionHandler();
	HttpServerRequestEndHandler serverRequestEndHandler = new HttpServerRequestEndHandler();
	ServerHandler sj;
	
	@Override
	public void handle(HttpServerRequest serverReqHandler) {
		// TODO Auto-generated method stub
	//  serverReqHandler -> {
		
	serverReqHandler.endHandler(serverRequestEndHandler);
		//serverReqHandler.exceptionHandler(serverRequestExceptionHandler);
		
		System.out.println("i am here at server request handler");
				ServerHandler.no_of_request.incrementAndGet();
				ServerHandler.streamID = serverReqHandler.response().streamId();
		// 
			//	fetching stream id from requestreceivedataServer then its response which is sent back to client 
				
				serverReqHandler.response()
				.putHeader("content-type", "text/html").putHeader("resheader", "headerValue")
						.end("<html><body>" + "<h1>Hello from vert.x!</h1>" + "<p>version = " + 
								serverReqHandler.version() + "</p>"
								+ "</body></html>");
						
				serverReqHandler.method();
				serverReqHandler.absoluteURI();
				
			//	System.out.println(	"Gettting server method"+ serverReqHandler.method()  +" and uri "  +serverReqHandler.absoluteURI());
				
			//reqRecAtServer.getHeader(headerName);
			//		  System.out.println("getting uri " +serverReqHandler.absoluteURI() );
		//	System.out.println( " checking ssl "+   serverReqHandler.isSSL());
				ServerHandler.no_of_response.incrementAndGet();
				

		 	}
	}

 