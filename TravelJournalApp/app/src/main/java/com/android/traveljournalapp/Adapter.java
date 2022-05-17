package com.android.traveljournalapp;
import com.squareup.picasso.Picasso;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    //ArrayList cityImg, cityName, cityDesc, cityFeedback, addedByUserId ;
    private ArrayList<ItemModal> itemModalArrayList;
    Context context;
    private String imageUrl;

    // creating a constructor for our variables.
    public Adapter(ArrayList<ItemModal> itemModalArrayList, Context context) {
        this.itemModalArrayList = itemModalArrayList;
        this.context = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<ItemModal> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        itemModalArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        ItemModal modal = itemModalArrayList.get(position);
        holder.cityNameTV.setText(modal.getCityName());
        holder.cityDescTV.setText(modal.getCityDescription());
        holder.cityFeedbackTV.setText(modal.getCityFeedback());
       // holder.cityImgIV.setImageURI(modal.getCityImg());
    }

    @Override
    public int getItemCount() {
       return itemModalArrayList.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
       // ImageView cityImgIV;
        TextView cityNameTV, cityDescTV, cityFeedbackTV;

        public ViewHolder(View view) {
            super(view);
           // cityImgIV = (ImageView) view.findViewById(R.id.cityImg);
            cityNameTV = (TextView) view.findViewById(R.id.cityName);
            cityDescTV = (TextView) view.findViewById(R.id.cityDesc);
            cityFeedbackTV = (TextView) view.findViewById(R.id.cityFeedback);
        }
    }
}