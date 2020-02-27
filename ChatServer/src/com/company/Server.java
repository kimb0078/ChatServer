package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server implements Runnable{

    private static ServerSocket serverSocket;
    private static final int PORT = 1234;
    static Vector<String> users = new Vector<>();


    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void setServerSocket(ServerSocket serverSocket) {
        Server.serverSocket = serverSocket;
    }

    public static int getPORT() {
        return PORT;
    }

    public static void main(String[] args){

        Server server = new Server();
        server.run();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Thread thread = new Thread(new Client());
        executorService.execute(thread);
    }

    public void run(){
        /*do {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            Thread thread = new Thread(new ClientSender());
            executorService.execute(thread);
        }while(!Thread.interrupted());*/

        System.out.println("Opening port...");
        try{
            serverSocket = new ServerSocket(PORT);
        }
        catch(IOException ioEx){
            System.out.println("Unable to attach port!");
            System.exit(1);
        }

        do{
            handleClient();
        }while(true);
    }
    private static void handleClient() {
        Socket link = null;
        try {


            link = serverSocket.accept();

            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(),true);
            String message, response;
            message = input.nextLine().replace(",", " ").replace(":"," ");
            System.out.println(message);



            if (message.contains("JOIN")){
                String userName = message.split(" ")[1];
                users.add(userName);
                response = "J_OK";
                output.println(response + users);



            }


        } catch (IOException ioEx) {

        } finally {
            try {
                System.out.println("\n* Closing connection...*");
                link.close();
            } catch (IOException ioEx) {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }
}
