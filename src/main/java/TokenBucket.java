public class TokenBucket {
    private long lastTokenDispensedTime;
    private long remainingTime;
    private long currTime;
    private int lastTokenCount;
    private final int MAX_TOKEN;
    private final int REGENERATION_TIME;

    public TokenBucket(int maxToken, int regenerationTime) {
        REGENERATION_TIME = regenerationTime;
        lastTokenCount = 0;
        lastTokenDispensedTime = System.currentTimeMillis();
        remainingTime = 0;
        MAX_TOKEN = maxToken;
    }

    public synchronized void getToken() throws NoTokenException {
        int currTokenCount = getCurrentTokenCount();
        if (currTokenCount == 0) {
            lastTokenCount = 0;
            throw new NoTokenException();
        }
        remainingTime = (currTime - lastTokenDispensedTime + remainingTime) % (REGENERATION_TIME * 1000L);
        lastTokenDispensedTime = currTime;
        lastTokenCount = currTokenCount - 1;
        System.out.printf("Thread : %s acquired token. Bucket size: %s At: %s%n",
                Thread.currentThread().getName(), lastTokenCount, System.currentTimeMillis() / 1000);
    }

    private int getCurrentTokenCount() {
        currTime = System.currentTimeMillis();
        long time = (currTime - lastTokenDispensedTime + remainingTime);
        time = time / (REGENERATION_TIME * 1000L);
        return (int) Math.min(MAX_TOKEN, lastTokenCount + time);
    }
}
