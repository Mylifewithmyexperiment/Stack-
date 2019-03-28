package com.rjil.http2.vertx.clientCallbacks;

import io.vertx.core.Handler;

public class HttpConnectionShutDownHandler implements Handler<Void> {

	@Override
	public void handle(Void event) {
		// TODO Auto-generated method stub
		System.out.println("In shutdown handler:-> Set an handler called when a GOAWAY"
				+ " frame has been sent or "
				+ "received and all connections are closed. "
				+ "This is not implemented for HTTP/1.x.\r\n" + "" + event);
	}

}
