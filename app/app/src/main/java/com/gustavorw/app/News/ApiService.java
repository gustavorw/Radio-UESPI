package com.gustavorw.app.News;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
   /*@GET("index.php{page}")
   Call<String> baseNews(@Query("rest_route=%2Fwp%2Fv2%2Fposts%2F&page") int page);

   */
   @GET("/prp/noticias.php")
   Call<List<News>> baseNews(@Query("pagina") int pagina);
}

