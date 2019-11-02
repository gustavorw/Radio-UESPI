package com.gustavorw.app.News;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
   @GET("teste/")
   Call<List<News>> baseNews();
}
