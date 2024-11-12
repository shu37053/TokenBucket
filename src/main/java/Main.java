import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Main {
    private static TokenBucket tokenBucket;
    private static long startTime;
    private static long endTime;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter max bucket size : ");
        int maxSize = Integer.parseInt(br.readLine());
        System.out.println("Enter regeneration time :");
        int reg = Integer.parseInt(br.readLine());
        tokenBucket = new TokenBucket(maxSize, reg);
        startTime = System.currentTimeMillis() / 1000;
        System.out.println("Start time : " + startTime);
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            int randomSeconds = random.nextInt(5) + 100;
            startNewThread(randomSeconds, i);
        }
    }

    private static void startNewThread(final int waitTime, int i) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    tokenBucket.getToken();
                    endTime = System.currentTimeMillis() / 1000;
                    return;
                } catch (NoTokenException e) {
                    System.out.printf("No token available for thread id = %s, sleeping for %s second%n",
                            Thread.currentThread().getName(), waitTime);
                    try {
                        sleep(waitTime * 1000L);
                    } catch (InterruptedException ex) {
                        System.out.println("Thread interrupted");
                    }
                }
            }
        }, String.valueOf(i));
        thread.start();
    }
}
