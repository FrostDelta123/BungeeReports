package ru.frostdelta.bungeereports.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class HastebinAPI {

    public static String post(List<String> input) throws IOException{

        Gson gson = new Gson();
        HttpURLConnection connection = (HttpURLConnection) new URL("https://paste.divinecode.org/documents").openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod( "POST" );
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty( "Charset", "UTF-8" );
        connection.setRequestProperty( "Content-Type", "text/plain" );
        connection.connect();
        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())){

            for(String in : input) {
                dos.write(in.getBytes(StandardCharsets.UTF_8));
            }
            dos.flush();
        }

        String result;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
        {
            result = br.lines().collect(Collectors.joining());
        }
        connection.disconnect();

        return "https://paste.divinecode.org/" +  gson.fromJson(result, JsonObject.class).get("key").getAsString();
    }
}
