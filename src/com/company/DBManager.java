package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.sun.javadoc.Doc;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

//manages database
public class DBManager
{
    private static MongoClient mongo = MongoClients.create();
    private static MongoDatabase db = mongo.getDatabase("chatDatabase");
    private static MongoCollection<Document> userInfo = db.getCollection("userInfo");

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

    public static String[] getFriendList(String userObjId) throws IllegalArgumentException
    {
        Document clientDoc = userInfo.find(Filters.eq("_id",userObjId)).first();
        if(clientDoc == null)
            throw new IllegalArgumentException("User is not registered with app");

        Object obj = clientDoc.get("friendList");
        try
        {

        }
        catch(Exception e)
        {
            System.out.println("err: DBManager:getFriendList: "+e);
            throw new IllegalArgumentException("Some err occured");
        }
        return null;
    }

    public static boolean addFriend(String userId, String friendId)
    {
        friendId = friendId.toLowerCase();
        ObjectId userObjId = new ObjectId(userId);

        Document clientDoc = userInfo.find(Filters.eq("_id",userObjId)).first();
        Document friendDoc = userInfo.find(Filters.eq("username",friendId)).first();
        if(friendDoc == null || clientDoc == null)
            return false;

        ObjectId friendObjId = friendDoc.getObjectId("_id");
        //update the clientDoc
        Document update = new Document(
                "$addToSet",
                new Document("friendList",
                        new Document("name",friendId).append("objId",friendObjId)));
        userInfo.updateOne(clientDoc,update);
        return true;
    }
}
