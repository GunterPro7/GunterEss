package com.GunterPro7uDerKatzenLord.Utils;

import com.GunterPro7uDerKatzenLord.Main;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JsonCollection {
    private final JsonObject gson;
    private final int tiers;
    private final Rewards[] rewards;

    public JsonCollection(JsonObject gson) {
        this.gson = gson;
        tiers = this.gson.get("maxTiers").getAsInt();
        rewards = new Rewards[tiers];

        int counter = 0;
        for (JsonElement jsonObject : this.gson.get("tiers").getAsJsonArray()) {
            JsonObject json = jsonObject.getAsJsonObject();
            rewards[counter++] = new Rewards(json.get("tier").getAsInt(), json.get("amountRequired").getAsInt(),
                    json.get("unlocks").toString().replaceAll("\\[", "").replaceAll("]", "")
                            .replaceAll("\"", "").split(",")); // TODO die liste hier, also die rewards (string) mus ich noch besser machen!!!
        }
    }

    public JsonObject getGson() {
        return gson;
    }

    public int getTiers() {
        return tiers;
    }

    public Rewards[] getRewards() {
        return rewards;
    }

    public List<String> getRewardsAsString(int start, int end) {
        if (end > tiers || start > tiers || start > end) {
            Utils.sendPrivateMessage("§CInvalid Input!");
        }
        List<String> rewardList = new ArrayList<>();

        int skyblockXP = 0;
        int counter = 0;
        for (int i = start; i < end; i++) {
            Rewards reward = rewards[i];
            for (String rewardString : reward.getUnlocks()) {
                if (rewardString.contains("SkyBlock XP")) {
                    skyblockXP += Integer.parseInt(rewardString.split(" ")[0].substring(1));
                } else {
                    rewardList.add(rewardString);
                }
            }
        }

        rewardList.add('+' + skyblockXP + " SkyBlock XP");
        return rewardList;
    }
}