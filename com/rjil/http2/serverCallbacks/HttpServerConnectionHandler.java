package com.rjil.http2.serverCallbacks;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpConnection;

public class HttpServerConnectionHandler implements Handler<HttpConnection> {

 

	@Override
	public void handle(HttpConnection serverConnHandler) {
		// TODO Auto-generated method stub
		
		System.out.println("Incoming connection with:" + serverConnHandler.remoteAddress());

		
	}

}
