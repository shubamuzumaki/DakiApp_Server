package com.company;

interface ComFlags
{
    interface fields
    {
        String header = "header";
        String username = "username";
        String password = "password";
        String _id = "_id";
        String extraMessage = "extraMessage";
        String newFriend = "newFriend";
        String friendsArr = "friendsArr";
    }

    interface request
    {
        int LOGIN = 1;
        int SIGN_UP = 2;
        int FRIEND_LIST = 3;
        int MESSAGES = 4;
        int SEND_MESSAGE = 5;
        int ADD_FRIEND = 6;
    }
    interface response
    {
        int OPERATION_SUCCESSFULL = 101;
        int OPERATION_FAILED = 102;
    }

//    String SEPARATOR_1 = "~";
//    String SEPARATOR_2 = "@";
//    String SEPARATOR_3 = "-";


//    int LOGIN_SUCCESSFULL = 3;
//    int LOGIN_FAILED = 4;
//    int SIGN_UP_SUCCESSFULL = 5;
//    int SIGN_UP_FAILED = 6;
//    int CONNECTION_FAILED = 7;
//    int SEND_MSG = 8;
//    int ERR_INVALID_ARGUMENTS = 9;
//    int GET_FRIEND_LIST = 10;
//    int FRIEND_LIST = 11;
//    int ADD_FRIEND_REQUEST = 12;
//    int ADD_FRIEND_RESPONSE_SUCCESSFULL = 13;
//    int ADD_FRIEND_RESPONSE_FAILED = 14;
//    int GET_MESSAGES = 15;
}
