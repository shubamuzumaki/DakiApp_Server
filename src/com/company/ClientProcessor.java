package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
//            System.out.println("Client Accepted");
            DataInputStream din = new DataInputStream(client.getInputStream());
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());

            String request = din.readUTF();
//            System.out.println(request);
            int header = RequestProcessor.getHeader(request);
            String username;
            String password;
            String friendId;
            String response;
            String userObjId;

            switch (header)
            {
//---------------------------------------------------------------------------------
                case CommunicationFlags.SIGN_UP:
                    username = RequestProcessor.getUserName(request);
                    password = RequestProcessor.getUserPassword(request);
                    boolean signedUp = DBManager.signUpUser(username,password);
                    if(signedUp)
                    {
                        response = ResponseGenerator.getSignUpSuccessfulResponse();
                        System.out.println("Signed up successfully");
                    }
                    else
                    {
                        response = ResponseGenerator.getSignUpFailedResponse();
                        System.out.println("Sign up Failed");
                    }
                    dout.writeUTF(response);
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.LOGIN:
                    username = RequestProcessor.getUserName(request);
                    password = RequestProcessor.getUserPassword(request);
                    boolean loggedIn = DBManager.logInUser(username,password);
                    if(loggedIn)
                    {
                        //@todo manage exception from getUserId
                        userObjId = DBManager.getUserId(username,password);
                        response = ResponseGenerator.getLoginSuccessfulResponse(userObjId);
                        System.out.println("Login Successfull:" + username);
                    }
                    else
                    {
                        response = (ResponseGenerator.getLoginFailedResponse("Login Failed Due to Some Reasons"));
                        System.out.println("Login Failed");
                    }
                    dout.writeUTF(response);
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.GET_FRIEND_LIST:
                    userObjId = RequestProcessor.getUserObjId(request);
                    response = ResponseGenerator.getFriendListResponse(DBManager.getFriendList(userObjId));
                    dout.writeUTF(response);
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.ADD_FRIEND_REQUEST:
                    userObjId = RequestProcessor.getUserObjId(request);
                    friendId = RequestProcessor.getFriendId(request);
                    boolean isFriendAdded = DBManager.addFriend(userObjId,friendId);

                    if(isFriendAdded)
                        response = ResponseGenerator.getAddFriendSuccessfulResponse(friendId);
                    else
                        response = ResponseGenerator.getAddFriendFailedResponse("Err due to rainy days");

                    dout.writeUTF(response);
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.SEND_MSG:
                    userObjId = RequestProcessor.getUserObjId(request);
                    friendId = RequestProcessor.getFriendId(request);
                    String message = RequestProcessor.getMessage(request);
                    if(DBManager.sendMessage(userObjId,friendId,message))
                        System.out.println("Message Sent");
                    else
                        System.out.println("Failed to deliver Message");
                    break;
//---------------------------------------------------------------------------------
                case CommunicationFlags.GET_MESSAGES:
                    userObjId = RequestProcessor.getUserObjId(request);
                    friendId = RequestProcessor.getFriendId(request);
                    //@TODO add try catch
                    ArrayList<String> messageList = DBManager.getMessageList(userObjId,friendId);

                    dout.writeUTF(ResponseGenerator.getMessageList(messageList));
                    break;
            }//switch
        }//try
        catch (Exception e)
        {
            System.out.println("err in run-ClientProcessor: "+e);
        }
    }
}
