package com.example.hittheshape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelsGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int unlockedLevels=1;

    public LevelsGridAdapter(Context con,int level){
        this.context = con;
        if(level!=0) this.unlockedLevels=level;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // according to position return here true or false to enable or disable respectively
        if(position<unlockedLevels){
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return Configuration.numberOfLevels;
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

        //TODO set proper image to unlocked und locked lvl
        if(position<unlockedLevels) {
            imageView.setImageResource(R.drawable.opened);
        }
        else {
            imageView.setImageResource(R.drawable.locked);
            imageView.setAlpha(0.6f);

        }

        TextView textView = convertView.findViewById(R.id.text_view);
        textView.setText(Integer.toString(position+1));

        return convertView;
    }
}
