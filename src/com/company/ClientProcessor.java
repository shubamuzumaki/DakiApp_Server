package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.ParseException;

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
        DataInputStream din;
        DataOutputStream dout = null;
        ReqProcessor request;
        try
        {
            System.out.print("Client Accepted:");
            din = new DataInputStream(client.getInputStream());
            dout = new DataOutputStream(client.getOutputStream());
            request = new ReqProcessor(din.readUTF());
            int header = (int)request.getHeader();
            String response="";
            System.out.println(request);

            switch(header)
            {
                case ComFlags.request.SIGN_UP:
                case ComFlags.request.LOGIN:
                    response = signUpLoginUser(header,request.getUsername(),request.getUserPassword());
                    break;
                case ComFlags.request.ADD_FRIEND:
                    response = addFriend(request.getUserId(),request.getNewFriend());
                    break;
                case ComFlags.request.FRIEND_LIST:
                    response = getFriendList(request.getUserId());
                    break;
                case ComFlags.request.MESSAGES:
                    break;
            }

            dout.writeUTF(response);

        }//try
        catch(ParseException pe)
        {
            pe.printStackTrace();
            try{dout.writeUTF("Invalid Request(Not Proper JSON)");}catch(Exception e){}
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Request type is Invalid(Not JSON):");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        catch (NullPointerException npe)
        {
            npe.printStackTrace();
            try{dout.writeUTF("Invalid Request(Filed Not Found)");}catch(Exception e){}
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("Invalid Field in request(Field Not Found)");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try{dout.writeUTF("Invalid Request");}catch(Exception ine){}
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("err in run-ClientProcessor: ");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }//run

    private String signUpLoginUser(long flag,String username,String password) throws Exception
    {
        try
        {
            String userObjId="";
            if(flag == ComFlags.request.SIGN_UP)
                userObjId = DBManager.signUpUser(username,password);
            else if(flag == ComFlags.request.LOGIN)
                userObjId = DBManager.logInUser(username,password);

            return ResponseGen.getInstance().AuthenticationSuccess(userObjId);
        }
        catch(Exception e)
        {
            return ResponseGen.getInstance().OperationFailed(e.getMessage());
        }
    }

    private String addFriend(String userId,String newFriend)
    {
        if(DBManager.addFriend(userId,newFriend))
            return ResponseGen.getInstance().OperationSuccess("Friend Added Successfully");
        return ResponseGen.getInstance().OperationFailed("Failed To Add Friend");
    }

    private String getFriendList(String userId)
    {
        try
        {
            return ResponseGen.getInstance().FriendList(DBManager.getFriendList(userId));
        }
        catch (Exception e)
        {
            return ResponseGen.getInstance().OperationFailed(e.getMessage());
        }
    }
}


///////////////////////////////////////////////////// [BACKUP] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//            case ComFlags.SIGN_UP:
//                    boolean signedUp = DBManager.signUpUser(username,password);
//                    if(signedUp)
//                    {
//                        response = ResponseGenerator.getSignUpSuccessfulResponse();
//                        System.out.println("Signed up successfully");
//                    }
//                    else
//                    {
//                        response =
//                        System.out.println("Sign up Failed");
//                    }
//
//                break;
//            case ComFlags.LOGIN:
//                break;
//        }

//
//if(header == ComFlags.request.SIGN_UP || header == ComFlags.request.LOGIN)
//        {
//        String username = request.getUsername();
//        String password = request.getUserPassword();
//        response = signUpLoginUser(header,username,password);
//        System.out.println(response);
//        }


//            switch (header)
//            {
////---------------------------------------------------------------------------------
//                case ComFlags.SIGN_UP:
//                    username = RequestProcessor.getUserName(request);
//                    password = RequestProcessor.getUserPassword(request);
//                    response = signUpLoginUser(header,username,password);
//                    dout.writeUTF(response);
//                    break;
////---------------------------------------------------------------------------------
//                case ComFlags.LOGIN:
//                    username = RequestProcessor.getUserName(request);
//                    password = RequestProcessor.getUserPassword(request);
//                    boolean loggedIn = DBManager.logInUser(username,password);
//                    if(loggedIn)
//                    {
//                        //@todo manage exception from getUserId
//                        userObjId = DBManager.getUserId(username,password);
//                        response = ResponseGen.getLoginSuccessfulResponse(userObjId);
//                        System.out.println("Login Successfull:" + username);
//                    }
//                    else
//                    {
//                        response = (ResponseGen.getLoginFailedResponse("Login Failed Due to Some Reasons"));
//                        System.out.println("Login Failed");
//                    }
//                    dout.writeUTF(response);
//                    break;
////---------------------------------------------------------------------------------
//                case ComFlags.GET_FRIEND_LIST:
//                    userObjId = RequestProcessor.getUserObjId(request);
//                    response = ResponseGen.getFriendListResponse(DBManager.getFriendList(userObjId));
//                    dout.writeUTF(response);
//                    break;
////---------------------------------------------------------------------------------
//                case ComFlags.ADD_FRIEND_REQUEST:
//                    userObjId = RequestProcessor.getUserObjId(request);
//                    friendId = RequestProcessor.getFriendId(request);
//                    boolean isFriendAdded = DBManager.addFriend(userObjId,friendId);
//
//                    if(isFriendAdded)
//                        response = ResponseGen.getAddFriendSuccessfulResponse(friendId);
//                    else
//                        response = ResponseGen.getAddFriendFailedResponse("Err due to rainy days");
//
//                    dout.writeUTF(response);
//                    break;
////---------------------------------------------------------------------------------
//                case ComFlags.SEND_MSG:
//                    userObjId = RequestProcessor.getUserObjId(request);
//                    friendId = RequestProcessor.getFriendId(request);
//                    String message = RequestProcessor.getMessage(request);
//                    if(DBManager.sendMessage(userObjId,friendId,message))
//                        System.out.println("Message Sent");
//                    else
//                        System.out.println("Failed to deliver Message");
//                    break;
////---------------------------------------------------------------------------------
//                case ComFlags.GET_MESSAGES:
//                    userObjId = RequestProcessor.getUserObjId(request);
//                    friendId = RequestProcessor.getFriendId(request);
//                    //@TODO add try catch
//                    ArrayList<String> messageList = DBManager.getMessageList(userObjId,friendId);
//
//                    dout.writeUTF(ResponseGen.getMessageList(messageList));
//
//                    break;
//            }//switch