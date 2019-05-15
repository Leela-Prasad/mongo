package com.mongo.operators;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoFindCommand3 {

	public static void main(String[] args) {
		
		try(MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("travel");
			MongoCollection<Document> collection = database.getCollection("flights");
			
			Bson destinationCode = eq("destination.code","JFK");
			Bson airlineId = gt("airline.airlineID", 4000);
			Bson originCode = or(
									eq("origin.code", "LAS"),
									eq("origin.code", "PHX")
								);
			
			Bson query = and(destinationCode,originCode,airlineId);
			FindIterable<Document> results = collection.find(query);
			for(Document next : results) {
				System.out.println(next.toJson());
			}
			
		}
		
	}

}
