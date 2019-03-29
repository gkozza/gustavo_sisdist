/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author gustavo.santos
 */
public class ConexaoMulticast {
    
    /**
     *
     * @param IP
     * @param porta
     * @throws UnknownHostException
     * @throws IOException
     */
    public InetAddress group;
    public int porta;
    public MulticastSocket multicast_socket;

    public MulticastSocket getMulticast_socket() {
        return multicast_socket;
    }

    public void setMulticast_socket(MulticastSocket multicast_socket) {
        this.multicast_socket = multicast_socket;
    }
    
  
    public InetAddress getGroup() {
        return group;
    }

    public void setGroup(InetAddress group) {
        this.group = group;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    public ConexaoMulticast(String IP, int porta) throws UnknownHostException, IOException {
        this.porta = porta;
        InetAddress group = InetAddress.getByName(IP);
        this.group = group;
        multicast_socket  = new MulticastSocket(porta);
        multicast_socket.joinGroup(group);
        
    }
}
