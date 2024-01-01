import index.InvertedIndex;
import server.Server;

import java.nio.file.Path;

public class Runner {
    public static void main(String[] args) throws InterruptedException {

        Path directory;
        int numOfThreads;

        try {
            directory = Path.of(args[0]);
            numOfThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid initial parameters");
            e.printStackTrace();
            return;
        }

        Server server = new Server(13666, directory, numOfThreads);
    }
}
