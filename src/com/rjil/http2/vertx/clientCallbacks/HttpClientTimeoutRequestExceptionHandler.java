package com.rjil.http2.vertx.clientCallbacks;

import io.vertx.core.Handler;

public class HttpClientTimeoutRequestExceptionHandler implements Handler<Throwable> {

	@Override
	public void handle(Throwable TimeoutExeHandler) {
		// TODO Auto-generated method stub
		System.out.println(
				"I occur 'coz of timeout exception which occur due to request not being sent(due to any reason) "
						+ "for particular specified timeout time." + TimeoutExeHandler);
	}

}
