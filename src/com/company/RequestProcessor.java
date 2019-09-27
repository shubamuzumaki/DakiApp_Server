package com.company;

public class RequestProcessor
{
    public static int getHeader(String request)
    {
        String header = request.split(CommunicationFlags.SEPARATOR_1)[0];
        return Integer.parseInt(header);
    }

    public static String getUserName(String request)
    {
        return request.split(CommunicationFlags.SEPARATOR_1)[1]
                      .split(CommunicationFlags.SEPARATOR_2)[0];
    }

    public static String getUserPassword(String request)
    {
        return request.split(CommunicationFlags.SEPARATOR_1)[1]
                      .split(CommunicationFlags.SEPARATOR_2)[1];
    }

    public static String getUserObjId(String request)
    {
        return "";
    }
}
