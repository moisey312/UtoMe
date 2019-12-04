package com.example.utome.RowType;


import androidx.recyclerview.widget.RecyclerView;

public class TextRowType implements PostInfoRowType {

    private String header;
    private String text;

    public TextRowType(String header, String text) {
        this.header = header;
        this.text = text;
    }

    @Override
    public int getItemViewType() {
        return PostInfoRowType.TEXT_ROW_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolderFactory.TextViewHolder textViewHolder = (ViewHolderFactory.TextViewHolder) viewHolder;
        textViewHolder.headerTextView.setText(header);
        textViewHolder.textView.setText(text);
    }
}
