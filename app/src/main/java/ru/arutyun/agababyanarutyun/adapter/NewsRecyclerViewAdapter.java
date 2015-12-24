package ru.arutyun.agababyanarutyun.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ru.arutyun.agababyanarutyun.R;
import ru.arutyun.agababyanarutyun.data.util.NewsUtils;
import ru.arutyun.agababyanarutyun.db.News;


public class NewsRecyclerViewAdapter
        extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<News> mNewsList = new ArrayList<>();
    private OnNewsClickListener mListener;

    public NewsRecyclerViewAdapter(@NonNull Context context) {
        mContext  = context;
    }

    public void addData(@NonNull List<News> newsList) {
        if (mNewsList.isEmpty()) {
            mNewsList.addAll(newsList);
        } else {
            final News lastNews = mNewsList.get(0);
            newsList = NewsUtils.filterByDate(newsList, lastNews.getPubDate());
            for(News news: newsList) {
                if (!lastNews.equals(news)) {
                    mNewsList.add(0, news);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void markAllRead() {
        for(News news: mNewsList) {
            news.setRead(true);
        }
        notifyDataSetChanged();
    }

    public void setOnNewsClickListener(@NotNull OnNewsClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final News news = mNewsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        if (!news.isRead()) {
            holder.tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tvTitle.setTypeface(Typeface.DEFAULT);
        }
        final String imageUrl = news.getImageUrl();
        if (imageUrl != null) {
            Glide.with(mContext)
                    .load(imageUrl)
                    .fitCenter()
                    .into(holder.ivImage);
        } else {
            // No photo -> load default icon
            Drawable defaultDrawable = null;
            switch (news.getSource()) {
                case LENTA:
                    defaultDrawable = ContextCompat.getDrawable(mContext, R.drawable.logo_lenta);
                    break;
                case GAZETA:
                    defaultDrawable = ContextCompat.getDrawable(mContext, R.drawable.logo_gazeta);
                    break;
            }
            if (defaultDrawable != null) {
                holder.ivImage.setImageDrawable(defaultDrawable);
            }
        }

        holder.cvRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    holder.tvTitle.setTypeface(Typeface.DEFAULT);
                    mListener.onNewsClick(news);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.cv_root)  CardView cvRoot;
        @InjectView(R.id.tv_title) TextView tvTitle;
        @InjectView(R.id.iv_image) ImageView ivImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

    }

    public interface OnNewsClickListener {
        void onNewsClick(News news);
    }
}
