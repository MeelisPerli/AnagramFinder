import java.util.HashMap;

public class anagramSolver extends Thread {

    private byte[] bytes;
    private int start;
    private int end;
    private String res;
    private HashMap<Byte, Integer> charToNumMap;

    public anagramSolver(byte[] bytes, int start, int end, HashMap<Byte, Integer> charToNumMap) {
        this.bytes = bytes;
        this.start = start;
        this.end = end;
        this.charToNumMap = charToNumMap;
    }


    public void run() {
        StringBuilder anagrams = new StringBuilder();
        char[] line = new char[main.inputlen];
        int currentVal = 1;
        char c;
        byte b;
        int count = 0;
        for (int i = start; i < end; i++) {
            b = bytes[i];
            if (b == 13) {
                // wont have to check if i < fileBytes.length this way.
                i++;
                if (currentVal == main.inputVal) {
                    anagrams.append(',');
                    anagrams.append(line);
                }
                count = 0;
                currentVal = 1;
                continue;
            }

            //skip word
            currentVal *= charToNumMap.get(b);
            line[count] = (char) b;
            count++;
            if (currentVal == 0) {
                while (b != 10) {
                    i++;
                    b = bytes[i];
                }
                count = 0;
                currentVal = 1;
            } else if (count == main.inputlen && bytes[i + 1] != 10 && bytes[i + 1] != 13) {
                while (b != 10) {
                    i++;
                    b = bytes[i];
                }
                count = 0;
                currentVal = 1;
            }
        }

        res = anagrams.toString();
    }

    public void runOnMain() {
        run();
    }

    public String getResult() {
        return res;
    }

}
