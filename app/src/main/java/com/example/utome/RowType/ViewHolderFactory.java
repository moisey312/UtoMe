package com.example.utome.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.utome.R;

public class ViewHolderFactory {

    public static class MapViewHolder extends RecyclerView.ViewHolder {

        public ImageView map;

        public MapViewHolder(View itemView) {
            super(itemView);
            map = itemView.findViewById(R.id.google_map);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        public TextView headerTextView;
        public TextView textView;

        public TextViewHolder(View itemView) {
            super(itemView);
            headerTextView = (TextView) itemView.findViewById(R.id.head);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

    }

    public static class NameViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public TextView name;
        public TextView rating;

        public NameViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.rv_main_name);
            rating = itemView.findViewById(R.id.rating);
        }
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PostInfoRowType.MAP_ROW_TYPE:
                View mapTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_type_map, parent, false);
                return new ViewHolderFactory.MapViewHolder(mapTypeView);

            case PostInfoRowType.TEXT_ROW_TYPE:
                View textTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_type_text, parent, false);
                return new ViewHolderFactory.TextViewHolder(textTypeView);

            case PostInfoRowType.NAME_ROW_TYPE:
                View nameTypeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_type_name, parent, false);
                return new ViewHolderFactory.NameViewHolder(nameTypeView);

            default:
                return null;
        }
    }
}
