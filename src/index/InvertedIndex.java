package index;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvertedIndex {

    private final ConcurrentHashMap<String, Set<String>> index;
    private final ThreadPoolExecutor threadPool;

    public InvertedIndex(Path startDirectory, Integer numOfThreads) {

        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
        index = new ConcurrentHashMap<>();

        double start = System.currentTimeMillis();

        buildIndex(startDirectory);
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double end = System.currentTimeMillis();
        System.out.printf("Indexing time: %.3f\n", (end - start) / 1000d);
        System.out.printf("Words indexed: %d\n", index.size());
//        printIndex();
    }

    private void buildIndex(Path directory) {

        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {
            for (Path path : files) {

                if (Files.isDirectory(path)) {
                    buildIndex(path);
                } else {
                    threadPool.submit(() -> {
                        readFromTxtFile(path);
                        return null;
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to read file from directory: " + directory);
            e.printStackTrace();
        }
    }

    private void readFromTxtFile(Path path) {

        try {
            //TODO improve regex if there's extra time
            String[] words = Files
                    .readString(path)
                    .toLowerCase()
                    .split("\\W+");

            for (String word : words) {
                index.putIfAbsent(word, ConcurrentHashMap.newKeySet());
                index.get(word).add(path.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printIndex() {
        for (var entry : index.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public Set<String> find(String phrase) {

        String[] words = phrase
                .toLowerCase()
                .split("\\W+");

        Set<String> result = index.get(words[0]);

        for (String word : words) {
            result.retainAll(index.get(word));
        }

        return result;
    }
}
