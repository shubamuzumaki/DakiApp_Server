package com.company;

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
        if(DBManager.logInUser("Shubam","8chcter"))
            System.out.println("Shubam Logged in Sucessfully");
        else
            System.out.println("Signing Up Failed");
    }
}