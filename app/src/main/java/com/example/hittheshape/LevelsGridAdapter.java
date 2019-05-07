package com.example.hittheshape;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelsGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    public LevelsGridAdapter(Context con){
        this.context = con;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.opened);

        //int h = imageView.getLayoutParams().height;
        //int w = imageView.getLayoutParams().width;

        //Log.d("myLog", h+" "+w);

        TextView textView = convertView.findViewById(R.id.text_view);

        //textView.setBackgroundResource(R.drawable.opened);

        textView.setText("Lvl "+position);

        return convertView;
    }
}
