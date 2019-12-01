package com.gustavorw.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gustavorw.app.News.ApiService;
import com.gustavorw.app.News.News;
import com.gustavorw.app.News.NewsClickEvent;
import com.gustavorw.app.News.NewsPostAdapter;
import com.gustavorw.app.News.Utils;
import com.gustavorw.app.Player.NewsWebViewActivity;
import com.gustavorw.app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class NewsFragment extends Fragment {
    private Context mContext;
    static int pages = 1;

    public NewsFragment(Context context) {
        this.mContext = context;
    }

    private RecyclerView recyclerView;
    private List<News> newsArrayList;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private NewsPostAdapter adapter;
    Boolean isScrolling = false;
    ProgressBar progressFist;
    int currentItems, totalItems, scrollOutItems;
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = viewRoot.findViewById(R.id.recyclerView);
        progressBar = viewRoot.findViewById(R.id.progressBar);
        progressFist = viewRoot.findViewById(R.id.progressBar3);
        progressFist.setVisibility(View.VISIBLE);
        newsArrayList = new ArrayList<>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        adapter = new NewsPostAdapter(getContext().getApplicationContext(), newsArrayList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = Utils.getClient();
        progressBar.setVisibility(View.VISIBLE);
       // progressBar.getIndeterminateDrawable().setColorFilter(Integer.parseInt("#044875"),android.graphics.PorterDuff.Mode.MULTIPLY);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    pages++;
                    getData();
                }
            }
        });
       getData();


        recyclerView.addOnItemTouchListener(new NewsClickEvent(getContext().getApplicationContext(), recyclerView, new NewsClickEvent.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                News aux = newsArrayList.get(position);
             //   Log.v("nome", aux.getTitulo() +" = " + String.valueOf(position));
                Intent intent = new Intent(getContext().getApplicationContext(), NewsWebViewActivity.class);
                intent.putExtra("link", aux.getLink());
                startActivity(intent);
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


    private void getData() {
        ApiService service = retrofit.create(ApiService.class);
        Call<List<News>> call = service.baseNews(pages);
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>>call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    List<News> aux = response.body();
                    for(int i = 0; i < aux.size(); i++){
                        newsArrayList.add(aux.get(i));
                      //  Log.v("on")
                    }
                    progressBar.setVisibility(View.GONE);
                    progressFist.setVisibility(View.GONE);
                   adapter.notifyDataSetChanged();
                }
                Log.v("on", "on response");
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                if(Utils.isNetwork(getContext().getApplicationContext())){
                   // Log.v("erro","sem internet");
                   // Toast.makeText(getContext().getApplicationContext(),"Sem conexão", LENGTH_LONG).show();
                }

                Log.v("erro","não foi possivel baixar os dados");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

}

