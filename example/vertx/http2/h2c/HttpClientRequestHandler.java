package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;

public class HttpClientRequestHandler implements Handler<HttpClientRequestHandler>  {

	public static  int stream_id; 
	
	@Override
	public void handle(HttpClientRequestHandler event) {
		System.out.println("I am in HttpClientRequestHandler "
				+ "(here, i will handle request & then send )  " +event);
		
		
		/*//event.handle(event); // only different method
		// few question 
	//	need to get stream_id from client side while sending streams 
		// HttpClientRequestHandler.stream_id  ; 
		 
		// objective while pause -> will fetch data from here 
		// not from  HttpClientResponseHandler
		//corelating it from client class
		*/
		
		//     ANSWER TO ABOVE QUESTIONS 
		// http/2 is a one way communication stack
		
		}

}
