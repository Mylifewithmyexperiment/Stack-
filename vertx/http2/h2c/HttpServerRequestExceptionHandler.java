package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;

public class HttpServerRequestExceptionHandler implements Handler<Throwable> {

	@Override
	public void handle(Throwable event) {
		// TODO Auto-generated method stub
		System.out.println("I am at HttpServerRequestExceptionHandler which throw "
				+ " an exception handler on the read stream. " +event );
	}

}
