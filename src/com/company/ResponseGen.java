package com.company;

import org.json.simple.*;

class ResponseGen
{
    private ResponseGen(){}
    private static ResponseGen instance = new ResponseGen();

    //@TODO rahul sir about synchronizations
    static ResponseGen getInstance()
    {
        return instance;
    }

    synchronized
    String AuthenticationSuccess(String userObjId)
    {
        JSONObject jo = new JSONObject();
        jo.put(ComFlags.fields.header,ComFlags.response.OPERATION_SUCCESSFULL);
        jo.put(ComFlags.fields._id,userObjId);
        return jo.toJSONString();
    }

    synchronized
    String OperationSuccess(String msg)
    {
        JSONObject jo = new JSONObject();
        jo.put(ComFlags.fields.header,ComFlags.response.OPERATION_SUCCESSFULL);
        jo.put(ComFlags.fields.extraMessage,msg);
        return jo.toJSONString();
    }

    String FriendList(JSONArray friendsArr)
    {
        JSONObject jo = new JSONObject();
        jo.put(ComFlags.fields.header,ComFlags.response.OPERATION_SUCCESSFULL);
        jo.put(ComFlags.fields.friendsArr,friendsArr);
        return jo.toJSONString();
    }
    synchronized
    String OperationFailed(String msg)
    {
        JSONObject jo = new JSONObject();
        jo.put(ComFlags.fields.header,ComFlags.response.OPERATION_FAILED);
        jo.put(ComFlags.fields.extraMessage,msg);
        return jo.toJSONString();
    }
}

//    public static String getSignUpSuccessResponse()
//    {
//        return Integer.toString(ComFlags.SIGN_UP_SUCCESSFULL);
//    }
//
//    public static String getSignUpFailedResponse()
//    {
//        return Integer.toString(ComFlags.SIGN_UP_FAILED);
//    }
//
//    public static String getLoginFailedResponse(String errMessage)
//    {
//        return ComFlags.LOGIN_FAILED + ComFlags.SEPARATOR_1 + errMessage;
//    }
//
//    public static String getLoginSuccessfulResponse(String userObjId)
//    {
//        return ComFlags.LOGIN_SUCCESSFULL + ComFlags.SEPARATOR_1 + userObjId;
//    }
//
//    public static String getAddFriendSuccessfulResponse(String friendId)
//    {
//        return ComFlags.ADD_FRIEND_RESPONSE_SUCCESSFULL + ComFlags.SEPARATOR_1 + friendId;
//    }
//
//    public static String getAddFriendFailedResponse(String errMessage)
//    {
//        return ComFlags.ADD_FRIEND_RESPONSE_FAILED + ComFlags.SEPARATOR_1 +errMessage;
//    }
//
//    public static String getFriendListResponse(ArrayList<String> friendList)
//    {
//        StringBuffer response = new StringBuffer();
//        response.append(ComFlags.FRIEND_LIST)
//                .append(ComFlags.SEPARATOR_1);
//
//        for(String s:friendList)
//            response.append(s).append(ComFlags.SEPARATOR_3);
//        return response.toString();
//    }
//
//    static String getMessageList(ArrayList<String> messageList)
//    {
//        StringBuffer response = new StringBuffer();
//        response.append(ComFlags.GET_MESSAGES)
//                .append(ComFlags.SEPARATOR_1);
//
//        for(String s:messageList)
//            response.append(s).append(ComFlags.SEPARATOR_3);
//
//        return response.toString();
//    }
