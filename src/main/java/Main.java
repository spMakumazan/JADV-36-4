import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static String LETTERS = "abc";
    public static int LENGTH = 100_000;
    public static int SIZE = 10_000;

    public static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < SIZE; i++) {
                try {
                    aQueue.put(generateText(LETTERS, LENGTH));
                    bQueue.put(generateText(LETTERS, LENGTH));
                    cQueue.put(generateText(LETTERS, LENGTH));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            System.out.println();
            System.out.println(countMaxValue(aQueue, 'a'));
        }).start();

        new Thread(() -> {
            System.out.println(countMaxValue(bQueue, 'b'));
        }).start();

        new Thread(() -> {
            System.out.println(countMaxValue(cQueue, 'c'));
        }).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String countMaxValue(BlockingQueue<String> queue, char c) {
        String result = null;
        int maxCounter = 0;
        for (int i = 0; i < SIZE; i++) {
            try {
                String s = queue.take();
                int counter = 0;
                for (int j = 0; j < s.length(); j++) {
                    if (s.charAt(j) == c) {
                        counter++;
                    }
                }
                if (counter > maxCounter) {
                    maxCounter = counter;
                    result = s;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return maxCounter + " символов " + c + " в строке: " + result.substring(0, 19) + "...";
    }
}
