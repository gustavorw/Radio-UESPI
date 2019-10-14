package com.gustavorw.teste;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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


public class MainActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private ArrayList<News> newsArrayList;
    private NewsAdapter newsAdapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Radio UESPI");
        setSupportActionBar(toolbar);


        // recyclerView
        recyclerView = findViewById(R.id.newsRecyclerView);
        if(isOnline(getApplicationContext())){
            progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando notícias..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            NewsUtils task = new NewsUtils();
            task.execute();
        }else {
            Toast.makeText(getApplicationContext(),"Dispositivo sem conexão com internet",Toast.LENGTH_LONG).show();
        }

        newsArrayList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

       // recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayout.VERTICAL));
        // Log.v("tamanho", String.valueOf(task.news.size()));
       // recyclerView.setHasFixedSize(true);
        //NewsAdapter newsAdapter = new NewsAdapter();
        //recyclerView.setAdapter(newsAdapter);


        // adicionando metodo para chamar WebView

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenerNews(getApplicationContext(), recyclerView, new RecyclerItemClickListenerNews.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               News aux = newsArrayList.get(position);
                Intent intent = new Intent(getApplicationContext(),Webview.class);
                intent.putExtra("link",aux.getLink());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    public void updateUI(ArrayList<News> newsArray){
        newsArrayList = newsArray;
        newsAdapter = new NewsAdapter(newsArray);
        recyclerView.setAdapter(newsAdapter);
        progressDialog.cancel();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.email_menu) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:radio@example.com"));
            startActivity(emailIntent);

            return true;
        } else if (id == R.id.compartilhar_menu) {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/plain");
            intentShare.putExtra(Intent.EXTRA_TEXT, "http://www.prp.uespi.com/");
            startActivity(Intent.createChooser(intentShare, "Compartilhar"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class NewsUtils extends AsyncTask<URL, Integer, ArrayList<News>> {
        public  ArrayList<News> news;

        @Override
        protected ArrayList<News> doInBackground(URL... urls) {
            // Create URL object
            // this.bar.setVisibility(View.VISIBLE);
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

        /**
         * Returns new URL object from the given string URL.
         */
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

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
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

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
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

        /**
         * Return an {@link News} object by parsing out information
         * about the first earthquake from the input earthquakeJSON string.
         */
        private ArrayList<News> extractFeatureFromJson(String earthquakeJSON) {
            if (TextUtils.isEmpty(earthquakeJSON)) {
                return null;
            }
            try {
                ArrayList<News> arrayList = new ArrayList<>();
                JSONArray jsonResponse = new JSONArray(earthquakeJSON);
                for(int i = 0; i < jsonResponse.length(); i++){
                    JSONObject aux = jsonResponse.getJSONObject(i);
                    Log.v("Titulo",aux.getString("titulo"));
                    News auxNews;
                    auxNews = new News(aux.getString("titulo"), aux.getString("data"),aux.getString("link"));
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
