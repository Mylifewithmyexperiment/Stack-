package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;


public class HttpClientSendHeadHandler implements Handler<HttpVersion> {

	@Override
	public void handle(HttpVersion httpClientsendHeadHandlerEvent) 
	{
		 
		
		System.out.println("Like sendHead() but with an handler after headers have been sent. "
				+ "The handler will be called with the HttpVersion if it can be determined "
				+ "or null otherwise."  + httpClientsendHeadHandlerEvent);
		
		
		
	}

}
