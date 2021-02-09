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

import com.vk.sdk.api.model.VKList;

import org.xml.sax.helpers.ParserAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adaptertext  extends RecyclerView.Adapter<Adaptertext.ViewHolder> {
    private int staticTag=0;
    private final LayoutInflater inflater;
    private final ArrayList<String> texts;

    Adaptertext(Context context, ArrayList<String> texts) {
        this.texts = texts;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public Adaptertext.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        view.setTag(staticTag);
        staticTag++;
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(Adaptertext.ViewHolder holder, int position) {
        texts.get(position);
        String text = texts.get(position); ;
        holder.textView.setText(text);
        holder.textView.setTag(staticTag-1);
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        ViewHolder(View view){
            super(view);
            textView=(TextView) view.findViewById(android.R.id.text1);

        }
    }
}
