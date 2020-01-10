package com.flover.ocrapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flover.ocrapplication.R;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> imageUrls;
    private ArrayList<String> imageResults;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> imageUrls, ArrayList<String> imageResults, Context mContext) {
        this.imageUrls = imageUrls;
        this.imageResults = imageResults;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "OnBindViewHolder: called");
        holder.imageResult.setText(imageResults.get(position));
        Glide.with(mContext).asBitmap().load(imageUrls.get(position)).into(holder.imageUrls);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageUrls;
        TextView imageResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUrls = itemView.findViewById(R.id.image);
            imageResult = itemView.findViewById(R.id.image_result);
        }
    }
}