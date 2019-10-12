package com.company;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.*;

import java.util.*;

interface DBInfo
{
    String database = "chatDatabase";
    String usersCol = "user";
    String friendChatCol = "friendChat";
    String groupChatCol = "groupChat";

    interface user
    {
        String username = "username";
        String password = "password";
        String friends = "friends";
        String friend_name = "friendName";
        String groups = "groups";
        String g_name = "groupName";
        String chat_id = "chat_id";

    }
    interface friendChat
    {
        String messages = "messages";
        String m_sender = "sender";
        String m_data = "data";
        String m_date = "DateOfMessage";
        String m_tag = "tag";
        String m_read = "read";
    }
    interface groupChat
    {
        String count = "count";
        String members = "members";
        String messages = "messages";
        String m_sender = "sender";
        String m_data = "data";
        String m_date = "DateOfMessage";
        String m_tag = "tag";
        String m_read = "read";
    }

    interface tags
    {
        String info = "info";
        String message = "message";
    }
}

//manages database
class DBManager
{
    private static MongoClient mongo = MongoClients.create();
    private static MongoDatabase db = mongo.getDatabase(DBInfo.database);
    private static MongoCollection<Document> user = db.getCollection(DBInfo.usersCol);
    private static MongoCollection<Document> friendChat = db.getCollection(DBInfo.friendChatCol);
    private static MongoCollection<Document> groupChat = db.getCollection(DBInfo.groupChatCol);

    static
    {
        System.out.println("Database Initialized...");
        System.out.println("Users Count: "+ user.countDocuments());
    }

    static String signUpUser(String username,String password)throws Exception
    {
        Document clientDoc = user.find(Filters.eq(DBInfo.user.username,username)).first();
        if(clientDoc != null)
            throw new Exception("Username already Exists");

        clientDoc = new Document(DBInfo.user.username,username)
                         .append(DBInfo.user.password,password);
        user.insertOne(clientDoc);

        clientDoc = user.find(clientDoc).first();
        assert clientDoc != null:"value toh null nikili yaara";
        return String.valueOf(clientDoc.getObjectId("_id"));
    }

    static String logInUser(String username,String password) throws Exception
    {
        Document clientDoc = user.find(Filters.eq(DBInfo.user.username,username.toLowerCase())).first();
        if(clientDoc == null)
            throw new Exception("user not registered");

        if(password.equals((String)clientDoc.get(DBInfo.user.password)))
            return String.valueOf(clientDoc.getObjectId("_id"));
        throw new Exception("Invalid Password");
    }

    static boolean addFriend(String userId, String friendName)
    {
        ObjectId userObjId = new ObjectId(userId);
        friendName = friendName.toLowerCase();

        Document userDoc = user.find(Filters.eq("_id",userObjId)).first();
        Document friendDoc = user.find(Filters.eq(DBInfo.user.username,friendName)).first();

        if(friendDoc == null || userDoc == null)
            return false;

        ArrayList<Document> friends = (ArrayList<Document>)userDoc.get(DBInfo.user.friends);
        if(friends !=null)
        {
            for(Document d:friends)
                if(friendName.equals(d.get(DBInfo.user.friend_name)))
                    return false;
        }

        String username = String.valueOf(userDoc.get(DBInfo.user.username));
        if(username.equals(friendName))
            return false;

        //create freindChat Document
        ObjectId friendObjId = friendDoc.getObjectId("_id");
        ObjectId chatId = new ObjectId();
        friendChat.insertOne(new Document("_id",chatId));

        //update friendlist of user and friend
        Document d = new Document(DBInfo.user.chat_id,chatId);
        user.updateOne(Filters.eq("_id",friendObjId), new Document(
                "$push",
                new Document(DBInfo.user.friends,
                        new Document(d).append(DBInfo.user.friend_name,username))
        ));
        user.updateOne(userDoc,new Document(
                "$push",
                 new Document(DBInfo.user.friends ,
                         new Document(d).append(DBInfo.user.friend_name,friendName))
        ));

        return true;

    }

    static JSONArray getFriendList(String userObjId) throws Exception
    {
        Document userDoc = user.find(Filters.eq("_id",new ObjectId(userObjId))).first();
        if(userDoc == null)
            throw new IllegalArgumentException("User is not registered with app");

        JSONArray friendsArr = new JSONArray();
        List<Document> friends = (ArrayList<Document>)userDoc.get(DBInfo.user.friends);
        if(friends == null)
            return friendsArr;

        for(Document d:friends)
            friendsArr.add(String.valueOf(d.get(DBInfo.user.friend_name)));
        return friendsArr;
    }

