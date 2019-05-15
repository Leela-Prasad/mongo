package com.mongo.operators;

import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class MongoDeleteCommand {

	public static void main(String[] args) {
		
		try(MongoClient client = new MongoClient("localhost", 27017)) {
			MongoDatabase database = client.getDatabase("travel");
			MongoCollection<Document> collection = database.getCollection("flights");
			
			Bson query = or(
						    eq("origin.code","SAB"),
						    eq("destination.code","SAB")
						  );
			
			DeleteResult result = collection.deleteMany(query);
			System.out.println(result.getDeletedCount());
		}
		
	}

}
