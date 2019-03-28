package com.example.vertx.http2.h2c;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;

public class HttpClientResponseHandler implements Handler<HttpClientResponse> {
	static int strid;

	private ClientApi c;
	// ordered declaration of different clients.
 
	HttpResponseBodyHandler bodyHandler = new HttpResponseBodyHandler();

	public HttpClientResponseHandler(ClientApi c) {
		this.c = c;
	}
 

	@Override
	public void handle(HttpClientResponse event) {
		// TODO Auto-generated method stub
		strid = event.request().streamId();
		
/*		event.pause();
	
		
		// map to put streamid and other details.
		
		Map<String, String> details = new HashMap<>();
	// HttpClientResponse stream =event.fetch(strid);
		
		
		// make body handler 
		event.bodyHandler(bodyHandler) ;
	
	  
		
	MultiMap resHeader =  event.headers();
	MultiMap resTrailer = event.trailers();
	
	System.out.println( " client response trailer msg  are:-> "
			+ "&&&&&&"+ event.trailers().toString());
	 
	
	
	//System.out.println( "the multi map is  "+ multi.iterator());
	//output 
     //	the multi map is  java.util.ArrayList$Itr@12e8aa3d
	
	//now output is 
	// the multi map is  io.vertx.core.http.impl.Http2HeadersAdaptor@6c48153e
//	System.out.println( "the multi map is  "+ resHeader.entries().toString());
	
	
	
	 for(String hed : multi.get())
	 {
		 System.out.println("Headers are " +hed);
	 }
	 
	 
	
	ListMultimap<String, String> multimap = ArrayListMultimap.create();
	   for (President pres : US_PRESIDENTS_IN_ORDER) {
	     multimap.put(pres.firstName(), pres.lastName());
	   }
	   for (String firstName : multimap.keySet()) {
	     // List<String> lastNames = multimap.get(firstName);
	     out.println(firstName + ": " + lastNames);
	   }
 
	
	
   //	MultiMap multi1 = event.getHeader(null); //what parameter to put and fetch.
	
	//	System.out.println( " client response headers are:-> "+ multi);
		
	//	output  :->  client response headers are:-> io.vertx.core.http.impl.Http2HeadersAdaptor@38b4d693

		// conversion of httpClientresponse to string.
	
	 
		
		
	//	System.out.println("client response   "+event.request());
		
		
		 details.put("Stream_id :->", Integer.toString(strid) );
		 details.put("Version:->"	, event.version().toString());
		 details.put("Status_Message:->", event.statusMessage().toString());
		 details.put("Cookies:->", event.cookies().toString());
		 details.put("Method_Type:-> ", event.request().method().toString());
		 details.put("Response_Header :->", resHeader.entries().toString());
		 details.put("Response_Trailers :->", resTrailer.entries().toString());
	 
    	System.out.println("Some of the Response details are :- "+ details);
	
	 	event.resume();
 
		// output 
	 	Some of the Response details are :- {Stream_id :->=121, Status_Message:->=OK, 
	  * Version:->=HTTP_2, Method_Type:-> =POST, Response_Trailers :->=[], Cookies:->=[],
	  *  Response_Header :->=[:status=200, content-type=text/html, content-length=76]}

*/
		
		if (HttpClientResponseHandler.strid > this.c.max_stream_id ) {
			this.c.toggleMe();
		}
		System.out.println("Received response for stream ID:-> " + strid);
	 
		 
		ClientApi.no_of_response.incrementAndGet();
		
		 
		
	}

}
