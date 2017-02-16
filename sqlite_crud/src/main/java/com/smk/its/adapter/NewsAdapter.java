package com.smk.its.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smk.its.R;
import com.smk.its.activity.News_activity;
import com.smk.its.entity.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smkpc9 on 20/12/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList = new ArrayList<>();
    private Context context;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh;mm");

    public  NewsAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String date = formatter.format(new Date(newsList.get(position).getCreateDate()));

        holder.title.setText(newsList.get(position).getTitle());
        holder.date.setText(date);
        holder.content.setText(newsList.get(position).getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void  addnews(List<News> newses){
        this.newsList = newses;
        notifyDataSetChanged();
    }

    public void clearNews(){
        newsList.clear();
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView date;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.text_title);
            date = (TextView) itemView.findViewById(R.id.text_date);
            content = (TextView) itemView.findViewById(R.id.text_content);


        }
}

public void dialog(final int position) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(R.string.app_name);
    builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == 0) {
                ((News_activity) context).edit(newsList.get(position));
            } else if (i == 1) {
                dialogDelete(position);
            }
        }
    });

builder.show();
}
     public void dialogDelete(final int position) {
         AlertDialog.Builder builder = new  AlertDialog.Builder(context);
         builder.setTitle(R.string.app_name);
         builder.setMessage("Item ini akan dihapus");
         builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {

             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 ((News_activity)context).delete(newsList.get(position));
             }
         });
         builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();

                     }
                 });
         builder.show();

     }

}