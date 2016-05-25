package com.khinthirisoe.mymoviecollection;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by khinthirisoe on 3/20/16.
 */
public class GridViewAdapter extends ArrayAdapter<GridItem> {

    private Context context;
    private int layoutId;
    private ArrayList<GridItem> mGridData = new ArrayList<>();

    public GridViewAdapter(Context context, int resource, ArrayList<GridItem> mGridData) {
        super(context, resource, mGridData);
        this.context = context;
        this.layoutId = resource;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        Picasso.with(context).load(item.getImage()).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
