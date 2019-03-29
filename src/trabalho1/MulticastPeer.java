/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class MulticastPeer {

    public static void main(String args[]) {
        try {
            // args give message contents and destination multicast group (e.g. "228.5.6.7")
            ConexaoMulticast c = new ConexaoMulticast("228.10.11.12", 6789);
            // Inicia o listener, que fica aguardando conexoes
            Listener l = new Listener(c);
            l.start();

            System.out.println("Informe seu nome!");
            Scanner sc1 = new Scanner(System.in);
            String nome = sc1.next();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Jogador j = new Jogador(c);

            JSONObject id = new JSONObject();
            id.put("nome", nome);
            id.put("datahora", timestamp);
            id.put("message", "hello");
            j.enviaMensagem(id);

        } catch (IOException ex) {
            Logger.getLogger(MulticastPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Pooling extends Thread {

    private String mestre;
    private InetAddress group;

    Pooling(String mestre, InetAddress group) {
        this.mestre = mestre;
        this.group = group;
    }

    @Override
    public void run() {
        MulticastSocket s = null;
        try {
            s = new MulticastSocket(6789);
        } catch (IOException ex) {
            Logger.getLogger(Pooling.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.joinGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(Pooling.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {

            JSONObject alive = new JSONObject();
            alive.put("nome", mestre);
            alive.put("message", "alive");
            String alive_str = alive.toString(1);
            byte[] pooling = alive_str.getBytes();
            DatagramPacket messageOut = new DatagramPacket(pooling, pooling.length, group, 6789);
            try {
                s.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(Pooling.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pooling.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}

//class Listener extends Thread {
//
//    public void run() {
//        MulticastSocket s = null;
//
//        InetAddress group = null;
//        try {
//            group = InetAddress.getByName("228.10.11.12");
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            s = new MulticastSocket(6789);
//        } catch (IOException ex) {
//            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            s.joinGroup(group);
//        } catch (IOException ex) {
//            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        ArrayList<String> jogadores = new ArrayList<>();
//        int nro_jogadores = 0;
//        String mestre = null;
//        boolean mestre_alive = false;
//        boolean start = false;
//        Date last_master_on = null;
//
//        while (true) {	// get messages from others in group
//            byte[] buffer = new byte[1000];
//            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
//            try {
//                s.receive(messageIn);
//            } catch (IOException ex) {
//                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            //System.out.println("Received:" + new String(messageIn.getData()));
//            String dados = new String(messageIn.getData());
//            JSONObject cast_data = new JSONObject(dados);
//            System.out.println("Tipo da mensagem: " + cast_data.getString("message") + " enviada por " + cast_data.getString("nome"));
//
//            if (cast_data.getString("message").equals("hello")) {
//                jogadores.add(cast_data.getString("nome"));
//                nro_jogadores++;
//
//                System.out.println(cast_data.getString("nome") + " entrou");
//                if (nro_jogadores == 3 && mestre == null) {
//                    chooseMaster(jogadores, group, s);
//                    start = true;
//                }
//            }
//
//            //Identifica mensagens de Alive, buscando o mestre
//            if (cast_data.getString("message").equals("alive")) {
//                //System.out.println(cast_data.getString("nome") + " is alive");
//                if (cast_data.getString("nome").equals(mestre)) {
//                    mestre_alive = true;
//                    last_master_on = new Date();
//                }
//            }
//
//            //Identica a mensagem que informa quem é o mestre
//            if (cast_data.getString("message").equals("info_mestre")) {
//                if (mestre == null) {
//                    mestre = cast_data.getString("nome");
//                    System.out.println("Atenção a todos, o mestre é o " + mestre);
//                    last_master_on = new Date();
//                    Runnable c = new CheckMaster(last_master_on, group, mestre);
//                    new Thread(c).start();
//                    if (mestre.equals(meu_nome)) {
//                        Runnable r = new Pooling(meu_nome, group);
//                        new Thread(r).start();
//                    }
//                }
//            }
//
//            if (cast_data.getString("message").equals("master_off")) {
//                jogadores.remove(cast_data.getString("nome"));
//                chooseMaster(jogadores, group, s);
//            }
//
//        }
//    }
//    public void chooseMaster(ArrayList<String> jogadores, InetAddress group, MulticastSocket s) {
//        //Elegendo o mestre
//        String mestre = null;
//        Random rand = new Random();
//        mestre = jogadores.get(jogadores.size() - 1);
//        System.out.println("O mestre é o " + mestre);
//        //Envia para os demais sobre quem é o mestre
//        JSONObject inf_mestre = new JSONObject();
//        inf_mestre.put("nome", mestre);
//        inf_mestre.put("message", "info_mestre");
//        String info_mestre = inf_mestre.toString(1);
//        byte[] send_info_mestre = info_mestre.getBytes();
//        DatagramPacket messageOut = new DatagramPacket(send_info_mestre, send_info_mestre.length, group, 6789);
//
//        try {
//            s.send(messageOut);
//        } catch (IOException ex) {
//            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
class CheckMaster extends Thread {

    private Date last_master_on;
    private InetAddress group;
    private String nome;

    CheckMaster(Date last_master_on, InetAddress group, String nome) {
        this.last_master_on = last_master_on;
        this.group = group;
        this.nome = nome;
    }

    @Override
    public void run() {
        MulticastSocket s = null;
        try {
            s = new MulticastSocket(6789);
        } catch (IOException ex) {
            Logger.getLogger(CheckMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.joinGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(CheckMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(nome + " " + last_master_on);
        while (true) {
            Date hora_agora = new Date();
            JSONObject master_check = new JSONObject();
            long diff = getDateDiff(last_master_on, hora_agora, TimeUnit.SECONDS);
            System.out.println(diff);
            master_check.put("nome", nome);

            if (diff < 10) {
                master_check.put("message", "master_on");
            } else {
                master_check.put("message", "master_off");
            }

            String master_check_str = master_check.toString(1);
            //System.out.println(master_check_str);
            byte[] master_check_bytes = master_check_str.getBytes();
            DatagramPacket messageOut = new DatagramPacket(master_check_bytes, master_check_bytes.length, group, 6789);
            try {
                s.send(messageOut);
            } catch (IOException ex) {
                Logger.getLogger(CheckMaster.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckMaster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Get a diff between two dates
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
}
