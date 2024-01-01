package index;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvertedIndex {

    private final ConcurrentHashMap<String, HashSet<String>> index;
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
        System.out.println((end - start) / 1000d);
        System.out.println(index.size());
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
                index.putIfAbsent(word, new HashSet<>());
                //TODO
                // add to HashSet is not thread safe,
                // probability of exception is extremely low,
                // however, need to work it around
                index.get(word).add(path.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<String> find(String phrase) {

        String[] words = phrase
                .toLowerCase()
                .split("\\W+");
        HashSet<String> result = index.get(words[0]);

        for (String word : words) {
            result.retainAll(index.get(word));
        }

        return result;
    }
}
