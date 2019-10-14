package com.gustavorw.teste;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<News> arrayNews;
    public NewsAdapter(ArrayList<News> news){
        this.arrayNews = news;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
       // TextView description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title_text);
            date = itemView.findViewById(R.id.data_text);
           // description = itemView.findViewById(R.id.description_text);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        News newsAux = arrayNews.get(position);
        holder.title.setText(newsAux.getTitle());
        //holder.description.setText(newsAux.getDate());
        holder.date.setText(newsAux.getDate());

    }

    @Override
    public int getItemCount() {
        return arrayNews.size();
    }
}
