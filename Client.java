package com.example;
import java.io.*;
import java.net.*;

public class Client {

    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket miSocket;
    BufferedReader tastiera;
    String stringaUtente;
    String strRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDaServer;
    

    public Socket connetti(){
        System.out.println(" CLIENT partito in esecuzione...");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            miSocket = new Socket(nomeServer,portaServer);

            outVersoServer = new DataOutputStream(miSocket.getOutputStream());
            inDaServer = new BufferedReader(new InputStreamReader(miSocket.getInputStream()));

        } catch (UnknownHostException e) {
            // TODO: handle exception
            System.err.println("host sconosciuto");
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("errore durante la connesione ");
            System.exit(1);
        }
        return miSocket;
    }

    public void comunica(){
        for(;;)
        try {
            System.out.println(" ...  inserisci il numero da indovinare al server:"+'\n');
            stringaUtente = tastiera.readLine();

            System.out.println(" invio il numero e attendo...");
            outVersoServer.writeBytes(stringaUtente+'\n');

            strRicevutaDalServer = inDaServer.readLine();
            System.out.println(" la risposta del server :"+'\n'+strRicevutaDalServer);

            if(strRicevutaDalServer.equals("=> Numero indovinato: "+stringaUtente)){
            System.out.println(" Client:termina elaborazione e chiude la connessione");
            miSocket.close();
            break;
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            System.out.println("errore durante la connesione con il server!");
            System.exit(1);
        }
    }
    
}

