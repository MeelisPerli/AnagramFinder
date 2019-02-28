import java.io.*;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class main {

    static HashMap<Byte, Integer> charToNumMap;
    static Integer[] primes;
    static int inputVal = Integer.MAX_VALUE;
    static int inputlen;

    public static void main(String[] argv) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        //This is where the magic happens
        initWordMap(argv[1]);
        String result = getAnagrams(argv[0]);

        long stop = System.currentTimeMillis() - startTime;
        System.out.println(stop + result);


    }

    //loads data as bytes, creates threads and gathers results.
    private static String getAnagrams(String dictPath) throws IOException, InterruptedException {
        byte[] fileBytes = Files.readAllBytes(new File(dictPath).toPath());
        int len = fileBytes.length;
        int numberOfThreads = 3;
        List<Integer> points = getChunkEndPoints(numberOfThreads, len, fileBytes);
        List<anagramSolver> threads = new ArrayList<>();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < numberOfThreads; i++) {
            anagramSolver t = new anagramSolver(fileBytes, points.get(i), points.get(i+1));
            t.start();
            threads.add(t);
        }
        for (int i = 0; i < numberOfThreads; i++) {
            anagramSolver t = threads.get(i);
            t.join();
            res.append(t.getResult());
        }

        return res.toString();

    }
    // finds where a line should end and splits there instead of mid line
    private static List<Integer> getChunkEndPoints(int chunks, int maxlen, byte[] bytes) {
        List<Integer> chunkPoints = new ArrayList<>();
        int chunkSize = maxlen/chunks;
        for (int i = 0; i < maxlen; i+= chunkSize) {
            int k = i;
            while(bytes[k] != 10){
                k++;
            }
            chunkPoints.add(k);
        }
        chunkPoints.set(0, 0);
        if (chunkPoints.size() != chunks + 1){
            chunkPoints.add(maxlen);
        }
        return chunkPoints;
    }

    private static void initWordMap(String inputword) {
        charToNumMap = new HashMap<>();
        int amountInList = 0;
        // could also make prime generator, but this should be faster
        primes = new Integer[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199};
        for (char c : inputword.toCharArray()) {
            char low = Character.toLowerCase(c);
            if (!charToNumMap.containsKey(low)) {
                charToNumMap.put((byte) Character.toLowerCase(c), primes[amountInList]);
                charToNumMap.put((byte) Character.toUpperCase(c), primes[amountInList]);
                amountInList++;
            }
        }
        inputlen = inputword.length();
        inputVal = wordToNum(inputword);

    }

    private static int wordToNum(String word) {
        int s = 1;
        for (byte c : word.getBytes())
            s *= byteToNum(c);
        return s;
    }

    public static int byteToNum(byte b) {
        if (charToNumMap.containsKey(b))
            return charToNumMap.get(b);
        return 0;
    }
}