In Relational Databases data is stored in tables and there exists relationships between tables.

There are 2 types of databases
1. Integration Database.
2. Application Database.

A database is an Integration database when multiple applications are use same database.
A database is a Application database when only single application is using that database.

RDBMS is a better choice for an Integration database because multiple applications are using that database, so that we need to have rigid table structure and constraints on the tables, so that we don’t end in duplicating data.

But for an Application database we may use a No SQL database if it is giving more performance or scalability.

NO SQL databases are NOT replacement of RDBMS but just alternative database for application databases that might give better performance and scalability.


Limitations of RDBMS
1. Scalability

Scalability can be done either Vertically or Horizontally.
Vertically means we will have a single powerful machine with more RAM and HDD, but this is quite expensive and is single point of failure.
Horizontal scaling means we will have a group of servers which combined to form a server.

In Horizontal Scaling we need to deal with 2 things.
1. Replication
2. Sharding

Replication means we need to maintain same data on multiple server so that we will not loose that data in case of catastrophe.

Sharding means we will split the data into small pieces and these pieces are stored in multiple servers.


** A database should support both Replication and Sharding then only we will not loose any data and can scale.


Most Production RDBMS support Replication but Sharding is not possible, because it has relationships with other table and it difficult to execute a query whose data is present across multiple machines. Due to relationships it is hard to achieve Sharding in Relation Databases. So Relational Databases cannot scale horizontally.

But if you can design simple database sharing may be possible.



NOSQL databases doesn’t have constraints that Relational Databases have.(like not null, foreign key, unique etc)

Types of No SQL Databases.
1. Document
2. key value
3. Graph
4. Column Family
5. Object



1. Mongo DB
It is a document database.
Document Database is schema less.

There are no rows and columns, here we have documents
A document is a collection of data.
Group of related documents is called collection.




What is happening under the hood of Mongo?
Mongo keeps as much data as possible in RAM 

Read operation:
Mongo will check the record in the RAM if it is not present then it will go the disk

Write Operation:
Record will be written to the RAM first and after a delay it will flushed to the disk.
This delay is normally 60 seconds

But what happens if we write and write to RAM is successful and as soon as that if the  system is crashed before the data is flushed to disk then the data will be lost.

This is the Default Behaviour in Mongo and can be tunned by setting the Write Concern.

Write Concern:
The default write concern is “Acknowledged”
In this mode the client will be informed that write to in-Memory is successful but write to disk will happen after a delay.In this case there is a risk of loosing data.


In Journalled we can avoid above problem
In Journal what happens is the document is recorded to journal(<100 ms) .
Journal is Persistence.
But written documents from Journal to the actual disk will happen after a delay(60 seconds)
In case of crash journal will be read and writes will be happen from journal to disk.


Working Set:
Mongo will put data as much as can in the RAM.
if the RAM is full then it will flush Least Recently Used Record to the disk and this process is called paging.
The Working set in Mongo is the data which is in active use. and this working set needs to fit in RAM.



Mongo will write to this location by default
C:\data\db - windows
/data/db - linux

mongo - executable to start a mongo client
mongod - executable to start mongo server here 'd' is database.

To list Databases.
show dbs

** db will not be created in mongo until data is inserted into it.

To switch to a specific database
use <db-name>
eg: use customerManager

Here table is a collection 
table contains rows
collection contains documents.

To list collections
show collections

** Collection will not be created in mongo until data is inserted into it

Insert Data into Collection
db.collectionName.insert(document)
Here db refers to the current database.

db.customers.insert({name: 'John'})
db.customers.insert({name: 'Sally'})
db.customers.insert({name: 'Mike', address: {street: '1 High Street', city: 'High Town'}})


Retrieving Data:
db.collecitonName.find() - returns all documents in a collection
db.collectionName.find(query)
db.collecitonName.find(query,projection)

db.collecitonName.find()
{ "_id" : ObjectId("5cbd18a6462c7fdfa794bc7b"), "name" : "John" }
{ "_id" : ObjectId("5cbd18dd462c7fdfa794bc7c"), "name" : "Sally" }
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "High Town" } }


db.collectionName.find(query)

db.customers.find({name: 'Sally'})
db.customers.find({'address.city': 'High Town'})
** To retrieve data from Nested document we use dot operator and the field name must be in quotes.
Here if it finds any document match then it will give all fields in that documents
db.customers.find({'address.city': 'High Town'})
Result:
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "High Town" } }

If we want to output only specific fields projection can be useful.


