package de.rexlmanu.ragemade.nicksystem.utils;

import com.google.gson.*;
import com.mojang.util.UUIDTypeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Created by rexlManu on 29.08.2017.
 * Plugin by rexlManu
 * https://rexlGames.de
 * Coded with IntelliJ
 */
public class UUIDFetcher {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static ExecutorService pool = Executors.newCachedThreadPool();

    private String name;
    private UUID id;

    private static void getUUID(String name, Consumer<UUID> action) {
        pool.execute(() -> {
            try {
                action.accept(UUIDFetcher.getUUID(name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static UUID getUUID(String name) throws Exception {
        return UUIDFetcher.getUUIDAt(name, System.currentTimeMillis());
    }

    private static UUID getUUIDAt(String name, long timestamp) throws Exception {

        HttpURLConnection connection = (HttpURLConnection)
                new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d",
                        name, timestamp / 1000)).openConnection();

        connection.setReadTimeout(5000);

        UUIDFetcher uuidFetcher = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
        return uuidFetcher.id;
    }

    private static void getName(UUID uuid, Consumer<String> action) {
        pool.execute(() -> action.accept(UUIDFetcher.getName(uuid)));
    }

    public static String getName(UUID uuid) {

        try {

            HttpURLConnection connection = (HttpURLConnection)
                    new URL(String.format("https://api.mojang.com/user/profiles/%s/names",
                            uuid.toString().replace("-", ""))).openConnection();

            connection.setReadTimeout(5000);

            JsonObject[] jsonObject = gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), JsonObject[].class);
            return jsonObject[(jsonObject.length - 1)].get("name").getAsString();

        } catch (Exception exc) {
        }

        return null;
    }

    private void getUUIDAt(String name, long timestamp, Consumer<UUID> action) {
        pool.execute(() -> {
            try {
                action.accept(UUIDFetcher.getUUIDAt(name, timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static Map<String, String> uuidCache = new HashMap<>();

    public static String getUUIDAlternative(String user) {
        if (uuidCache.containsKey(user)) {
            return (String) uuidCache.get(user);
        }
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + user);
            InputStream stream = url.openStream();
            InputStreamReader inr = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(inr);
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            String result = sb.toString();

            JsonElement element = new JsonParser().parse(result);
            JsonObject obj = element.getAsJsonObject();

            String uuid = obj.get("id").toString();

            uuid = uuid.substring(1);
            uuid = uuid.substring(0, uuid.length() - 1);

            uuidCache.put(user, uuid);

            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
