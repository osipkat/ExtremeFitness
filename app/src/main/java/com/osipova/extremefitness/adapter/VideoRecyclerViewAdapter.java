package com.osipova.extremefitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osipova.extremefitness.R;
import com.osipova.extremefitness.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Osipova Ekaterina on 26.01.2016.
 */
public class VideoRecyclerViewAdapter
        extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private final List<Video> mValues;

    public VideoRecyclerViewAdapter(List<Video> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Context context = holder.mPreviewView.getContext();
        Picasso.with(context).load(context.getString(R.string.url_youtube_preview,
                mValues.get(position).getV())).into(holder.mPreviewView);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mTextView.setText(Html.fromHtml(mValues.get(position).getText()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(context.getString(R.string.url_youtube_video, holder.mItem.getV()))));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPreviewView;
        public final TextView mTitleView;
        public final TextView mTextView;
        public Video mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPreviewView = (ImageView) view.findViewById(R.id.preview);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mTextView = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText() + "'";
        }
    }
}