db.collecitonName.find(query,projection)
db.customers.find({'address.city': 'High Town'}, {name: true})
Result:
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike" }


db.collectionName.findOne()
This will give only one document in a FORMATTED way.

Updating data:
db.collectionName.update(query, update)
Here update parameter can have 2 functionalities
{newDocument}
it can be a document then it will replace that document with this new document
{$set: {attr1: value1, attr2: value2}}
it can be list of fields that needs to be updated.

here $set is a operator.

db.customers.update({_id:ObjectId("5cbd18dd462c7fdfa794bc7c")}, {name: 'Amanda'})
It will replace entire document.

db.customers.update({_id:ObjectId("5cbd1911462c7fdfa794bc7d")}, {$set: {'address.city': 'Down Town'}})
This will update only city field.


** By Default update will only document.
db.customers.update({}, {$set: {active: true}})
Here {} in query will match all documents.
Result:
{ "_id" : ObjectId("5cbd18a6462c7fdfa794bc7b"), "name" : "John", "active" : true }
{ "_id" : ObjectId("5cbd18dd462c7fdfa794bc7c"), "name" : "Amanda" }
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "Down Town" } }


To update all Records we need to pass another parameter multi as true.
db.customers.update({}, {$set: {active: true}}, {multi: true})
Result:
{ "_id" : ObjectId("5cbd18a6462c7fdfa794bc7b"), "name" : "John", "active" : true }
{ "_id" : ObjectId("5cbd18dd462c7fdfa794bc7c"), "name" : "Amanda", "active" : true }
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "Down Town" }, "active" : true }


Removing Data:
db.collectionName.remove(query)

db.customers.remove({_id: ObjectId("5cbd18a6462c7fdfa794bc7b")})
Result:
{ "_id" : ObjectId("5cbd18dd462c7fdfa794bc7c"), "name" : "Amanda", "active" : true }
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "Down Town" }, "active" : true }

** If we want to remove only one record then we need to pass another parameter justOne as true
db.customers.remove({active: true}, {justOne: true})
Result:
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "Down Town" }, "active" : true }



We will insert a document and remove multiple documents.
db.customers.insert({name: 'John', active: true})
Result:
{ "_id" : ObjectId("5cbd1911462c7fdfa794bc7d"), "name" : "Mike", "address" : { "street" : "1 High Street", "city" : "Down Town" }, "active" : true }
{ "_id" : ObjectId("5cbd2205652b27fca6ceb646"), "name" : "John", "active" : true }

Remove all active customers
db.customers.remove({active: true})
Resutl: No documents

Exercise: Converting Relation DB tables to Mongo Documents.
Here we will deNormalize the tables to get performance by avoiding complex and costly join operation, this is intended

db.stock.insert({_id: 1, description: 'potatoes', 'Food Type': {description: 'Vegetable'}, 'Supplier': {description: 'Drake Farm', email: 'info@drake.farm.com'}, quantity: 49})

db.stock.insert({_id: 2, description: 'chicken', 'Food Type': {description: 'Meat'}, 'Supplier': {description: 'Billy Services', email: 'billy@billy.com'}, quantity: 27})

db.stock.insert({_id: 3, description: 'Peas', 'Food Type': {description: 'Vegetable'}, 'Supplier': {description: 'Drake Farm', email: 'info@drake.farm.com'}, quantity: 89})

db.stock.insert({_id: 4, description: 'Milk', 'Food Type': {description: 'Dairy'}, 'Supplier': {description: 'Billy Services', email: 'billy@billy.com'}, quantity: 35})

db.stock.insert({_id: 5, description: 'Cheese', 'Food Type': {description: 'Dairy'}, 'Supplier': {description: 'Buck Farm', email: 'sales@buck.com'}, quantity: 17})

db.stock.insert({_id: 6, description: 'Carrots', 'Food Type': {description: 'Vegetable'}, 'Supplier': {description: 'Buck Farm', email: 'sales@buck.com'}, quantity: 26})

db.stock.insert({_id: 7, description: 'Lamb', 'Food Type': {description: 'Meat'}, 'Supplier': {description: 'Buck Farm', email: 'sales@buck.com'}, quantity: 54})


Mongo Import:
we can do bulk document insert using mongoimport

mongoimport --db dbName --collection collectionName --file FileAbsolutePath

mongoimport --db travel --collection flights --file /home/leela/Desktop/flights.json

Exercise:

1. Get list of all places that you fly from Leed Bradforad Airport(airport code LBA)
db.flights.find({'origin.code': 'LBA'}, {'destination.city': true})

