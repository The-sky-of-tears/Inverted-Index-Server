import server.Server;

import java.nio.file.Path;

public class Runner {
    public static void main(String[] args) throws InterruptedException {

        try {
            int serverPort = Integer.parseInt(args[0]);
            Path directory = Path.of(args[1]);
            int numOfThreads = Integer.parseInt(args[2]);
            Server server = new Server(serverPort, directory, numOfThreads);
        } catch (NumberFormatException e) {
            System.err.println("Invalid initial parameters");
            e.printStackTrace();
        }
    }
}