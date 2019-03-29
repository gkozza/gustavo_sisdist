/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.io.IOException;
import java.net.DatagramPacket;
import org.json.JSONObject;

/**
 *
 * @author gustavo.santos
 */
public class Jogador {
    public int ativo;
    public String nome;
    public String estado;
    public String papel;  
    public ConexaoMulticast conexao;
    public DatagramPacket messageOut;
    
    public Jogador(ConexaoMulticast m){
        ativo = 0;
        estado = "online";
        papel = null;
        conexao = m;
        
        
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
    public void enviaMensagem(JSONObject json) throws IOException{
        String json_str = json.toString(1);
        byte[] mensagem_enviar = json_str.getBytes();
        messageOut = new DatagramPacket(mensagem_enviar, mensagem_enviar.length, conexao.getGroup(), conexao.getPorta());
        conexao.getMulticast_socket().send(messageOut);
    }
 
}
