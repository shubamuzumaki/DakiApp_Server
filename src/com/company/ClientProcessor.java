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

            String username,password;
            String request = din.readUTF();
            int header = RequestProcessor.getHeader(request);

            switch (header)
            {
//---------------------------------------------------------------------------------
                case CommunicationFlags.SIGN_UP:
                    username = din.readUTF();
                    password = din.readUTF();

                    //SignUp in database
                    boolean signedUp = DBManager.signUpUser(username,password);
                    if(signedUp)
                    {
                        //send back successfull status
                        dout.writeUTF(Integer.toString(CommunicationFlags.SIGN_UP_SUCCESSFULL));
                        System.out.println("Signed up successfully");
                    }
                    else
                    {
                        //send back error status
                        dout.writeUTF(Integer.toString(CommunicationFlags.SIGN_UP_FAILED));
                        System.out.println("Sign up Failed");
                    }
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.LOGIN:
                    username = din.readUTF();
                    password = din.readUTF();

                    //log in the database
                    boolean loggedIn = DBManager.logInUser(username,password);
                    if(loggedIn)
                    {
                        dout.writeUTF(Integer.toString(CommunicationFlags.LOGIN_SUCCESSFULL));
                        System.out.println("Login Successfull");
                    }
                    else
                    {
                        dout.writeUTF(Integer.toString(CommunicationFlags.LOGIN_FAILED));
                        System.out.println("Login Failed");
                    }
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
