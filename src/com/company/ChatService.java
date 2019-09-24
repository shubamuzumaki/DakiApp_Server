package com.company;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatService
{
    ArrayList<Socket> clientList = new ArrayList<Socket>();
    final int MAX_CAPACITY;
    int userConnected = 0;

    public ChatService(int maxCapacity)
    {
        MAX_CAPACITY = maxCapacity;
    }

    public boolean addClient(Socket client)
    {
        clientList.add(client);
        userConnected++;
        return true;
    }

    public void sendMessage(Socket client, String data)
    {
        DataOutputStream dout;
        for(Socket s:clientList)
        {
            if(!s.equals(client))
            {
                try
                {
                    dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(data);
                }
                catch(Exception e)
                {
                    System.out.println("Err in sendMessage-chatService");
                }
            }
        }
    }

}
