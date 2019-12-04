package com.example.utome;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Response;

import java.util.List;
import java.util.ResourceBundle;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private List<Post> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public boolean isClickable = true;
    public Context con;

    ListRecyclerViewAdapter(Context context, List<Post> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        con = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = mData.get(position);
        holder.name.setText(mData.get(position).name);
        holder.descript.setText(mData.get(position).info);
        holder.catg_img.setBackgroundResource(con.getResources().getIdentifier("category_" + post.category, "drawable", con.getPackageName()));

        // if(position == mData.size()-1) holder.l.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name  = itemView.findViewById(R.id.rv_main_name);
        TextView descript  = itemView.findViewById(R.id.rv_main_info);
        ImageView catg_img  = itemView.findViewById(R.id.category);

        RelativeLayout l = itemView.findViewById(R.id.Post_layer);

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(isClickable) {
                if (mClickListener != null)
                    mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    Post getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
