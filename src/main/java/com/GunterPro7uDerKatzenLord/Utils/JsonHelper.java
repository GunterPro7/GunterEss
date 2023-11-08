package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Main;
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

public class JsonHelper {
    public static final String PLAYER_UUID = "ca9cc012-83cd-4570-ab57-48b36596ab5d";
    public static final String PROFILE_UUID = "f7b343ae1cc04f6c826bee85fa37a993";
    public static final String API_KEY = "a3d12e28-66d5-4d00-8449-2ad71aaa43f2"; // "a3d12e28-66d5-4d00-9449-2ad71aaa43f2"

    public static String fetch(String url) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);

        return EntityUtils.toString(httpResponse.getEntity());
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

    public static void downloadFile(String url, String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }
    }

    public static void collectionApiFetch() throws IOException {
        String url = "https://api.hypixel.net/skyblock/profile?key=" + API_KEY + "&profile=" + PROFILE_UUID;
        String responseContent = fetch(url);

        String[] collectionList = responseContent.split("\"collection\"");
        String[] unlockedTierList = responseContent.split("\"unlocked_coll_tiers\"");
        String collection = collectionList[collectionList.length - 1].split("}")[0].substring(2); // TODO THIS IS SHIT
        String unlockedTiers = unlockedTierList[unlockedTierList.length - 1].split("}")[0].split("]")[0].substring(2); // TODO THIS IS SHIT

        Listeners.SERVER_COLLECTION_MAP = processJSONString(collection, false);
        HashMap<String, Long> map = processJSONString(unlockedTiers, true);

        //for (Collections collections : Collections.values()) {
        //    if (SERVER_COLLECTION_MAP.get(collections.getApiName()) != null) {
        //        collections.addCollection(SERVER_COLLECTION_MAP.get(collections.getApiName()));
        //    }
        //}

        for (String key : map.keySet()) {
            System.out.println(key);
            Collections coll = Collections.valueOfAPI(key);
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
            if (Collections.includesAPI(parts[0]) && !tiers) {
                Collections.valueOfAPI(parts[0]).setCrops(Long.parseLong(parts[1].replace("\"", "")));
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
                if (Collections.includesAPI(key)) {
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
}
