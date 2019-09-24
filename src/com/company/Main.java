package com.company;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new ChatServer(9887);
        }
        catch(Exception e)
        {
            System.out.println("Err in Main: "+e);
        }
    }
}