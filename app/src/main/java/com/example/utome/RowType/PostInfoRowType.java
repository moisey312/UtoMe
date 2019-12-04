package com.example.utome.RowType;

import androidx.recyclerview.widget.RecyclerView;

public interface PostInfoRowType {
    int TEXT_ROW_TYPE = 0;
    int MAP_ROW_TYPE = 1;
    int NAME_ROW_TYPE = 2;

    int getItemViewType();

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder);

}
