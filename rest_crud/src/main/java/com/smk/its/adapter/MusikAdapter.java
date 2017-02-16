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
import com.smk.its.activity.Musik_activity;
import com.smk.its.entity.Musik;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smkpc9 on 20/12/16.
 */
public class MusikAdapter extends RecyclerView.Adapter<MusikAdapter.ViewHolder> {
    private  List<Musik> newsList = new ArrayList<>();
    private Context context;

    public MusikAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.musik_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title.setText(newsList.get(position).getTitle());
        holder.tahun.setText(newsList.get(position).getTahun());
        holder.desc.setText(newsList.get(position).getDeskripsi());

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

    public void  addnews(List<Musik> newses){
        this.newsList = newses;
        notifyDataSetChanged();
    }

    public void clearNews(){
        newsList.clear();
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        TextView tahun;
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.text_Title);
            tahun = (TextView) itemView.findViewById(R.id.text_Tahun);
            desc = (TextView) itemView.findViewById(R.id.text_Desc);


        }
}

public void dialog(final int position) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(R.string.app_name);
    builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == 0) {
                ((Musik_activity) context).edit(newsList.get(position));
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
                 ((Musik_activity)context).delete(newsList.get(position));
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