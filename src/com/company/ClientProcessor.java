package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientProcessor implements Runnable
{
    private Socket client;

    ClientProcessor(Socket client)
    {
        this.client = client;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        //process Client here
        try
        {
            System.out.println("Client Accepted");
            DataInputStream din = new DataInputStream(client.getInputStream());
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());

            String request = din.readUTF();
            System.out.println(request);
            int header = RequestProcessor.getHeader(request);
            String username;
            String password;

            switch (header)
            {
//---------------------------------------------------------------------------------
                case CommunicationFlags.SIGN_UP:
                    username = RequestProcessor.getUserName(request);
                    password = RequestProcessor.getUserPassword(request);
                    boolean signedUp = DBManager.signUpUser(username,password);
                    if(signedUp)
                    {
                        dout.writeUTF(Integer.toString(CommunicationFlags.SIGN_UP_SUCCESSFULL));
                        System.out.println("Signed up successfully");
                    }
                    else
                    {
                        dout.writeUTF(Integer.toString(CommunicationFlags.SIGN_UP_FAILED));
                        System.out.println("Sign up Failed");
                    }
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.LOGIN:
                    username = RequestProcessor.getUserName(request);
                    password = RequestProcessor.getUserPassword(request);
                    boolean loggedIn = DBManager.logInUser(username,password);
                    if(loggedIn)
                    {
                        //@todo manage exception from getUserId
                        String userObjId = DBManager.getUserId(username,password);
                        System.out.println(username+":"+userObjId);
                        String response = CommunicationFlags.LOGIN_SUCCESSFULL + CommunicationFlags.SEPARATOR_1 + userObjId;
                        dout.writeUTF(response);
                        System.out.println("Login Successfull:" + username);
                    }
                    else
                    {
                        dout.writeUTF(Integer.toString(CommunicationFlags.LOGIN_FAILED));
                        System.out.println("Login Failed");
                    }
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.GET_FRIEND_LIST:
                      System.out.println("user Asking for Friend List");
                      String response = CommunicationFlags.FRIEND_LIST + CommunicationFlags.SEPARATOR_1;
                      String[] friendlist = {"Rahul","Rishi","Mall"};
                      response += String.join(CommunicationFlags.SEPARATOR_FRIEND_LIST,friendlist);
                      dout.writeUTF(response);
                    break;
//---------------------------------------------------------------------------------
            }//switch
        }//try
        catch (Exception e)
        {
            System.out.println("err in run-ClientProcessor: "+e);
        }
    }
}
