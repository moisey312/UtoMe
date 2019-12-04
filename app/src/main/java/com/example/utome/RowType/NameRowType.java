package com.example.utome.RowType;

import androidx.recyclerview.widget.RecyclerView;
import com.example.utome.R;

public class NameRowType implements PostInfoRowType {

    private String text;
    private int rating;

    public NameRowType(String text, int rating) {
        this.text = text;
        this.rating = rating;
    }

    @Override
    public int getItemViewType() {
        return PostInfoRowType.NAME_ROW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolderFactory.NameViewHolder nameViewHolder = (ViewHolderFactory.NameViewHolder) viewHolder;
        nameViewHolder.name.setText(text);
        nameViewHolder.rating.setText("" + rating);
    }
}
