package com.rjil.http2.vertx.clientCallbacks;

import io.vertx.core.Handler;

public class HttpConnectionCloseHandler implements Handler<Void> {

	@Override
	public void handle(Void event) {
		// TODO Auto-generated method stub
		System.out.println("In close handler:->Set a close handler. The handler will get notified when the connection is closed.\r\n" + 
				"Parameters:handler the handler to be notified  :-  " +  event);
	}

}