2. Remove all flights where airline active attribute is false.
> db.flights.remove({'airline.active': false})
WriteResult({ "nRemoved" : 874 })

3. change the name of the airport in both origin and destination attribute where the code is ICT - new name is 'Wichita Dwight D.Eisenhower National Airport'
> db.flights.update({'origin.code': 'ICT'}, {$set: {'origin.name': 'Wichita Dwight D.Eisenhower National Airport'}}, {multi: true})
WriteResult({ "nMatched" : 20, "nUpserted" : 0, "nModified" : 20 })
> 
> db.flights.update({'destination.code': 'ICT'}, {$set: {'destination.name': 'Wichita Dwight D.Eisenhower National Airport'}}, {multi: true})
WriteResult({ "nMatched" : 20, "nUpserted" : 0, "nModified" : 20 })

Mongo Operators:
In Mongo all operators are preceeded with '$'

Query Operator
1. Comparision Operators
$gt	greater than
$gte	greater than or equal to
$lt	less than
$lte	less than or equal to
$ne	not equal to 
$in	in
$nin	not in
$eq	equals

$eq is the default operator that will be applied.

Find teachers whose age is <=30
db.teachers.find({age: {$lte: 30} })

find only women teachers.
db.teachers.find({title: {$ne: "Mr"} })

Find teachers who teach French
db.teachers.find({subjects: "french" })

Here subjects is an array but mongo will intercept automatically.

db.teachers.find({firstName: {$in: ["David", "Davina"]} })
db.teachers.find({firstName: {$nin: ["David", "Davina"]} })


Regular expression to find teachers whose firstname starts with Dav
db.teachers.find({firstName: /Dav/ })

2. Logical Operators
$or	match on either condition
$and	both conditions are true
$nor	both conditions are false
$not	condition is false


Only in Logical Operators operator will come first whereas in other operators fieldNames will come first.

syntax .find(operator: [{document1} , {document2}])

Teachers First Name starts with Dav and age should be <=40
db.teachers.find({$and: [{firstName: /Dav/}, {age: {$lte: 40}}]})

3. Update Operators
$set	updates or inserts an attribute
$currentDate	set the value to todays date
$inc	increments the value
$mul	multiplies the value
$rename	changes attribute name
$unset	removes an attribute
$min	set the value to the lower of its current value and a value we provide
$max	set the value to the higher of its current value and a value we provide

update age to 34
db.teachers.update({_id:10}, {$set: {age: 34}})

update age to 34 and surName to Purple
db.teachers.update({_id:10}, {$set: {age: 34, surName: "Purple"}})

removes age attribute
db.teachers.update({_id:10}, {$unset: {age: true}})

removes age and surName attribute
db.teachers.update({_id:10}, {$unset: {age: true, surName: true}})


Here in unset we are specifying value as true and this can be completly a random value, which is provided to make the document balance with key value pair.
true seems to make sense as it telling we want to unset age.

Increments all teachers age by 2
db.teachers.update({}, {$inc: {age: 2}}, {multi: true})


Multiplies age by 1.5 when age >=40
db.teachers.update({age: {$gte: 40}}, {$mul: {age: 1.5}}, {multi:true})

Rename age attribute to currentAge
db.teachers.update({}, {$rename: {age: "currentAge"}}, {multi: true})


Exercise 3: 

db.flights.find({$and: [{"origin.code": "LHR"}, {"destination.code": "ORD"}] }, {"airline.name": true})

db.flights.find({$and: [{"origin.code": "AMS"}, {$and: [{"destination.country": "Ireland"}, {"destination.code": {$ne: "ORK"}}] } ]})

(or)

db.flights.find({$and: [{"origin.code": "AMS"}, {"destination.country": "Ireland"}, {"destination.code": {$ne: "ORK"}}] })


Mongo and Javascript:
Mongo choosen Javascript as a query language, means everything that you are executing is a Javascript funciton on the Javascript objects.

use school
school is a javascript object

db.teachers.find()
we are using javascript find function on javascript object teachers.


*** The commands we are executing in command line can go into javascript function also.

function foo() {
	var cursor = db.teacher.find()
}



function calcBirthYear(age) {
	//currentyear is a local variable 
	//if var is not there then it will be global variable.
	var currentyear = new Date().getFullYear();
	return (currentyear - age);
}


function addBirthYear() {
	var cursor = db.teachers.find();
	while(cursor.hasNext()) {
		var currentRow = cursor.next();
		var age = currentRow.age;
		var birthYear = calcBirthYear(age);
		var id = currentRow._id;
		db.teachers.update({_id: id}, {$set: {birthYear: birthYear} });
	}
}


