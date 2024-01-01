import java.nio.file.Path;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        InvertedIndex invertedIndex = new InvertedIndex(Path.of("data"), 16);
        System.out.println(invertedIndex.find("Hello cruel world"));

    }
}
