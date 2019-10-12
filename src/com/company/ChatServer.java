package com.company;

import java.io.IOException;
import java.net.*;

public class ChatServer implements Runnable
{
    private ServerSocket serverSocket;

    ChatServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                new ClientProcessor(serverSocket.accept());
            }
            catch (Exception e)
            {
                System.out.println("err in run-ChatServer"+e);
            }
        }
    }

}
