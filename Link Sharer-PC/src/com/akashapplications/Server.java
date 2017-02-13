/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akashapplications;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akash
 */
public class Server {
    int PORT = 4444;
    Socket socket;
    static ServerSocket serverSocket;
    
    public void deployServer()
    {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try {
                        socket = serverSocket.accept();
                        Scanner input = new Scanner(socket.getInputStream());
                        if(input.hasNext())
                        {
                            String s = input.nextLine().trim();
                            String[] data = {s.substring(0,s.indexOf('|')),s.substring(1+s.indexOf('|'))};
                            handleRequest(data);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void handleRequest(String[] data) {
                try {
                    System.out.println(data[0]+" , "+data[1]);
                    switch(data[0])
                    {
                        case "text":
                            StringSelection selection = new StringSelection(data[1]);
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, selection);
                            break;
                        case "link":
                            launchBrowser(data[1]);
                            break;
                    }
                } catch (URISyntaxException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
        }).start();
    }
   
    public void launchBrowser(String url) throws URISyntaxException, IOException
    {
        URI uri;
        if(url.indexOf(" ")==-1)
        {
            uri = new URI(url);
        }
        else
        {
            uri = new URI("http://www.google.com/search?q="+url.replace(" ", "+"));
        }
        if(Desktop.isDesktopSupported())
        {
            Desktop.getDesktop().browse(uri);
        }
    }
}