    static boolean sendMessage(String userId, String friendName , String data, String tag)
    {
        friendName = friendName.toLowerCase();
        ObjectId userObjId = new ObjectId(userId);

        Document userDoc = user.find(Filters.eq("_id", userObjId)).first();
        if (userDoc == null)
            return false;

        String username = (String)userDoc.get(DBInfo.user.username);
        ObjectId chatId;
        try
        {
            chatId = getChatId(friendName,userDoc,DBInfo.user.friends);
        }
        catch(Exception e)
        {
            return false;
        }

        Document message = new Document(DBInfo.friendChat.m_sender,username)
                                .append(DBInfo.friendChat.m_data,data)
                                .append(DBInfo.friendChat.m_tag,tag)
                                .append(DBInfo.friendChat.m_read,false)
                                .append(DBInfo.friendChat.m_date,new Date());

        Document query = new Document(
                "$push",
                new Document(DBInfo.friendChat.messages,message)
        );

        friendChat.updateOne(Filters.eq("_id",chatId), query);
        return true;
    }

    static JSONArray getMessageList(String userObjId,String friendName) throws Exception
    {
        friendName = friendName.toLowerCase();
        Document userDoc = user.find(Filters.eq("_id",new ObjectId(userObjId))).first();
        if(userDoc == null)
            throw new IllegalArgumentException("User is not registered with app");

        JSONArray messages = new JSONArray();
        ObjectId chatId = getChatId(friendName,userDoc,DBInfo.user.friends);

        Document chat = friendChat.find(Filters.eq("_id",chatId)).first();
        List<Document> messageList = (List<Document>)chat.get(DBInfo.friendChat.messages);
        if(messageList == null)
            return messages;

        messages.addAll(messageList);

        return messages;

    }

    private static ObjectId getChatId(String friendName,Document userDoc,String arrField)throws Exception
    {
        List<Document> friends = (ArrayList<Document>)userDoc.get(arrField);
        if(friends == null)
            throw new Exception("No friend Exists.");

        ObjectId chatId = null;
        for(Document d:friends)
        {
            if(friendName.equalsIgnoreCase(d.get(DBInfo.user.friend_name).toString()))
            {
                chatId = d.getObjectId(DBInfo.user.chat_id);
                break;
            }
        }

        if(chatId == null)
            throw new Exception("no Valid chatId found for " + friendName);
        return chatId;
    }
}


//        ArrayList<String> friendList = new ArrayList<>();
//        for(Document d:(List<Document>)clientDoc.get("friendList"))
//            friendList.add(d.getString("name"));
//
//        return friendList;

//    static String getUserId(String username , String password) throws IllegalArgumentException
//    {
//        Document clientDoc = userInfo.find(Filters.eq("username",username.toLowerCase())).first();
//        if(clientDoc == null)
//            throw new IllegalArgumentException("user is not registered with app");
//
//        if(password.equals(clientDoc.get("password")))
//            return clientDoc.getObjectId("_id").toString();
//
//        throw new IllegalArgumentException("Invalid Password.Please Provide Valid Password");
//    }

//
//        ObjectId friendObjId = friendDoc.getObjectId("_id");
//        //update the clientDoc
//        Document update = new Document(
//                "$addToSet",
//                new Document("friendList",
//                        new Document("name",friendId).append("objId",friendObjId)));
//        user.updateOne(clientDoc,update);

//        ObjectId friendObjId = null;
//        //@TODO use friendList to send message don't send directly
//        for(Document d:(List<Document>)userDoc.get("friendList"))
//        {
//            if(friendName.equals(d.getString("name")))
//            {
//                friendObjId = (ObjectId) d.get("objId");
//                break;
//            }
//        }
//
//        if(friendObjId == null) return false;
//
//        Document friendDoc = user.find(Filters.eq("_id",friendObjId)).first();
//
//        Document query = new Document(
//                "$push",
//                new Document("dropbox",
//                        new Document("sender",username).append("message",message))
//        );
//
//        user.updateOne(friendDoc,query);
//        ArrayList<String> messageList = new ArrayList<>();
//        Object obj = clientDoc.get("dropbox");
//        List<Document> listDoc = null;
//        if(obj instanceof List)
//            listDoc = (List<Document>)obj;
//        if(obj == null)
//            return messageList;
//        for(Document d: listDoc)
//        {
//            String sender = (String) d.get("sender");
//            String message = (String)d.get("message");
//
//            if(friendId.equals(sender)) {
//                messageList.add(message);
//            }
//        }
//
//        //remove messages from the database
//        Document query = new Document(
//                "$pull",
//                new Document("dropbox",
//                        new Document("sender",friendId))
//        );
//
//        userInfo.updateOne(clientDoc,query);
//        return messageList;