package com.gustavorw.app.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavorw.app.News.News;
import com.gustavorw.app.News.NewsAdapter;
import com.gustavorw.app.News.RecyclerItemClickListenerNews;
import com.gustavorw.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class NewsFragment extends Fragment {
    public NewsFragment() {
    }

    @Nullable

    private RecyclerView recyclerView;
    private ArrayList<News> newsArrayList;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = viewRoot.findViewById(R.id.recyclerView);
        progressBar = viewRoot.findViewById(R.id.progressBar);
        newsArrayList = new ArrayList<>();
        NewsUtils task = new NewsUtils();
        task.execute();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenerNews(getContext().getApplicationContext(), recyclerView, new RecyclerItemClickListenerNews.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                News aux = newsArrayList.get(position);
                //Intent intent = new Intent(getApplicationContext(),Webview.class);
                //intent.putExtra("link",aux.getLink());
                //startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
        return viewRoot;

    }


    public void updateUI(ArrayList<News> newsArray) {
        newsArrayList = newsArray;
        newsAdapter = new NewsAdapter(newsArray);
        recyclerView.setAdapter(newsAdapter);
        progressBar.setVisibility(View.GONE);

    }


    public class NewsUtils extends AsyncTask<URL, Integer, ArrayList<News>> {
        public ArrayList<News> news;

        @Override
        protected ArrayList<News> doInBackground(URL... urls) {
            URL url = createUrl("https://portaldaibiapaba.com.br/prp/teste/");
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            ArrayList<News> news = extractFeatureFromJson(jsonResponse);
            this.news = news;
            return news;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.v("Deu merda", "url");
                // Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                // TODO: Handle the exception
                Log.v("Deu merda", "Http");
                //Log.e(LOG_TAG, "Error response" + urlConnection.getResponseCode());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<News> extractFeatureFromJson(String earthquakeJSON) {
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }
            try {
                ArrayList<News> arrayList = new ArrayList<>();
                JSONArray jsonResponse = new JSONArray(earthquakeJSON);
                for (int i = 0; i < jsonResponse.length(); i++) {
                    JSONObject aux = jsonResponse.getJSONObject(i);
                    Log.v("Titulo", aux.getString("titulo"));
                    News auxNews;
                    auxNews = new News(aux.getString("titulo"), aux.getString("data"), aux.getString("link"));
                    arrayList.add(auxNews);

                }
                return arrayList;
            } catch (JSONException e) {
                Log.v("Deu merda", "Extrair json");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //progressBar.setVisibility(View.GONE);

        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {

            super.onPostExecute(news);
            updateUI(news);
        }

    }
}
