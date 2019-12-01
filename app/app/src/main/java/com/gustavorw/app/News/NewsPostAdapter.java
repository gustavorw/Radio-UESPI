package com.gustavorw.app.News;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gustavorw.app.R;

import java.util.List;

public class NewsPostAdapter extends RecyclerView.Adapter<NewsPostAdapter.PostViewHolder> {

    private Context context;
    private List<News> news;
    private Activity activity;

    public NewsPostAdapter(Context context, List<News> news, Activity activity){
        this.context = context;
        this.news = news;
        this.activity =  activity;
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
        String data = aux.getData();
        data = data.substring(5,10);
        String mes = data.substring(0,2);
        String dia = data.substring(3,5);
        switch (Integer.valueOf(mes)){
            case 12:
                mes = "dez";
                break;
            case 11:
                mes = "nov";
                break;
            case 10:
                mes = "out";
                break;
            case 9:
                mes = "set";
                break;
            case 8:
                mes = "ago";
                break;
            case 7:
                mes = "jul";
                break;
            case 6:
                mes = "jun";
                break;
            case 5:
                mes = "mai";
                break;
            case 4:
                mes = "abr";
                break;
            case 3:
                mes = "mar";
                break;
            case 2:
                mes = "fev";
                break;
            case 1:
                mes = "jan";
                break;
        }
        mes = dia + "\n"+mes;
        holder.data.setText(mes);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,aux.getTitulo(),Toast.LENGTH_LONG).show();
               // Intent intent = new Intent(activity, LiveFragment.class);
               // intent.putExtra("link",aux.getLink());
              //  context.startActivity(intent);
               // context.getApplicationContext().startActivity(intent);

            }
        });*/

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