(or)

function addBirthYear() {
	db.teachers.find().forEach(
		function (currentRow) {
			var age = currentRow.age;
			var birthYear = calcBirthYear(age);
			var id = currentRow._id;
			db.teachers.update({_id: id}, {$set: {birthYear: birthYear} });
		}
	)
}


To enter a javascript function in a text editor then we need to set below variable
var EDITOR="full path to text editor executable"

To open text editor
edit calcBirthYear

Saving JavaScript Functions in memory
db.system.js.save({_id: "functionname", value: function(parameters) {//implementation} })

eg: db.system.js.save({_id: "calcBirthYear", value: calcBirthYear })

Here the method save is identical to insert except save will do an update if that _id value exist, so save is "insert or update"



*** If you login to mongo shell then all functions will not be available until below command is executed.
db.loadServerScripts()




Exercise
function findDetinations(airportCode) {
	db.flights.find({"origin.code": airportCode}).forEach(
		function (document) {
			print(document.destination.code);
		}
	)

}

Aggregation:
GroupBy:

syntax:
db.collection.group({
	key: {attribute1: true,attribute2: true},
	initial: {var1: value, var2: value},
	reduce: function(currentRow,groupDocument) {
				//implementation
			},
	cond: {query}		
})

Here key will have Grouping Attributes (means the fields which we will mention in group by clause)
initial will have initial values for the Grouping Attributes
reduce this is a javascript function which will have logic for the calculation attributes.
currentRow is the document in the result set.
groupDocument is the one where we will calcualte the calculation attribute and put back the results in the right document.Its mongo responbility to give update right record
suppose we are calculating total amount grouping party and type 
so when we are calculating total amount its mongo responsibility that this total amount should go to right document which have combination of party and type.
cond here the condition for the incoming document.
suppose we want to calculating this grouping for todays transaction, then we can specify that in the cond, it is just like a filter.


db.transactions.group({
	key: {party: true, type: true},
	initial: {totalAmount: 0},
	reduce: function(currentRow,groupDocument) {
				groupDocument.totalAmount += currentRow.amount
			}
})



db.transactions.group({
	key: {party: true, type: true},
	initial: {totalAmount: 0, 
			  numberOfTransactions: 0,
			  averageAmount: 0	
			 },
	reduce: function(currentRow,groupDocument) {
				groupDocument.totalAmount += currentRow.amount;
				groupDocument.numberOfTransactions++;
			},
	finalize: function(finalGroupDocument) {
				finalGroupDocument.averageAmount = finalGroupDocument.totalAmount/finalGroupDocument.numberOfTransactions;
			  }		
})

Here finalize will be executed after executing group by.


Exercise:
{
	key: {"origin.code": true},
	cond: {"destination.country": 'Spain'},
	initial: {destinations: [],
			  numberOfDestinations: 0},
	reduce: function(currentDoc, groupDoc) {
				if(groupDoc.destinations.indexOf(currentDoc.destination.code) == -1) {
					groupDoc.destinations.push(currentDoc.destination.code);
				}
			},
	finalize: function(finalDoc) {
				 finalDoc.destinations.sort();
				 finalDoc.numberOfDestinations=finalDoc.destinations.length
			  }		
}

 Profiling:
db.collection.find(query).explain("executionStats")

db.flights.find({_id: 101}).explain("executionStats")

{
	"queryPlanner" : {
		"plannerVersion" : 1,
		"namespace" : "travel.flights",
		"indexFilterSet" : false,
		"parsedQuery" : {
			"_id" : {
				"$eq" : 101
			}
		},
		"winningPlan" : {
			"stage" : "IDHACK"
		},
		"rejectedPlans" : [ ]
	},
	"executionStats" : {
		"executionSuccess" : true,
		"nReturned" : 1,
		"executionTimeMillis" : 4,
		"totalKeysExamined" : 1,
		"totalDocsExamined" : 1,
		"executionStages" : {
			"stage" : "IDHACK",
			"nReturned" : 1,
			"executionTimeMillisEstimate" : 0,
			"works" : 2,
			"advanced" : 1,
			"needTime" : 0,
			"needYield" : 0,
			"saveState" : 0,
			"restoreState" : 0,
			"isEOF" : 1,
			"invalidates" : 0,
			"keysExamined" : 1,
			"docsExamined" : 1
		}
	},
	"serverInfo" : {
		"host" : "leela-VirtualBox",
		"port" : 27017,
		"version" : "4.0.9",
		"gitVersion" : "fc525e2d9b0e4bceff5c2201457e564362909765"
	},
	"ok" : 1
}

Here the stage is IDHACK, as _id will always be indexed in mongo by default.
Since it is indexed mongo can go to the right document,so
"totalKeysExamined" : 1,
"totalDocsExamined" : 1,


db.flights.find({"origin.code": "JFK"}).explain("executionStats")

{
	"queryPlanner" : {
		"plannerVersion" : 1,
		"namespace" : "travel.flights",
		"indexFilterSet" : false,
		"parsedQuery" : {
			"origin.code" : {
				"$eq" : "JFK"
			}
		},
		"winningPlan" : {
			"stage" : "COLLSCAN",
			"filter" : {
				"origin.code" : {
					"$eq" : "JFK"
				}
			},
			"direction" : "forward"
		},
		"rejectedPlans" : [ ]
	},
	"executionStats" : {
		"executionSuccess" : true,
		"nReturned" : 455,
		"executionTimeMillis" : 230,
		"totalKeysExamined" : 0,
		"totalDocsExamined" : 65674,
		"executionStages" : {
			"stage" : "COLLSCAN",
			"filter" : {
				"origin.code" : {
					"$eq" : "JFK"
				}
			},
			"nReturned" : 455,
			"executionTimeMillisEstimate" : 195,
			"works" : 65676,
			"advanced" : 455,
			"needTime" : 65220,
			"needYield" : 0,
			"saveState" : 515,
			"restoreState" : 515,
			"isEOF" : 1,
			"invalidates" : 0,
			"direction" : "forward",
			"docsExamined" : 65674
		}
	},
	"serverInfo" : {
		"host" : "leela-VirtualBox",
		"port" : 27017,
		"version" : "4.0.9",
		"gitVersion" : "fc525e2d9b0e4bceff5c2201457e564362909765"
	},
	"ok" : 1
}

Here for this query stage:"COLLSCAN" as we are not searching based on indexes, so this will result in scanning all collections inside a database which results in poor performing query.
"nReturned" : 455,
"executionTimeMillis" : 230,
"totalKeysExamined" : 0,
"totalDocsExamined" : 65674,

To improve performance we can create an index on origin.code

db.flights.createIndex({"origin.code":1})
Here the value can be either 1(ascending) or -1(descending)
Both values doesn't have performance impact if only one key is present as mongo can scan forward and reverse direction.
But this will have performance impact on compound index.
For Custom Index stage will be "IXSCAN"

{
	"queryPlanner" : {
		"plannerVersion" : 1,
		"namespace" : "travel.flights",
		"indexFilterSet" : false,
		"parsedQuery" : {
			"origin.code" : {
				"$eq" : "JFK"
			}
		},
		"winningPlan" : {
			"stage" : "FETCH",
			"inputStage" : {
				"stage" : "IXSCAN",
				"keyPattern" : {
					"origin.code" : 1
				},
				"indexName" : "origin.code_1",
				"isMultiKey" : false,
				"multiKeyPaths" : {
					"origin.code" : [ ]
				},
				"isUnique" : false,
				"isSparse" : false,
				"isPartial" : false,
				"indexVersion" : 2,
				"direction" : "forward",
				"indexBounds" : {
					"origin.code" : [
						"[\"JFK\", \"JFK\"]"
					]
				}
			}
		},
		"rejectedPlans" : [ ]
	},
	"executionStats" : {
		"executionSuccess" : true,
		"nReturned" : 455,
		"executionTimeMillis" : 17,
		"totalKeysExamined" : 455,
		"totalDocsExamined" : 455,
		"executionStages" : {
			"stage" : "FETCH",
			"nReturned" : 455,
			"executionTimeMillisEstimate" : 0,
			"works" : 456,
			"advanced" : 455,
			"needTime" : 0,
			"needYield" : 0,
			"saveState" : 3,
			"restoreState" : 3,
			"isEOF" : 1,
			"invalidates" : 0,
			"docsExamined" : 455,
			"alreadyHasObj" : 0,
			"inputStage" : {
				"stage" : "IXSCAN",
				"nReturned" : 455,
				"executionTimeMillisEstimate" : 0,
				"works" : 456,
				"advanced" : 455,
				"needTime" : 0,
				"needYield" : 0,
				"saveState" : 3,
				"restoreState" : 3,
				"isEOF" : 1,
				"invalidates" : 0,
				"keyPattern" : {
					"origin.code" : 1
				},
				"indexName" : "origin.code_1",
				"isMultiKey" : false,
				"multiKeyPaths" : {
					"origin.code" : [ ]
				},
				"isUnique" : false,
				"isSparse" : false,
				"isPartial" : false,
				"indexVersion" : 2,
				"direction" : "forward",
				"indexBounds" : {
					"origin.code" : [
						"[\"JFK\", \"JFK\"]"
					]
				},
				"keysExamined" : 455,
				"seeks" : 1,
				"dupsTested" : 0,
				"dupsDropped" : 0,
				"seenInvalidated" : 0
			}
		}
	},
	"serverInfo" : {
		"host" : "leela-VirtualBox",
		"port" : 27017,
		"version" : "4.0.9",
		"gitVersion" : "fc525e2d9b0e4bceff5c2201457e564362909765"
	},
	"ok" : 1
}


Now we can see performance improvement as it is not scanning entire collection.
"nReturned" : 455,
"executionTimeMillis" : 17,
"totalKeysExamined" : 455,
"totalDocsExamined" : 455,


Indexes will speed up find query, but inserts, updates and deletes will be slower as it has to update indexes also.


Creating Unique Index:
db.teachers.createIndex({surName: 1}, {unique: true})

If we try to duplicate surName then below error will occur.
db.teachers.insert({_id:11, surName: 'Red'})
WriteResult({
	"nInserted" : 0,
	"writeError" : {
		"code" : 11000,
		"errmsg" : "E11000 duplicate key error collection: school.teachers index: surName_1 dup key: { : \"Red\" }"
	}
})

> db.teachers.insert({_id:11, surName: 'Cyan'})
this will run.


Sorting:
db.collection.find(query).sort(sortQuery)

Usually sort is applied on indexed attributes,
If we apply sort to Non Index attributes we will get poor performance.

SortQuery parameters will have 1(ascending) or -1(descending)


db.teachers.find({}, {surName:true}).sort({surName: 1})
db.teachers.find({}, {surName:true}).sort({surName: -1})

Getting Indexes that exist in a collection
db.collection.getIndexes()

db.teachers.getIndexes()
[
	{
		"v" : 2,
		"key" : {
			"_id" : 1
		},
		"name" : "_id_",
		"ns" : "school.teachers"
	},
	{
		"v" : 2,
		"unique" : true,
		"key" : {
			"surName" : 1
		},
		"name" : "surName_1",
		"ns" : "school.teachers"
	}
]

We can give a name to the index during creation using options parameter if not mongo will generate a index name appending index name with sort order


Droping Index:
db.collection.dropIndex("Index Name")

db.teachers.dropIndex("surName_1")

Compound Index:
db.flights.createIndex({"origin.code":1, "destination.code":1})

This query will run faster
db.flights.find({}, {"origin.code": true, "destination.code": true}).sort({"origin.code":1, "destination.code":1}).explain("executionStats")

This query will run slower
db.flights.find({}, {"origin.code": true, "destination.code": true}).sort({"origin.code":1, "destination.code":-1}).explain("executionStats") 


For Compound Index Sort Order matters.



Replication:
Replication is duplicating data across instances in the cluster, so that if one server goes down we will have our data safe.
Instances in the cluster will be under a Replication Set, and in the Replication Set ONLY one node can be PRIMARY and the rest are SECONDARY nodes.
** Mongo Client can execute commands ONLY in the primary node. Connecing via programming will resolve the primary node automatically from the list of instances that you provided, but through terminal we need to find which instance is primary.


To run a 3 instances in the same server we create 3 different data paths and run it on different ports.
/data/rep1
/data/rep2
/data/rep3


mongod --replSet setName --dbpath pathToDataFolder --port portNumber

mongod --replSet travelReplicaSet --dbpath /data/rep1 --port 27001
mongod --replSet travelReplicaSet --dbpath /data/rep2 --port 27002
mongod --replSet travelReplicaSet --dbpath /data/rep3 --port 27003

Below command should execute in mongo client of Primary Node.
** But this will not SET the Replication, for replicaiton to happen we need to execute below command.
rs.initiate({_id: replicationSetName, members: [{_id:1, host: "server:port"}, {_id:1, host: "server:port"}, {_id:1, host: "server:port"}]})
here rs means replication set

rs.initiate({_id: "travelReplicaSet", members: [{_id: 1, host: "localhost:27001"}, {_id: 2, host: "localhost:27002"}, {_id: 3, host: "localhost:27003"}]})


Connection via mongo client:
mongo localhost:27001
Here we need to connect to PRIMARY instance in the cluster and here i am assuming 27001 is the primary.

Importing collection to a primary instance
mongoimport --db travel --collection flights --file flights.json --host localhost:27001


What happens when a command is issued to primary instance via mongo client:
This command will execute on the primary server and at the same time this command will be copied into a file called "OpLog"(Operation Log). This OpLog will be synced to another instances in the cluster at a regular interval, so that other instances in the cluster can execute same commands on their datasets.

So in the case of failure in primary instances one of the other instances can become primary.

Rules to Elect an instance as primary:
1. The new Primary MUST be able to see majority of the servers.(Here majority means more that half of number of instances eg: 9 instances in the cluster then 5 is majority)
2. The new Primary MUST have latest OpLog file of all the current secondaries.

Suppose in case of network partition into 2 groups then mongo instances will know about the reduction of nodes in the cluster and will do a peer to peer communication to become a primary instance.
one group have 4 instances and other group as 5 instances
Here Majority is 5 (n/2+1)
then Group1 instances are not eligible for Primary because every instance will see only 4 instances which is not majority
one of the instance in Group2 can become primary as every instance will see 5 instances, so now instance with Latest OpLog file will become primary.

*** In the Mongo Cluster ONLY one Primary will exist, During this failure primary instance in Group1 will change its state from Primary to secondary to see who will elect as primary.
This is needed to avoid Data inconsitency, because if there are 2 Primaries then client can execute in either of the 2 instances which will create data inconsistency problems.

*** In Mongo there is NO Master to determine Primary Instance, election will happen on Peer to Peer communication.


Suppose if we have 8 node cluster and network partition happen 
with Group1 with 4 instances
Group2 with 4 instances
then no instance will become primary and entire mongo instance will be down. This is needed so that we don't create 2 primaries (1 in Group1 and 1 in Group2) which creates data inconsistency Problems.
*** Mongo Prefers Data Consistency over Availability in this case.
SO IT IS HIGHLY RECOMMENDED TO HAVE A ODD NUMBER OF INSTANCES IN THE CLUSTER TO AVOID THESE TIE BREAKS.

** During the election as primary no client can execute queries.

Mongo Client via api will resolve Primary instance automatically from the list of instances specified.
In the shell we need to find out the primary instance.
Below are the commands which will be helpful.
rs.status()
rs.isMaster()

** To see the Replication Status on other nodes we can use below command
rs.printSlaveReplicationInfo()

You CANNOT execute commands on Mongo Secondary instances.
*** If you are ok to execute Read only Queries and ok to see stale data then we need execute below command before executing queries.
rs.slaveOk()


Arbiter:
Arbiter is a light weight node(low RAM, less CPU and Low HDD) which is used to resolve a tie break as this node in the cluster will make the cluster number as Odd.
*** Arbiter will never be elected as Primary Node, Mongo will take care of this.

When this is useful?
If you want to run a 2 node cluster then we can use Arbiter to resolve tie break.


Sharding:
Sharding is a process of splitting dataset across multiple instances.

why sharding?
1. For Good Performance we need to be able to fit working set and indexs into RAM
2. Network I/O may not be sufficient on a single instance to handle/process large loads.

Mongo is designed to make this sharding easy, It is not a mandatory option that we need to have, but replication is a mandatory for a production grade system.
In Mongo we can start with single instance and we can monitor the performance it we need have a problem in future then we can shard at that point.

Shard Keys:
It is a key used by mongo to determine which of the servers that the document we insert is stored.So shard key is very important.
We need to carefully select attribute as a key in a document that can be used to decide which of the shards each document should be stored on.
** This shard key is an attribute that should exist on each and every document in a collection.
This key should be immutable. This value should never be updated.
This key may not be unique.
If we consider flights db example a good shard will be origin.code

*** Generally Documents with the same shard key will always be stored on the same shard.
Mongo distributes documents across shards automatically
During this distribution Mongo uses keyspace to determine how they should be split. Here keyspace means a range of Keys
If we consider our flights example with origin.code as shard key then keyspace can be
{[A-L] [K-Z]} for two shards.
This Keyspace is managed by another Server called "Configuration  Server" when it will maintain the index of shard keys and which shard is having these keys.
We have another server called Entrypoint Server, it is just like a gateway to the shards.
When a client interacts with the database(eg: for a database insert) it will talk to entrypoint server and entrypoint server will contact configuration server to determine from which shard the data should be read/inserted.
*** Mongo Clients always need to interact with Entry point server to talk to the database which inturn talks to the shards using configuration server


Shard setup
cd data
mkdir shard1
mkdir shard2
mkdir shardConfig
chown ec2-user shard*

Starting Shard Servers
mongod --shardsvr --dbpath path to data folder --port portNumber

mongod --shardsvr --dbpath /data/shard1 --port 27001
mongod --shardsvr --dbpath /data/shard2 --port 27002

Starting Configuration Server
mongod --configsvr --dbpath path to data folder --port portNumber

mongod --configsvr --dbpath /data/shardConfig --port 27003

Starting Entrypoint server
mongos --configdb serverName:port --port portNumber

mongos --configdb localhost:27003 --port 27004

mongos --configdb localhost:27003 --port 27004
With these steps we just estabilished a link between Entrypoint server and Config server
But we need to tell to Config Server where our shards are:

For this log into entrypoint server using mongo client
mongo localhost:27004

use admin
db.runCommand({addShard: "serverName:port"})

db.runCommand({addShard: "localhost:27001"})
db.runCommand({addShard: "localhost:27002"})

With the above setup Mongo will not Enable sharding as we need explictly specify for which database and for which collections we need enable sharding
this make sense as we need sharding for some of the collections only.

db.runCommand({enableSharding: "database"})
db.runCommand({shardCollection: "database.collection", key: {attribute: 1}})
Here 1 means true.

db.runCommand({enableSharding: "travel"})
db.runCommand({shardCollection: "travel.flights", key: {"origin.code": 1}})


Now we can import flights collection via entrypoint server
mongoimport --host localhost:27004 --db travel --collection flights --file fligts.json 


Rules for selecting good shard key:
1. Use a Wide Value Range
If you choose a field with only  a limited range of values you will get a badly performing shard cluster.
This is because documents with same shard key will be stored in same shard, so if we have more documents for a few shard keys that will make the shard overloaded 
eg: a shard key has only 2 possible values
then these will be stored in 2 different shards, but what if we need to scale to 6 shards for getting performance 
It is no use as always the first 2 shards only will get filled and 4 shards are ignored, So Horizontal Scaling will be achieved.

2. Beware of hotspots
In our example we choosen origin.code as shard key
if 90% of origin code is LHR then 90% of documents are on the same shard which will make shard overloaded. This is called hotspot.
so look for a field with a fairly even distribution of possible values.

3. What about IDs?
Either own or autogenerated ids 
Autogenerted ids are also in Incremental basis that are based on timestamps.

*** The Problem with incremental Ids are they will ALWAYS be WRITTEN to SAME SHARD instance.

suppose if you have 2 shards
and you inserted 10 documents so it will take the keyspace as [1, 10]
so each shard will get 5 docs
but when we add another doc with id as 11 then mongo will say ok this id is on higher side so it will insert in shard2
when we add another doc with id as 12 then mongo will say ok this id is on higher side so it will insert in shard2
when we add another doc with id as 13 then mongo will say ok this id is on higher side so it will insert in shard2
when we add another doc with id as 14 then mongo will say ok this id is on higher side so it will insert in shard2

This looks like a hotspot but it is not as it will eventually get balanced because of Shard Collection Balancing.

After some time Shard Collection Balancer notice that there shard keys having huge range and make the shards balanced, but the downside is all Write operations will go to same shard.
This means one shard is overloaded with CPU/IO

From Mongo 2.6 Mongo solved this incremental problem using Hashed Shard Keys.
In this process each key will run through hash algorithm and the resulting hash is used to determine the shard.
This approach is still a problem with same shard key which results in hotspot because same key generates same hash key. since above scenario is incremental(which are different) it will generate different hashes.

THis Hashing ensures
a. Wide range of values.
b. Random distribution of hash values.

To make the keys hashed we need to use below command
db.runCommand({shardCollection: "database.collection", key: {attribute: "hashed"}})

db.runCommand({shardCollection: "travel.flights", key: {"_id": "hashed"}})

4. Consider Queries
So we should always use id as shard key?
Depends
If you use hashed ids then the query which is running needs to run across different shards and need to merge the results before giving to the client which is huge task.
So we should use shard key that you are going to use as a target for your queries so that queries can execute on single shard and merge step can be skipped which results in better performance.

*** Mongo will Create an Index for Shard Key.


Mongo with Java
MongoClient - connection pool to the server
MongoDatabase - reference to the database
MongoCollection - reference to the collection
Document - reference to BSON document.
