package com.company;

import com.mongodb.DB;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new ChatServer(9887);
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
        String objId = DBManager.getUserId("akash","1234");
        try
        {
            ArrayList<String> list =  DBManager.getFriendList(DBManager.getUserId("akash","1234"));
            for(String s:list)
                System.out.println(s);
        }
        catch (Exception e)
        {
            System.out.println("Exception raised"+e);
        }
    }
}