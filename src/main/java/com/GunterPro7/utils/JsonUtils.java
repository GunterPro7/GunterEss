package com.GunterPro7.utils;

import com.GunterPro7.hypixel.Collections;
import com.GunterPro7.hypixel.JsonCollection;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.MiscListener;
import com.GunterPro7.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class JsonUtils {
    public static final String PLAYER_UUID = "";
    public static final String PROFILE_UUID = "";
    public static final String API_KEY = "";
    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    public static String fetch(String url) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);

        return EntityUtils.toString(httpResponse.getEntity());
    }

    public static void fetch(String url, Consumer<String> callback) {
        POOL.execute(() -> {
            try {
                String fetch = fetch(url);

                callback.accept(fetch);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Deprecated
    public static String fetch2(String urlString) throws IOException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return response.toString();
            } else {
                throw new IOException("Timed out!");
            }
        } catch (IOException e) {
            throw new IOException("Timed out");
        }
    }

    public static boolean downloadFile(String url, File file) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Unable to Download newest Version of GunterEss! Err: ");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveToFile(File file, ByteArrayOutputStream data) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.toByteArray());
            return true;
        } catch (IOException e) {
            System.err.println("Unable to save byte array to file: " + file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<ByteArrayOutputStream> downloadFile(String url) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Unable to Download newest Version of GunterEss! Err: ");
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(out);
    }

    public static void collectionApiFetch() throws IOException {
        String url = "https://api.hypixel.net/skyblock/profile?key=" + API_KEY + "&profile=" + PROFILE_UUID;
        String responseContent = fetch(url);

        String[] collectionList = responseContent.split("\"collection\"");
        String[] unlockedTierList = responseContent.split("\"unlocked_coll_tiers\"");
        String collection = collectionList[collectionList.length - 1].split("}")[0].substring(2); // TODO THIS IS SHIT
        String unlockedTiers = unlockedTierList[unlockedTierList.length - 1].split("}")[0].split("]")[0].substring(2); // TODO THIS IS SHIT

        MiscListener.SERVER_COLLECTION_MAP = processJSONString(collection, false);
        HashMap<String, Long> map = processJSONString(unlockedTiers, true);

        //for (Collections collections : Collections.values()) {
        //    if (SERVER_COLLECTION_MAP.get(collections.getApiName()) != null) {
        //        collections.addCollection(SERVER_COLLECTION_MAP.get(collections.getApiName()));
        //    }
        //}

        for (String key : map.keySet()) {
            System.out.println(key);
            com.GunterPro7.hypixel.Collections coll = com.GunterPro7.hypixel.Collections.valueOfAPI(key);
            if (coll != null) {
                coll.setLevel(Math.toIntExact(map.get(key)));
            }
        }

        Main.starting = false;
    }

    public static HashMap<String, Long> processJSONString(String json, boolean tiers) {
        HashMap<String, Long> map = new HashMap<>();

        for (String part : json.split(",")) {
            String[] parts;
            if (tiers) {
                int index = part.lastIndexOf("_");

                parts = new String[]{part.substring(0, index), part.substring(index + 1)};
            } else {
                parts = part.split("\":");
            }
            System.out.println(Arrays.toString(parts));
            parts[0] = parts[0].replace("\"", "");
            long value = Long.parseLong(parts[1].replace("\"", ""));
            if (tiers) {
                if (map.get(parts[0]) != null) {
                    long mapValue = map.get(parts[0]);
                    if (mapValue >= value) {
                        continue;
                    }
                }
            }
            map.put(parts[0].replace("\"", ""), Long.parseLong(parts[1].replace("\"", "")));
            if (com.GunterPro7.hypixel.Collections.includesAPI(parts[0]) && !tiers) {
                com.GunterPro7.hypixel.Collections.valueOfAPI(parts[0]).setCrops(Long.parseLong(parts[1].replace("\"", "")));
            }


        }


        return map;
    }

    public static void addEntriesToEnum(JsonObject jsonObject) {
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            keys.add(entry.getKey());
            if (entry.getValue().isJsonObject()) {
                String key = entry.getKey();
                if (com.GunterPro7.hypixel.Collections.includesAPI(key)) {
                    Collections.valueOfAPI(key).setCollection(new JsonCollection((JsonObject) entry.getValue()));
                }
                addEntriesToEnum(entry.getValue().getAsJsonObject());
            }
        }
    }

    public static boolean isJson(String string) {
        try {
            new JsonParser().parse(string);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static String fetchToBackend(String file, Map<String, String> args) {
        return fetchToBackend(file, args, () -> {});
    }

    public static String fetchToBackend(String file, Map<String, String> args, AdvancedChat.Callback callback) {
        StringBuilder call = new StringBuilder("http://49.12.101.156/GunterEss/" + file);

        if (args.size() > 0) {
            call.append("?");
        }

        for (Map.Entry<String, String> arg : args.entrySet()) {
            if (!call.toString().endsWith("?")) {
                call.append("&");
            }
            call.append(arg.getKey()).append("=").append(arg.getValue());
        }

        StringBuilder response = new StringBuilder();
        POOL.execute(() -> {
            try {
                response.append(fetch(call.toString()));
                AdvancedChat.sendPrivateMessage(response.toString());
                callback.run(); // TODO hier die response dann mitübergeben und dann ne action machen
            } catch (IOException e) {
                System.out.println("Unable to fetch to backend call: " + call);
            }
        });

        return response.toString();
    }
}
