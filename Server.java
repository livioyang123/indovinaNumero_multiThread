package it.fi.itismeucci;
import java.io.*;
import java.net.*;

public class Server {
    public  void avvioServer(){
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(6789);
            System.out.println("Server in attesa di connessioni...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientHandlerThread = new Thread(new ClientHandler(clientSocket));
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader inDaClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outVersoClient = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            double randomValue = Math.random();
            int numeroCasuale = (int) (randomValue * 1000) + 1;

            String strRicevuta;
            for (;;) {
                try {
                    strRicevuta = inDaClient.readLine();
                    int numeroRicevuto = Integer.parseInt(strRicevuta);

                    if (numeroRicevuto == numeroCasuale) {
                        outVersoClient.writeBytes("=> Numero indovinato: " + numeroCasuale + "\n");
                        System.out.println("Numero indovinato: " + strRicevuta);
                        break;
                    } else {
                        System.out.println("Il numero da indovinare: " + numeroCasuale + "\n");
                        if (numeroCasuale > numeroRicevuto) outVersoClient.writeBytes("Il numero da indovinare è maggiore.\n");
                        if (numeroCasuale < numeroRicevuto) outVersoClient.writeBytes("Il numero da indovinare è minore.\n");
                        System.out.println("Il client non ha indovinato.");
                    }
                } catch (Exception e) {
                    outVersoClient.writeBytes("Inserisci un numero valido.\n");
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}