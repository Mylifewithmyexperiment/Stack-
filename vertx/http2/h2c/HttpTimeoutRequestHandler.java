package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;

public class HttpTimeoutRequestHandler implements Handler<Throwable> {

	@Override
	public void handle(Throwable TimeoutExeHandler) {
		// TODO Auto-generated method stub
		System.out.println("The request timeout exception from HttpTimeoutRequestHandler class is  " +TimeoutExeHandler);
	}

}
