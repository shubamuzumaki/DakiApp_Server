package com.company;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ReqProcessor
{
    private JSONObject request = new JSONObject();

    public ReqProcessor(String request)throws ParseException
    {
        this.request = (JSONObject)(new JSONParser().parse(request));
    }

    int getHeader()throws NullPointerException
    {
        Long l = (long)request.get(ComFlags.fields.header);
        return l.intValue();
    }

    String getUsername()throws NullPointerException
    {
        return (String)request.get(ComFlags.fields.username);
    }

    String getUserPassword()throws NullPointerException
    {
        return (String)request.get(ComFlags.fields.password);
    }

    String getUserId()throws NullPointerException
    {
        return (String)request.get(ComFlags.fields._id);
    }

    String getNewFriend()throws NullPointerException
    {
        return (String)request.get(ComFlags.fields.newFriend);
    }

    @Override
    public String toString()
    {
        return request.toJSONString();
    }
//    public static int getHeader(String request)
//    {
//        String header = request.split(ComFlags.SEPARATOR_1)[0];
//        return Integer.parseInt(header);
//    }
//
//    public static String getUserName(String request)
//    {
//        return request.split(ComFlags.SEPARATOR_1)[1]
//                      .split(ComFlags.SEPARATOR_2)[0];
//    }
//
//    public static String getUserPassword(String request)
//    {
//        return request.split(ComFlags.SEPARATOR_1)[1]
//                      .split(ComFlags.SEPARATOR_2)[1];
//    }
//
//    public static String getUserObjId(String request)
//    {
//        return request.split(ComFlags.SEPARATOR_1)[1]
//                .split(ComFlags.SEPARATOR_2)[0];
//    }
//
//    public static String getFriendId(String request)
//    {
//        return request.split(ComFlags.SEPARATOR_1)[1]
//                .split(ComFlags.SEPARATOR_2)[1]
//                .split(ComFlags.SEPARATOR_3)[0];
//    }
//
//    public static String getMessage(String request)
//    {
//        //4~7845125df4f5@shubamÆhello there
//        return request.split(ComFlags.SEPARATOR_1)[1]
//                .split(ComFlags.SEPARATOR_2)[1]
//                .split(ComFlags.SEPARATOR_3)[1];
//    }
}
