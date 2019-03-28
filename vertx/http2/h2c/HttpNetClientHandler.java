package com.example.vertx.http2.h2c;

import io.vertx.core.AsyncResult;
import io.vertx.core.net.NetSocket;

//Handler<AsyncResult<NetSocket>>
public class HttpNetClientHandler implements  AsyncResult<NetSocket> {

	@Override
	public NetSocket result() {
		// TODO Auto-generated method stub
		
		System.out.println("I am in netsocket result");
		return null;
	}

	@Override
	public Throwable cause() {
		// TODO Auto-generated method stub
		System.out.println("I am in netsocket cause");
		return null;
	}

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		System.out.println("I am in netsocket succeeded");
		return false;
	}

	@Override
	public boolean failed() {
		// TODO Auto-generated method stub
		System.out.println("I am in netsocket  failed");
		return false;
	}

}
