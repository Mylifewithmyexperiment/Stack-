package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;

public class HttpClientEndRequestHandler  implements Handler<Void>{

	@Override
	public void handle(Void event) {
		// TODO Auto-generated method stub
		System.out.println(" Set a client end request handler. Once the stream has ended,"
				+ " and there is no more data to be read, this handler will be called " + event);
		
	}

}
