package com.company;

import com.mongodb.DB;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new ChatServer(8421);
            System.out.println("Server up...");
//            testFunction();
        }

        catch(Exception e)
        {
            System.out.println("Err in Main: "+e);
        }
    }

    private static void testFunction()
    {
        //testing the database
        String objId = DBManager.getUserId("gaurav","tagotra");
        DBManager.addFriend(objId,"akash");
    }
}