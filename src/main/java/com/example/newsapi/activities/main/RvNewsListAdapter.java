package com.example.newsapi.activities.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapi.R;
import com.example.newsapi.dto.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RvNewsListAdapter extends RecyclerView.Adapter<RvNewsListAdapter.ViewHolder> {

    List<Article> articleList;
    Context parentContext = null;

    public RvNewsListAdapter(List<Article> articleList, Context parentContext) {
        this.articleList = articleList;
        this.parentContext = parentContext;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivPhoto;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(parentContext).inflate(R.layout.rv_news_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(articleList.get(position).getTitle());
        holder.tvDescription.setText(articleList.get(position).getDescription());
        Picasso.with(parentContext).load(articleList.get(position).getUrlToImage()).into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}