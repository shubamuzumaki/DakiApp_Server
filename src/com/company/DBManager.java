package com.company;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;
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

    public static ArrayList<String> getFriendList(String userObjId) throws Exception
    {
        Document clientDoc = userInfo.find(Filters.eq("_id",new ObjectId(userObjId))).first();
        if(clientDoc == null)
            throw new IllegalArgumentException("User is not registered with app");

        ArrayList<String> friendList = new ArrayList<>();
        for(Document d:(List<Document>)clientDoc.get("friendList"))
            friendList.add(d.getString("name"));

        return friendList;
    }

    //@TODO raise exception here
    static ArrayList<String> getMessageList(String userObjId,String friendId) //throws Exception
    {
        friendId = friendId.toLowerCase();
        Document clientDoc = userInfo.find(Filters.eq("_id",new ObjectId(userObjId))).first();
//        if(clientDoc == null)
//            throw new IllegalArgumentException("User is not registered with app");

        ArrayList<String> messageList = new ArrayList<>();
        for(Document d: (List<Document>)clientDoc.get("dropbox"))
        {
            String sender = (String) d.get("sender");
            String message = (String)d.get("message");

            if(friendId.equals(sender))
                messageList.add(message);
        }

        //remove messages from the database
        Document query = new Document(
                "$pull",
                new Document("dropbox",
                        new Document("sender",friendId))
        );

        userInfo.updateOne(clientDoc,query);
        return messageList;
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

    static boolean sendMessage(String userId, String friendId , String message)
    {
        friendId = friendId.toLowerCase();
        ObjectId userObjId = new ObjectId(userId);

        Document clientDoc = userInfo.find(Filters.eq("_id", userObjId)).first();
        if (clientDoc == null)
            return false;

        String username = (String)clientDoc.get("username");
        ObjectId friendObjId = null;
        //@TODO use friendList to send message don't send directly
        for(Document d:(List<Document>)clientDoc.get("friendList"))
        {
            if(friendId.equals(d.getString("name")))
            {
                friendObjId = (ObjectId) d.get("objId");
                break;
            }
        }

        if(friendObjId == null) return false;

        Document friendDoc = userInfo.find(Filters.eq("_id",friendObjId)).first();

        Document query = new Document(
                "$push",
                new Document("dropbox",
                        new Document("sender",username).append("message",message))
        );

        userInfo.updateOne(friendDoc,query);
        return true;
    }


}
