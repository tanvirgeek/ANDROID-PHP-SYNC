package com.tanvirgeek.sync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder> {

    private ArrayList<Contact> contactArrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Contact> arrayList){

        this.contactArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.name.setText(contactArrayList.get(position).getName());
        int msyncStatus = contactArrayList.get(position).getSyncstatus();
        if(msyncStatus == DBContact.SYNC_STATUS_OK){
            holder.msyncStatus.setImageResource(R.drawable.tick);
        }else{
            holder.msyncStatus.setImageResource(R.drawable.sync);
        }
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        ImageView msyncStatus;
        TextView name;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            msyncStatus = (ImageView) itemView.findViewById(R.id.imgSynch);
            name = (TextView) itemView.findViewById(R.id.txtName);
        }
    }
}
