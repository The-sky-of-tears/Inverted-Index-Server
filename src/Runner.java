import java.nio.file.Path;

public class Runner {
    public static void main(String[] args) throws InterruptedException {

        String directory;
        int numOfThreads;

        try {
            directory = args[0];
            numOfThreads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid initial parameters");
            e.printStackTrace();
            return;
        }

        InvertedIndex invertedIndex = new InvertedIndex(Path.of(directory), numOfThreads);
    }
}
