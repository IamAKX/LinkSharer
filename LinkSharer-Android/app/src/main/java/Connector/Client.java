package Connector;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by akash on 13/2/17.
 */

public class Client {
    static Socket client;
    static String IP;
    private static PrintWriter printwriter;
    static int PORT;

    public Client(String IP) {
        this.IP = IP;
        PORT = 4444;
    }

    public void sendRequest(final String data)
    {
        new Thread(new Runnable()
        {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try
                {
                    client=new Socket(IP , PORT);
                    printwriter=new PrintWriter(client.getOutputStream());
                    printwriter.write(data);
                    printwriter.flush();
                    printwriter.close();
                    client.close();
                }catch(UnknownHostException e)
                {
                    e.printStackTrace();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }).start();


    }

}