package com.sonyamoisset.android.myrestaurants.services;


import com.sonyamoisset.android.myrestaurants.Constants;
import com.sonyamoisset.android.myrestaurants.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YelpService {

    public void findRestaurants(String location, Callback callback) {

        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder urlBuilder =
                Objects.requireNonNull(HttpUrl.parse(Constants.YELP_BASE_URL)).newBuilder();
        urlBuilder.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Constants.YELP_TOKEN)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Restaurant> processResult(Response response) {

        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            String jsonData = Objects.requireNonNull(response.body()).string();
            JSONObject yelpJSON = new JSONObject(jsonData);
            JSONArray businessJSON = yelpJSON.getJSONArray("businesses");

            for (int i = 0; i < businessJSON.length(); i++) {
                JSONObject restaurantJSON = businessJSON.getJSONObject(i);

                String name = restaurantJSON.getString("name");
                String phone =
                        restaurantJSON.optString("display_phone", "Phone not available");
                String website = restaurantJSON.getString("url");
                double rating = restaurantJSON.getDouble("rating");
                String imageUrl = restaurantJSON.getString("image_url");
                double latitude =
                        restaurantJSON.getJSONObject("coordinates").getDouble("latitude");
                double longitude =
                        restaurantJSON.getJSONObject("coordinates").getDouble("longitude");

                ArrayList<String> address = new ArrayList<>();
                JSONArray addressJSON =
                        restaurantJSON.getJSONObject("location").getJSONArray("display_address");

                for (int y = 0; y < addressJSON.length(); y++) {
                    address.add(addressJSON.get(y).toString());
                }

                ArrayList<String> categories = new ArrayList<>();
                JSONArray categoriesJSON = restaurantJSON.getJSONArray("categories");

                for (int y = 0; y < categoriesJSON.length(); y++) {
                    categories.add(categoriesJSON.get(y).toString());
                }

                Restaurant restaurant = new Restaurant(
                        name,
                        phone,
                        website,
                        rating,
                        imageUrl,
                        address,
                        latitude,
                        longitude,
                        categories
                );
                restaurants.add(restaurant);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}