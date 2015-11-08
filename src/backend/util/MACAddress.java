package backend.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by cuong on 11/4/2015.
 */
public class MACAddress {
    public static String getMACAddress() {
        try {
            NetworkInterface net = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte mac[] = net.getHardwareAddress();
            StringBuilder buff = new StringBuilder();

            for (byte i = 0; i < mac.length; i++) {
                buff.append(String.format("%02X%s", mac[i], (i < mac.length-1 ? ":" : "")));
            }

            return buff.toString();

        } catch (SocketException e) {
            e.printStackTrace();
            return null;

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String args[]) {
        System.out.println(getMACAddress());
    }
}
