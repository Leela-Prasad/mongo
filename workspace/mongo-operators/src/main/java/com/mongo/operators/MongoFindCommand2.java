package com.mongo.operators;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoFindCommand2 {

	public static void main(String[] args) {
		
		try(MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("travel");
			MongoCollection<Document> collection = database.getCollection("flights");
			
			Document query = new Document("origin.code","LAS");
			query.append("destination.code", "JFK");
			
			FindIterable<Document> results = collection.find(query);
			for(Document next : results) {
				System.out.println(next.toJson());
			}
			
		}
		
	}

}
