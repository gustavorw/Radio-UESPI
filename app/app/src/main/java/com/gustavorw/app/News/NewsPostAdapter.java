package com.gustavorw.app.News;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavorw.app.MainActivity;
import com.gustavorw.app.R;
import com.gustavorw.app.Webview;

import java.util.List;

public class NewsPostAdapter extends RecyclerView.Adapter<NewsPostAdapter.PostViewHolder> {

    private Context context;
    private List<News> news;

    public NewsPostAdapter(Context context, List<News> news){
        this.context = context;
        this.news = news;

    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_list, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final News aux = this.news.get(position);
        holder.titulo.setText(aux.getTitulo());
        holder.data.setText(aux.getData());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,aux.getTitulo(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context.getApplicationContext(),Webview.class);
                intent.putExtra("link",aux.getLink());
               // context.getApplicationContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.news.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView data;
        public PostViewHolder(View view){
            super(view);
            titulo = view.findViewById(R.id.title_text);
            data = view.findViewById(R.id.data_text);
        }
    }

}
