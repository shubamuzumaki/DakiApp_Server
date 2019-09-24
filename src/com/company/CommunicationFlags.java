package com.company;

public interface CommunicationFlags
{
    String SEPARATOR_1 = "~";
    String SEPARATOR_2 = "*&^%$#@!";
    int LOGIN = 1;
    int SIGN_UP = 2;
    int LOGIN_SUCCESSFULL = 3;
    int LOGIN_FAILED = 4;
    int SIGN_UP_SUCCESSFULL = 5;
    int SIGN_UP_FAILED = 6;
    int CONNECTION_FAILED = 7;
    int SEND_MSG = 8;
}
