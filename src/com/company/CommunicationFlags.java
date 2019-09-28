package com.company;

public interface CommunicationFlags
{
    String SEPARATOR_1 = "~";
    String SEPARATOR_2 = "@";
    String SEPARATOR_FRIEND_LIST = "-";
    int LOGIN = 1;
    int SIGN_UP = 2;
    int LOGIN_SUCCESSFULL = 3;
    int LOGIN_FAILED = 4;
    int SIGN_UP_SUCCESSFULL = 5;
    int SIGN_UP_FAILED = 6;
    int CONNECTION_FAILED = 7;
    int SEND_MSG = 8;
    int ERR_INVALID_ARGUMENTS = 9;
    int GET_FRIEND_LIST = 10;
    int FRIEND_LIST = 11;
    int ADD_FRIEND_REQUEST = 12;
    int ADD_FRIEND_RESPONSE_SUCCESSFULL = 13;
    int ADD_FRIEND_RESPONSE_FAILED = 14;
}
