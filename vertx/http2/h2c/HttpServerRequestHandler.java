package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerRequestHandler  implements Handler<HttpServerRequest>{
 
	HttpServerRequestExceptionHandler serverRequestExceptionHandler = new HttpServerRequestExceptionHandler();
	HttpServerRequestEndHandler serverRequestEndHandler = new HttpServerRequestEndHandler();
	ServerApi sj;
	
	@Override
	public void handle(HttpServerRequest serverReqHandler) {
		// TODO Auto-generated method stub
	//  serverReqHandler -> {
		
		serverReqHandler.endHandler(serverRequestEndHandler);
		serverReqHandler.exceptionHandler(serverRequestExceptionHandler);
		
		System.out.println("i am here at server request handler");
				ServerApi.no_of_request.incrementAndGet();
				ServerApi.streamID = serverReqHandler.response().streamId();
		// 
			//	fetching stream id from requestreceivedataServer then its response which is sent back to client 
				
				serverReqHandler.response()
				.putHeader("content-type", "text/html").putHeader("resheader", "headerValue")
						.end("<html><body>" + "<h1>Hello from vert.x!</h1>" + "<p>version = " + 
								serverReqHandler.version() + "</p>"
								+ "</body></html>");
						
					 
				System.out.println(	"Gettting server method"+ serverReqHandler.method());
				//reqRecAtServer.getHeader(headerName);
					  System.out.println("getting uri " +serverReqHandler.absoluteURI() );
			System.out.println( " checking ssl "+   serverReqHandler.isSSL());
				ServerApi.no_of_response.incrementAndGet();
				;

		 	}
	}

 