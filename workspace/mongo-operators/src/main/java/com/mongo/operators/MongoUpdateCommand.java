package com.mongo.operators;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class MongoUpdateCommand {

	public static void main(String[] args) {
		
		try(MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("travel");
			MongoCollection<Document> collection = database.getCollection("flights");
			
			Document queryDoc = new Document("_id","62466");
			Document updateDoc = new Document("$set" , new Document("airline.name","Mongo Airways"));
			
			UpdateResult result = collection.updateOne(queryDoc, updateDoc);
			System.out.println("Matched Count :: " + result.getMatchedCount()  + "Modified Count :: " + result.getModifiedCount());
		}
		
	}

}
