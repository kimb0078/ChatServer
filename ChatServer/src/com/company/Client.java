package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    private static boolean isRunning = true;
    private static String userName = null;
    private static final int PORT  = 1234;
    private static final String serverIp = "127.0.0.1";
    static private Socket clientSocket;
    private Scanner userEntry = new Scanner(System.in);

    public static void main(String[] args) throws NullPointerException{


        Thread thread = new Thread(new Client());
        thread.start();




    }

    /*public ClientSender(String userName, InetAddress host, Socket clientSocket){
        this.userName = userName;
        this.host = host;
        this.clientSocket = clientSocket;
    }*/

    public static boolean isIsRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        Client.isRunning = isRunning;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Client.userName = userName;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Scanner getUserEntry() {
        return userEntry;
    }

    public void setUserEntry(Scanner userEntry) {
        this.userEntry = userEntry;
    }


    public void run() {

        try{
            clientSocket = new Socket(serverIp,PORT);
        }
        catch (IOException ioEx){
            ioEx.printStackTrace();
        }

        while(userName == null) {

            System.out.println("Choose a username with 12 or less characters:");
            String s = userEntry.nextLine();
            if (s.length() > 12) {
                System.out.println("Please choose a shorter username!");
            } else {
                setUserName(s);
                System.out.println("Your username is " + getUserName());

            }
        }

        accessServer();
    }

    private static void accessServer(){
        Socket link = null;
        Scanner userEntry = new Scanner(System.in);
        try{
            link = new Socket(getServerIp(),getPORT());
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(),true);
            String protocol, response;

            do{
                System.out.println("Enter protocols 'JOIN' or 'DATA'.");
                protocol = userEntry.next();
                switch (protocol){
                    case("JOIN"):
                        output.println(protocol + " " + getUserName() + ", " + getServerIp() + ":" + getPORT());
                        System.out.println(input.nextLine());
                        break;
                    case("DATA"):
                        System.out.println("What would you like to tell everyone?");
                        String message = userEntry.nextLine();
                        output.println(protocol + getUserName() + ":" + message);
                        break;
                }
                response = input.nextLine();
                System.out.println("Server: " + response);
            }while(!userEntry.equals("QUIT"));
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
}
