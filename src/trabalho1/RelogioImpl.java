/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho1;

import java.rmi.RemoteException;

/**
 *
 * @author gustavo.santos
 */
public class RelogioImpl {
    private int hora;
    private int diff;
    
    public RelogioImpl(){
        int i = (int) (Math.random()) * 23;
        int j = (int) (Math.random()) * 59;
        // em minutos
        hora = (i * 60) / j;        
    }
    
    public void setTime(int time) {
        this.hora = time;
    }
    
    public void setDiferenca(int diferenca){
        this.diff = diferenca;
    }
    
    public int getDiferenca(){
        return diff;
    }

    //Metodo igual ao construtor para poder executar varias vezes
    public void random()throws RemoteException {
        int i = (int) (Math.random()) * 23;
        int j = (int) (Math.random()) * 59;
        // em minutos
        hora = (i * 60) / j; 
    }
    
    
    
    
    
    
    
}
