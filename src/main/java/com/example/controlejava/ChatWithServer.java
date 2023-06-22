package com.example.controlejava;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatWithServer extends Thread{
    private int ClientNbr; // Nombre de clients
    private List<Communication> clientsconnectés=new ArrayList<Communication>();


    public static void main(String[] args) {
        new ChatWithServer().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8080);
            System.out.println("The server is about to run");
            /** Le serveur va se connecter aux plusieurs clients et à chaque fois
             * on accepte un client on doit créer un thread **/
            while (true){
                Socket socket=ss.accept(); // acceptation du client
                ++ClientNbr;
                Communication newComm= new Communication(socket,ClientNbr); // A chaque fois qu'un client se connecte, il doit être ajouté dans la liste des clients
                clientsconnectés.add(newComm);
                newComm.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /** Création de la classe Communication qui représente la classe interne
         * et qui permet d'encapsuler les données de la classe externe ChatWithServer**/
    }
    public class Communication extends Thread{
        /** Chaque communication prend un socket de client **/
        private Socket s;
        private int ClientNbr;
        Communication(Socket s,int ClientNbr){
            this.s= s;
            this.ClientNbr=ClientNbr;
        }

        @Override
        public void run() {
            // Echange de chaînes de caractères
            try {
                InputStream is= s.getInputStream(); // permet de lire un octet
                InputStreamReader isR= new InputStreamReader(is); // permet de lire un caractère
                BufferedReader br=new BufferedReader(isR); // permet de lire une chaîne de caractères, prend en paramètre le caractère(InputStreamReader)
                OutputStream os= s.getOutputStream();
                System.out.println("Client number "+ClientNbr+" and his IP "+s.getRemoteSocketAddress());
                PrintWriter pw=new PrintWriter(os,true); // il va écrire une seule chaîne à la fois (true=> println && false=> flush(il va écrire le tout à la fois)
                pw.println("You are the client "+ClientNbr);
                pw.println("Start the conversation");
                while(true){
                    String UserRequest= br.readLine();
                    if(UserRequest.contains("=>")){
                        String[] usermessage=UserRequest.split("=>"); // le tableau va stocker le numero du client et le message --exemple: 5=>HI!
                        if(usermessage.length==2){
                            String msg=usermessage[1];
                            int numClient= Integer.parseInt(usermessage[0]);
                            Send(msg,s,numClient); // send to one specific client
                        }
                    }
                    else {
                        Send(UserRequest,s,-1); // send to everyone
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /** méthode qui permet d'envoyer le message saisie par le client à tout le monde sauf the sender lui même**/
        void Send(String UserRequest,Socket socket,int numeroClient) throws IOException {
            for(Communication client : clientsconnectés){
                // Remarque: le client ayant le même socket que le socket en param c'est le client qui a envoyé le message
                if(client.s!=socket) {
                    if(client.ClientNbr==numeroClient || numeroClient==-1) {
                        PrintWriter pw = new PrintWriter(client.s.getOutputStream(), true);
                        pw.println(UserRequest);
                    }
                }
            }
        }
    }
}
