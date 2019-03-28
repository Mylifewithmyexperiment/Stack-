package com.rjil.http2.vertx.clientCallbacks;

import java.util.HashMap;
import java.util.Map;

import com.rjil.http2.vertx.main.ClientHandler;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClientResponse;

public class HttpClientResponseHandler implements Handler<HttpClientResponse> {
	public static int strid;

	private ClientHandler c;
	// ordered declaration of different clients.
 
	HttpClientResponseBodyHandler bodyHandler = new HttpClientResponseBodyHandler();

	public HttpClientResponseHandler(ClientHandler c) {
		this.c = c;
	}
 

	@Override
	public void handle(HttpClientResponse event) {
		// TODO Auto-generated method stub
		strid = event.request().streamId();
		
 		event.pause();
	
		
		// map to put streamid and other details.
		
		Map<String, String> details = new HashMap<>();
	//   HttpClientResponse stream =event.fetch(strid);
		
		
		// make body handler 
		event.bodyHandler(bodyHandler) ;
	 	
	 
	 	
	 	// System.out.println("client response   "+event.request());
		
		
		 details.put("Stream_id :->", Integer.toString(strid) );
		 details.put("Version:->"	, event.version().toString());
		 details.put("Status_Message:->", event.statusMessage().toString());
		 details.put("Cookies:->", event.cookies().toString());
		 details.put("Method_Type:-> ", event.request().method().toString());
		 details.put("Response_Header :->", event.headers().entries().toString());
		 details.put("Response_Trailers :->", event.trailers().entries().toString());
	 
    	System.out.println("Some of the Response details in httpClientResponseHandler are :- "+ details);
	
	 	event.resume();
 
	/*	// output 
	 	Some of the Response details are :- {Stream_id :->=121, Status_Message:->=OK, 
	  * Version:->=HTTP_2, Method_Type:-> =POST, Response_Trailers :->=[], Cookies:->=[],
	  *  Response_Header :->=[:status=200, content-type=text/html, content-length=76]}
 	*/
	 	
	 	
		if (HttpClientResponseHandler.strid > ClientHandler.max_stream_id ) {
			this.c.toggleMe();
		}
		System.out.println("Received response for stream ID:-> " + strid);
	 
		 
		ClientHandler.no_of_response.incrementAndGet();
		
		 
		
	}

}
