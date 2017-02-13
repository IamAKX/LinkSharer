/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akashapplications;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author akash
 */
public class GetIPAddress {
   
    public String fetch() throws UnknownHostException, SocketException
    {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if(validate(i.toString()))
                    return i.toString().substring(1);
            }
        }
        return InetAddress.getLocalHost().toString();
    }

    private boolean validate(String ip) throws UnknownHostException {
        boolean isValid = false;
        System.out.println(ip);
        int noOfDots = 0;
        for(char c : ip.toCharArray())
            if(c == '.')
                noOfDots++;
        if(noOfDots == 3 && !ip.equalsIgnoreCase(InetAddress.getLocalHost().toString()))
            return true;
        return false;
    }
}
