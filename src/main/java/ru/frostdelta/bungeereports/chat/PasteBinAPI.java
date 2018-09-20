package ru.frostdelta.bungeereports.chat;

import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PasteBinAPI {

    public static String paste(String urlParameters) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://hastebin.com/documents");

        try {
            post.setEntity(new StringEntity(urlParameters));
            HttpResponse response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());

            return "https://hastebin.com/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Could not post!";
    }
}
