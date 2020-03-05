package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server implements Runnable {

    private ServerSocket serverSocket;
    private final int PORT = 1234;
    private Vector<String> users = new Vector<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(5);




    public void main(String[] args) {

        Server server = new Server();
        server.handleClient();


    }

    public void run() {
        /*do {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            Thread thread = new Thread(new ClientSender());
            executorService.execute(thread);
        }while(!Thread.interrupted());*/




    }

    public void handleClient(){

        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {

                Socket link = serverSocket.accept();
                System.out.println("Connected");

                BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);
                String message, response;
                message = input.readLine();

                System.out.println(message);


                if (message.startsWith("JOIN")) {
                    String userName = message.replace(",", "").split(" ")[1];
                    users.add(userName);
                    response = "J_OK";
                    output.println(response + users);

                }

                if (message.startsWith("DATA")){
                    String messageToEveryone = message.split(":")[1];
                    output.println(messageToEveryone);

                }
                /*Thread thread = new Thread(new Client());
                executorService.execute(thread);*/
            }

        } catch(IOException ioEx)        {
            ioEx.printStackTrace();
        }
    }
       /* } finally {
            try {
                System.out.println("\n* Closing connection...*");
                link.close();
            } catch (IOException ioEx) {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }*/

    public ServerSocket getServerSocket() {
        return serverSocket;
    }



    public int getPORT() {
        return PORT;
    }

}
