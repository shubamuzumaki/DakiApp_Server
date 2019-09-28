package com.company;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
//            new ChatServer(9887);
//            System.out.println("Server up...");
            testFunction();
        }
        catch(Exception e)
        {
            System.out.println("Err in Main: "+e);
        }
    }

    private static void testFunction()
    {
        //testing the database
        var test = DBManager.addFriend(DBManager.getUserId("rap","1243"),"gaurav");
        if(test)
            System.out.println("Database Updated");
        else
            System.out.println("Database failed to Update");
    }
}