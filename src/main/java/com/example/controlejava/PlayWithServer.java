package com.example.controlejava;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.IllegalFormatException;
import java.util.Random;

public class PlayWithServer extends Thread{
    /** The guessing game: The server will pick a random number and the clients have
     * to guess it. Once the answer is guessed right, the game stops.**/
    private int ClientNbr; // Nombre de clients
    private int SecretNbre;
    private boolean fin; // flag pour savoir lorsque le client se termine
    private String Winner; // Client gagnant
    public static void main(String[] args) {
        new PlayWithServer().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8081);
            /** Le serveur doit choisir le nombre au hazard **/
            SecretNbre= new Random().nextInt(100); // va stocker un nombre aléatoire de 0 à 100
            System.out.println(SecretNbre);
            System.out.println("The server is about to run");
            /** Le serveur va se connecter aux plusieurs clients et à chaque fois
             * on accepte un client on doit créer un thread **/
            while (true){
                Socket socket=ss.accept(); // acceptation du client
                ++ClientNbr;
                new Communication(socket,ClientNbr).start();  // Communication avec plusieurs clients
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /** Création de la classe Communication qui représente la classe interne
         * et qui permet d'encapsuler les données de la classe externe ServerMutiThread**/
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
                pw.println("Guess the secret number ");
                while(true){
                    String UserRequest= br.readLine();
                    boolean RequestFormat = false; // la valeur par défaut de boolean est false
                    int UserNumber = 0;
                    try {
                        /** Récupération du nombre devinée par le client **/
                        try {
                            // cette instruction doit être dans un bloc try/catch pour traiter le cas lorsque le client saisie une chaîne de caractères
                             UserNumber = Integer.parseInt(UserRequest);
                            RequestFormat = true;
                        }catch (NumberFormatException e){
                            pw.println("Guess again with a number");
                        }
                        if(RequestFormat) {
                            System.out.println("Client number " + ClientNbr + "guessed number " + UserNumber);
                            if (!fin) {
                                // si fin == false càd si on aucun client n'a deviné le nombre secret correct
                                if (UserNumber > SecretNbre)
                                    pw.println("The number you've guessed is higher than the secret number");
                                else if (UserNumber < SecretNbre)
                                    pw.println("The number you've guessed is lower than the secret number");
                                else {
                                    pw.println("Superb! you've guessed the right number");
                                    System.out.println("The winner in this game is no one but the Client number " + ClientNbr + " and his IP is the following: " + s.getRemoteSocketAddress());
                                    fin = true;
                                }
                            } else {
                                pw.println("Game over! the client " + ClientNbr + " has already won \n You can always try next time");
                            }
                        }
                    }catch (IllegalFormatException e){
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
