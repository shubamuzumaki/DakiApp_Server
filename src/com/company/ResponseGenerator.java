package com.company;

import java.util.ArrayList;

public class ResponseGenerator
{
    public static String getSignUpSuccessfulResponse()
    {
        return Integer.toString(CommunicationFlags.SIGN_UP_SUCCESSFULL);
    }

    public static String getSignUpFailedResponse()
    {
        return Integer.toString(CommunicationFlags.SIGN_UP_FAILED);
    }

    public static String getLoginFailedResponse(String errMessage)
    {
        return CommunicationFlags.LOGIN_FAILED + CommunicationFlags.SEPARATOR_1 + errMessage;
    }

    public static String getLoginSuccessfulResponse(String userObjId)
    {
        return CommunicationFlags.LOGIN_SUCCESSFULL + CommunicationFlags.SEPARATOR_1 + userObjId;
    }

    public static String getAddFriendSuccessfulResponse(String friendId)
    {
        return CommunicationFlags.ADD_FRIEND_RESPONSE_SUCCESSFULL + CommunicationFlags.SEPARATOR_1 + friendId;
    }

    public static String getAddFriendFailedResponse(String errMessage)
    {
        return CommunicationFlags.ADD_FRIEND_RESPONSE_FAILED + CommunicationFlags.SEPARATOR_1 +errMessage;
    }

    public static String getFriendListResponse(ArrayList<String> friendList)
    {
        StringBuffer response = new StringBuffer();
        response.append(CommunicationFlags.FRIEND_LIST)
                .append(CommunicationFlags.SEPARATOR_1);

        for(String s:friendList)
            response.append(s).append(CommunicationFlags.SEPARATOR_FRIEND_LIST);
        return response.toString();
    }
}
