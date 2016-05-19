package com.flying.xiaopo.swipepostcard.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flying.xiaopo.swipepostcard.R;
import com.flying.xiaopo.swipepostcard.SwipePostcard;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Flying SnowBean on 2016/1/26.
 */
public class PostcardAdapter extends SwipePostcard.Adapter {
    private final String TAG = PostcardAdapter.class.getSimpleName();
    private Context mContext;
    private List<Bean> mData;

    public PostcardAdapter(Context context, List<Bean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_postcard, parent, false);
    }

    @Override
    public void bindView(View view, final int position) {
        Bean bean = mData.get(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Picasso.with(mContext).load(bean.getResId()).into(holder.mIvTaeyeon);
        holder.mTvDescribe.setText(bean.getText());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "You clicked -->" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }

    static class ViewHolder {
        ImageView mIvTaeyeon;
        TextView mTvDescribe;
        Button mButton;

        ViewHolder(View view) {
            mIvTaeyeon = (ImageView) view.findViewById(R.id.iv_taeyeon);
            mTvDescribe = (TextView) view.findViewById(R.id.tv_describe);
            mButton = (Button) view.findViewById(R.id.btn_event);
        }
    }
}
