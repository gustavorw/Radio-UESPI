package com.gustavorw.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavorw.app.News.ApiService;
import com.gustavorw.app.News.News;
import com.gustavorw.app.News.NewsPostAdapter;
import com.gustavorw.app.News.Utils;
import com.gustavorw.app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class NewsFragment extends Fragment {
    private Context mContext;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = viewRoot.findViewById(R.id.recyclerView);
        progressBar = viewRoot.findViewById(R.id.progressBar);
        progressFist = viewRoot.findViewById(R.id.progressBar3);
        progressFist.setVisibility(View.VISIBLE);
        newsArrayList = new ArrayList<>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        adapter = new NewsPostAdapter(getContext().getApplicationContext(), newsArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        retrofit = Utils.getClient();
        progressBar.setVisibility(View.VISIBLE);

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
                    getData();
                }
            }
        });
        getData();


        return viewRoot;

    }


    private void getData() {
        ApiService service = retrofit.create(ApiService.class);
        Call<List<News>> call = service.baseNews();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    List<News> aux = response.body();
                    for(int i = 0; i < aux.size(); i++){
                        newsArrayList.add(aux.get(i));
                    }
                    progressBar.setVisibility(View.GONE);
                    progressFist.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
                Log.v("erro","sem resposta");
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                if(Utils.isNetwork(getContext().getApplicationContext())){
                    Log.v("erro","sem internet");
                    Toast.makeText(getContext().getApplicationContext(),"Sem conexão", LENGTH_LONG).show();
                }

                Log.v("erro","sem internet");
            }
        });
    }

}

