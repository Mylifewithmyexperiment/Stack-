package com.rjil.http2.serverCallbacks;

import io.vertx.core.Handler;

public class HttpServerRequestEndHandler implements Handler<Void> {

	@Override
	public void handle(Void event) {
		// TODO Auto-generated method stub
		System.out.println("Set a server request end handler.Once the stream has ended, "
				+ "and there is no more data to be read, this handler will be called" +event );
	}

}
