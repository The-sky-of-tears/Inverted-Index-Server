package server;

import index.InvertedIndex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private ServerSocket serverSocket;
    private InvertedIndex invertedIndex;
    private final ThreadPoolExecutor serverWorkers;

    public Server(int port, Path directory, int numOfThreads) {

        serverWorkers = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
        invertedIndex = new InvertedIndex(directory, numOfThreads);

        try {
            //init serverSocket
            serverSocket = new ServerSocket(port);

            while (true) {
                System.out.println("Server is working and waiting for the clients");

                Socket clientSocket = null;

                try {
                    //listening to the socket, accept new clients
                    clientSocket = serverSocket.accept();
                    System.out.println("Server accepted a new client: " + clientSocket);

                    //process server-client communication in the new thread
                    Socket finalClientSocket = clientSocket;
                    serverWorkers.submit(() -> {
                        processClientQuery(finalClientSocket);
                        return null;
                    });

                    System.out.printf("Active users: (%d/%d)", serverWorkers.getActiveCount(), numOfThreads);

                } catch (IOException e) {
                    //handle exceptions during listening to the socket
                    //close the socket and termite all the processes
                    serverWorkers.shutdownNow();
                    clientSocket.close();
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            //handle exceptions during opening the socket
            e.printStackTrace();
        }
    }

    private void processClientQuery(Socket clientSocket) {
        try {
            Scanner console = new Scanner(System.in);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            dos.writeUTF("Welcome! Send text to find its occurrences in the database");
            String text = dis.readUTF();

            System.out.println(text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
