package com.ly.flower.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.ly.flower.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/24.
 */
public class FootprintTitleViewHolder {
    private ImageView ivClub;

    public FootprintTitleViewHolder(View parentView)
    {
        ivClub = (ImageView) parentView.findViewById(R.id.iv_club);
    }

    public void initData(JSONObject object)
    {
        try {
            String strBkUrl = object.getString("url_bk");
            ImageLoader.getInstance().displayImage(strBkUrl, ivClub);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
