package com.example.utome.RowType;

import androidx.recyclerview.widget.RecyclerView;

public class MapRowType implements PostInfoRowType {

    public MapRowType() {
    }

    @Override
    public int getItemViewType() {
        return PostInfoRowType.MAP_ROW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolderFactory.MapViewHolder mapViewHolder = (ViewHolderFactory.MapViewHolder) viewHolder;
    }
}
