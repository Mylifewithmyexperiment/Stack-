package com.rjil.http2.vertx.clientCallbacks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpClientResponseBodyHandler implements Handler<Buffer> {

	@Override
	public void handle(Buffer event) {
		// TODO Auto-generated method stub

		/*
		 * System.out.
		 * println(" Convenience method for receiving the entire request body in one piece. \r\n"
		 * + "\r\n" +
		 * "This saves you having to manually set a dataHandler and an endHandler and append the chunks"
		 * +
		 * " of the body until the whole body received. Don't use this if your request body is large - "
		 * + "you could potentially run out of RAM.\r\n" +
		 * "Parameters:bodyHandler This handler will be called after all the body has been received "
		 * );
		 */

		// fetching the body parts

	/*	JSONParser parser = new JSONParser();

		 // JSONObject jsonObject = (JSONObject) obj;

		try {
			Object obj = parser
				.parse(new FileReader("." + File.separator +
						"Configuration" + File.separator + "sample.json"));
		} 
		
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 
		
	//	System.out.println(name);

		System.out.println("5th option " + event.toJsonObject().toString());
		
		*/
 
		// for converting to json we need jaskson databind
	 
		//	// String name = (String) ((Map) resjs).get("name");
		
		//say to download jar
	/* 	try {
			JsonArray resjson=  event.toJsonArray()	;
			JsonObject s=	resjson.getJsonObject(0);
 System.out.println( "getting byte of data :-"+  s );
		} catch (NoClassDefFoundError e) {
			// TODO Auto-generated catch block
			System.out.println("I am here");
			e.printStackTrace();
		}
		 */
		
		
		
	//	System.out.println( "1st setting  "	+ event.toString() );
	//	System.out.println( "2nd setting  "	+event.getString(0, event.length()) );
	//	System.out.println( "3rd setting  "+ event.toJsonArray()	 );
	//	System.out.println( "4th setting  "	+event.toJsonObject() );
	//	System.out.println( "5th setting  "	+event.getString(0, event.length()) );
		 
		
		
		Map<String, String> responseDetail = new HashMap<>();
 
		responseDetail.put("BodyLength", "" + event.length());
		responseDetail.put("BodyContent", event.getString(0, event.length()));

	  System.out.println("The few Response Body details in HttpClientResponseBodyHandler are " + responseDetail);

		 

	}

}
