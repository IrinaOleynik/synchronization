import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int threadsCount = 100;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        Runnable logic = () -> {
            String route = generateRoute("RLRFR", 100);
            int rCount = (int) route.chars().filter(ch -> ch == 'R').count();
            int frequency = 1;
            synchronized (sizeToFreq) {
                for (Map.Entry<Integer, Integer> kv : sizeToFreq.entrySet()) {
                    if (kv.getKey().equals(rCount)) {
                        frequency = kv.getValue() + 1;
                    }
                }
                sizeToFreq.put(rCount, frequency);
            }
        };

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Optional<Map.Entry<Integer, Integer>> maxEntity = sizeToFreq.entrySet().stream().
                max(Map.Entry.comparingByValue());
        int maxCount = maxEntity.get().getKey();
        System.out.println("Самое частое количество повторений: " + maxCount + " (встретилось " +
                maxEntity.get().getValue() + " раз)");

        System.out.println("Другие размеры:");
        sizeToFreq.remove(maxCount);
        for (Map.Entry<Integer, Integer> kv : sizeToFreq.entrySet()) {
            System.out.format(" - %d (%d раз)\n", kv.getKey(), kv.getValue());
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}