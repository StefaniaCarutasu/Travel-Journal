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
    ArrayList cityImg, cityName, cityDesc, cityFeedback, addedByUserId ;
    Context context;
    private String imageUrl;

    // Constructor for initialization
    public Adapter(Context context, /*ArrayList addedByUserId,*/ ArrayList cityImg, ArrayList cityName, ArrayList cityDesc, ArrayList cityFeedback) {
        this.context = context;
        //this.addedByUserId=addedByUserId;
        this.cityImg=cityImg;
        this.cityName=cityName;
        this.cityDesc=cityDesc;
        this.cityFeedback=cityFeedback;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // Passing view to ViewHolder
        Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // TypeCast Object to int type

       /* String res = (String) cityImg.get(position);
        holder.images.setImageResource(res);
*/
        //imageUrl=(String) cityImg.get(position);
        //Picasso.with(context).load(imageUrl).into(holder.images);
        holder.images.setImageURI((Uri) cityImg.get(position));
        holder.text1.setText((String) cityName.get(position));
        holder.text2.setText((String) cityDesc.get(position));
        holder.text3.setText((String) cityFeedback.get(position));
    }


    @Override
    public int getItemCount() {
        // Returns number of items 
        // currently available in Adapter
        return cityImg.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView text1, text2, text3;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.cityImg);
            text1 = (TextView) view.findViewById(R.id.cityName);
            text2 = (TextView) view.findViewById(R.id.cityDesc);
            text3 = (TextView) view.findViewById(R.id.cityFeedback);
        }
    }
}