package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    private static boolean isRunning = true;
    private static String userName = null;
    private static final int PORT  = 1234;
    private static final String serverIp = "127.0.0.1";
    static private Socket clientSocket;
    private BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws NullPointerException{


        Thread thread = new Thread(new Client());
        thread.start();




    }

    /*public ClientSender(String userName, InetAddress host, Socket clientSocket){
        this.userName = userName;
        this.host = host;
        this.clientSocket = clientSocket;
    }*/




    public void run() {

        try {
            while (userName == null) {

                System.out.println("Choose a username with 12 or less characters:");
                String s = userEntry.readLine();
                if (s.length() > 12) {
                    System.out.println("Please choose a shorter username!");
                } else {
                    setUserName(s);
                    System.out.println("Your username is " + getUserName());

                }
            }
        }
        catch (IOException ioEx){
            ioEx.printStackTrace();
        }



        accessServer();
    }

    private static void accessServer(){
        Socket link = null;
        Scanner userEntry = new Scanner(System.in);
        try{
            link = new Socket(serverIp,PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter output = new PrintWriter(link.getOutputStream(),true);
            String protocol, response;

            do{
                System.out.println("Enter protocols 'JOIN' or 'DATA'.");
                protocol = userEntry.next();
                switch (protocol){
                    case("JOIN"):
                        String message = (protocol + " " + userName + ", " + serverIp + ":" + PORT);
                        output.println(message);
                        System.out.println(message);
                        break;
                    case("DATA"):
                        System.out.println("What would you like to tell everyone?");
                        String messageForEveryone = userEntry.next();
                        output.println(protocol + getUserName() + ":" + messageForEveryone);
                        break;
                }
                response = input.readLine();
                System.out.println("Server: " + response);
            }while(!protocol.equals("QUIT"));
        }
        catch (IOException ioEx){
            ioEx.printStackTrace();
        }
        finally {
            try{
                System.out.println("*Closing connection!*");
                link.close();
            }
            catch (IOException ioEx){
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }

        }
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Client.userName = userName;
    }
}
