package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

//manages database
public class DBManager
{
    static MongoClient mongo = MongoClients.create();
    static MongoDatabase db = mongo.getDatabase("chatDatabase");
    static MongoCollection<Document> userInfo = db.getCollection("userInfo");

    static
    {
        System.out.println("Database Initialized...");
        System.out.println("Users Count: "+ userInfo.countDocuments());
    }

    //this will signup the user if possible
    public static boolean signUpUser(String username,String password)
    {
        //first find the user in the database
        Document clientDoc = userInfo.find(Filters.eq("username",username)).first();
        if(clientDoc != null)
            return false;

        clientDoc = new Client(username,password).toDocument();
        userInfo.insertOne(clientDoc);
        return true;
    }

    //this will check whether
    public static boolean logInUser(String username,String password)
    {
        //find the user in the database
        Document clientDoc = userInfo.find(Filters.eq("username",username.toLowerCase())).first();
        if(clientDoc == null)
            return false;

        if(password.equals((String)clientDoc.get("password")))
            return true;
        return false;
    }

    public static String getUserId(String username , String password) throws IllegalArgumentException
    {
        Document clientDoc = userInfo.find(Filters.eq("username",username.toLowerCase())).first();
        if(clientDoc == null)
            throw new IllegalArgumentException("username is not registered with app");

        if(password.equals(clientDoc.get("password")))
            return clientDoc.getObjectId("_id").toString();

        throw new IllegalArgumentException("Invalid Password.Please Provide Valid Password");
    }
}
