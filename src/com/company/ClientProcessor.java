package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientProcessor implements Runnable
{
    Socket client;

    public ClientProcessor(Socket client)
    {
        this.client = client;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        //process Client here
        try
        {
            DataInputStream din = new DataInputStream(client.getInputStream());
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());

            String request = din.readUTF();

            int header = RequestProcessor.getHeader(request);

            switch (header)
            {
                case CommunicationFlags.SIGN_UP:

                    break;
            }//switch

            dout.writeUTF(reply);

        }//try
        catch (Exception e)
        {
            System.out.println("err in run-ClientProcessor: "+e);
        }
    }
}
