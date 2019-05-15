package com.mongo.operators;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoInsertCommand {

	public static void main(String[] args) {
		
		try(MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("travel");
			MongoCollection<Document> collection = database.getCollection("flights");
			
			Document airlineDoc = new Document("airlineID","98765");
			airlineDoc.append("name", "Java Airways");
			airlineDoc.append("country", "England");
			airlineDoc.append("active", true);
			
			Document originDoc = new Document("country","Netherlands Antilles");
			originDoc.append("code", "SAB");
			originDoc.append("name", "Juancho E. Yrausquin");
			originDoc.append("city", "Saba");
			
			Document destinationDoc = new Document("country","United Kingdom");
			destinationDoc.append("code", "LHR");
			destinationDoc.append("name", "Heathrow");
			destinationDoc.append("city", "London");
			
			Document flightDoc = new Document("_id", 62466);
			flightDoc.append("airline", airlineDoc);
			flightDoc.append("origin", originDoc);
			flightDoc.append("destination", destinationDoc);
			
			collection.insertOne(flightDoc);
		}
		
	}

}
