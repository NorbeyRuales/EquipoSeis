package com.example.pico_botella.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PokemonRemoteDataSource {

    private static final String POKEDEX_URL =
            "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json";

    private final Random random = new Random();

    public String obtenerImagenAleatoria() throws Exception {
        String response = readUrl(POKEDEX_URL);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray pokemonArray = jsonObject.getJSONArray("pokemon");
        JSONObject randomPokemon = pokemonArray.getJSONObject(random.nextInt(pokemonArray.length()));
        String imageUrl = randomPokemon.getString("img");

        if (imageUrl.startsWith("http://")) {
            return imageUrl.replace("http://", "https://");
        }

        return imageUrl;
    }

    private String readUrl(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
        )) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}
