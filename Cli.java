package miniredis;

import java.util.Scanner;

public class Cli {

    public static void main(String[] args) {

        StorageEngine storageEngine = new StorageEngine();
        ExpiryManager expiryManager = new ExpiryManager(storageEngine);

        MiniRedis redis = new MiniRedis(storageEngine, expiryManager);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {

                String command = scanner.nextLine().trim();

                if (command.isEmpty()) {
                    continue;
                }

                String[] parsedCommand = command.split("\\s+");

                String redisCmd = parsedCommand[0].toUpperCase();

                switch (redisCmd) {

                    case "SET" -> {
                        redis.set(parsedCommand[1], parsedCommand[2]);
                        System.out.println("OK");
                    }

                    case "GET" -> {
                        System.out.println(redis.get(parsedCommand[1]));
                    }

                    case "DEL" -> {
                        redis.delete(parsedCommand[1]);
                        System.out.println("OK");
                    }

                    case "EXPIRE" -> {
                        redis.setTtl(
                                parsedCommand[1],
                                Integer.parseInt(parsedCommand[2]) * 1000
                        );
                        System.out.println("OK");
                    }

                    case "TTL" -> {
                        System.out.println(redis.getTtl(parsedCommand[1]));
                    }

                    case "INCR" -> {
                        redis.increment(parsedCommand[1]);
                    }

                    case "DECR" -> {
                        redis.decrement(parsedCommand[1]);
                    }

                    case "FLUSHALL" -> {
                        redis.flushAll();
                        System.out.println("OK");
                    }

                    case "EXIT", "QUIT" -> {
                        return;
                    }
                    default -> System.out.println("Unknown command: " + redisCmd);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}