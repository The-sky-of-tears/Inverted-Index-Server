package server;

import index.InvertedIndex;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private ServerSocket serverSocket;
    private InvertedIndex invertedIndex;
    private final ThreadPoolExecutor serverWorkers;

    public Server(int port, Path directory, int numOfThreads) {

        invertedIndex = new InvertedIndex(directory, numOfThreads);
        serverWorkers = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is working and waiting for clients");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Server accepted a new client: " + clientSocket);

                    serverWorkers.submit(() -> {
                        processClientQuery(clientSocket);
                        return null;
                    });

                    System.out.printf("Active users: (%d/%d)\n", serverWorkers.getActiveCount(), numOfThreads);
                    System.out.printf("Users in queue: %d\n", serverWorkers.getQueue().size());

                } catch (IOException e) {
                    System.err.println("Exception occurred while waiting for the clients");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Open server socket exception");
            e.printStackTrace();
        }
    }

    private void processClientQuery(Socket clientSocket) {

        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())){

            while (true) {
                dos.writeUTF("Send text to find its occurrences in the database or \"0\" to exit");
                String text = dis.readUTF();

                System.out.printf("Thread ID %d; Received from client: %s\n",
                        Thread.currentThread().getId(),
                        text);

                if (text.equals("0")) {
                    return;
                }

                try {
                    Set<String> result = invertedIndex.find(text);
                    System.out.printf("Thread ID %d founded %d occurrences\n",
                            Thread.currentThread().getId(),
                            result.size());

                    dos.writeInt(result.size());

                    for (String path : result) {
                        dos.writeUTF(path);
                    }
                } catch (NullPointerException e) {
                    dos.writeInt(0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.printf("Thread ID %d is terminated\n", Thread.currentThread().getId());
        }
    }
}
