package com.ly.flower.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.flower.R;
import com.ly.flower.helper.MyItemTouchCallback;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by panji on 2016/5/31.
 */
public class MyClubAdapter extends RecyclerView.Adapter<MyClubAdapter.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {
    private LayoutInflater  mInflater;
    private JSONArray       mArray;

    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    @Override
    public void onMove(int fromPosition, int toPosition) {
        System.out.println("onMove");
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {

    }

    public MyClubAdapter(Context context, JSONArray array){
        this.mInflater=LayoutInflater.from(context);
        this.mArray = array;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_image)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_image) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                // .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                //.displayer(new RoundedBitmapDisplayer(0))// 不推荐用！！！！是否设置为圆角，弧度为多少
                //.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间，可能会出现闪动
                .build();
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_my_club,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.initData(mArray.getJSONObject(position));
        } catch (JSONException e) {
            System.out.println("onBindViewHolder Crash!");
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        private RoundedImageView rivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.tv_name);
            rivImage = (RoundedImageView) itemView.findViewById(R.id.riv_image);
        }

        public void initData(JSONObject object)
        {
            try {
                String strBkUrl = object.getString("url_bk");
                String strTitle = object.getString("cname");

                if(tvName != null) {
                    tvName.setText(strTitle);
                }

                if(rivImage != null) {
                    imageLoader.displayImage(strBkUrl, rivImage, imageOptions);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
