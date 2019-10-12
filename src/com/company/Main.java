package com.company;
public class Main
{
    public static void main(String[] args)
    {
        try
        {
//            new ChatServer(8421);
//            System.out.println("Server up...");
            testFunction();
        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Err in Main: "+e);
        }
    }

    private static void testFunction() throws Exception
    {
//        5d9f81f9d3cff31758b9f01e  :shubam
        var id1 = DBManager.logInUser("shubam","");
//        var id2 = DBManager.signUpUser("rahul","");
        var id = DBManager.getMessageList(id1,"rahul");
        System.out.println("id:" +id);
    }
}