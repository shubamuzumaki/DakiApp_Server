package com.company;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client
{
    private String username;
    private String password;
    //     <FriendName,objId>
    private HashMap<String,String> friendMap;
    //      <sender,data>
    private HashMap<String,String> dropBox;

    public Client(String username,String password)
    {
        this.username = username.toLowerCase();
        this.password = password;
    }

    public Document toDocument()
    {
        List<Document> docFriendList = MapToDocList(friendMap,"name","obj_id");
        List<Document> docDropBoxList = MapToDocList(dropBox,"sender","data");

        Document myDoc = new Document("username",username);
        myDoc.append("password",password);

        myDoc.append("friendList",docFriendList);
        myDoc.append("dropBox",docDropBoxList);
        return myDoc;
    }

    private ArrayList<Document> MapToDocList(HashMap<String,String> map, String field1, String field2)
    {
        ArrayList<Document> docFriendList = new ArrayList<>();
        if(map == null)
            return docFriendList;

        for(Map.Entry<String,String> e: map.entrySet())
        {
            Document newDoc = new Document();
            newDoc.append(field1,e.getKey());
            newDoc.append(field2,e.getValue());
            docFriendList.add(newDoc);
        }
        return docFriendList;
    }
}
