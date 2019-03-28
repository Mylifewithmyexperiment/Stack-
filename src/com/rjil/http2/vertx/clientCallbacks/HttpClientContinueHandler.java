package com.rjil.http2.vertx.clientCallbacks;

import io.vertx.core.Handler;

public class HttpClientContinueHandler  implements Handler<Void>{

	@Override
	public void handle(Void event) {
		
  System.out.println("  If you send an HTTP request with the header "
		+ "Expect set to the value 100-continue and the server responds"
		+ " with an interim HTTP response with a status code of 100 and a "
		+ "continue handler has been set using this method, then the handler"
		+ " will be called. /n"
		+ "You can then continue to write data to the request body and later end it. "
		+ "This is normally used in conjunction with the sendHead() "
		+ "method to force the request header to be written before the request has ended. "
		+ "");		

//this is just a confirmation handler.
	}

	

}
