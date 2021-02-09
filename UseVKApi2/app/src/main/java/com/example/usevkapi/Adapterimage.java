package com.example.usevkapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapterimage  extends RecyclerView.Adapter<Adapterimage.ViewHolder> {
    private int staticTag=0;
    private final LayoutInflater inflater;
    private final ArrayList<Bitmap> bitmaps;

    Adapterimage(Context context, ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public Adapterimage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.newsfeedpost, parent, false);
        view.setTag(staticTag);
        staticTag++;
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(Adapterimage.ViewHolder holder, int position) {
        Bitmap bitmap = bitmaps.get(position);
        System.out.println("5555 "+bitmaps.get(position).toString());
        holder.image.setImageBitmap(bitmap);
        holder.image.setTag(staticTag-1);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        ViewHolder(View view){
            super(view);
            image=(ImageView) view.findViewById(R.id.image);

        }
    }
}
