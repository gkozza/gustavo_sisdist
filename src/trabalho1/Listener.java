/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author gustavo.santos
 */
public class Listener extends Thread {

    public ConexaoMulticast connect;

    public Listener(ConexaoMulticast c) {
        connect = c;
    }

    @Override
    public void run() {

        MulticastSocket s = connect.getMulticast_socket();

        while (true) {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                s.receive(messageIn);
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
            String dados = new String(messageIn.getData());
            System.out.println(dados);
        }
    }
}
