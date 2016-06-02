package com.ly.flower.activity.user;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.flower.R;
import com.ly.flower.adapter.MyClubAdapter;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.helper.MyItemTouchCallback;
import com.ly.flower.helper.OnRecyclerItemClickListener;
import com.ly.flower.share.MessageHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/25.
 */
public class UserInfoActivity extends BaseActivity {
    private LayoutInflater inflater = null;
    private ImageView ivBkg, ivBack, ivEdit;
    private RoundedImageView rivPortrait;
    private TextView tvNickName;
    private TextView tvSignature;
    private RecyclerView rvClubs;
    private MyClubAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private JSONObject userObject;

    @ Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                userObject = new JSONObject(data.getStringExtra("data"));
                refreshBaseInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void init() {
        setView(R.layout.activity_user_info);
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    private void initView()
    {
        ivBack = (ImageView) this.findViewById(R.id.iv_back);
        ivEdit = (ImageView) this.findViewById(R.id.iv_edit);
        ivBkg = (ImageView) this.findViewById(R.id.iv_bg);
        rivPortrait = (RoundedImageView) this.findViewById(R.id.riv_portrait);
        tvNickName = (TextView) this.findViewById(R.id.tv_nickname);
        tvSignature = (TextView) this.findViewById(R.id.tv_signature);
        rvClubs = (RecyclerView) this.findViewById(R.id.rv_images_club);
    }

    private void fillView() {
        hideTitleLayout();
        try {
            userObject = new JSONObject(new String(getIntent().getStringExtra("data")));
            refreshBaseInfo();
            JSONArray clubs = userObject.getJSONArray("clubs");
            refreshClubsLayout(clubs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                setResult(MessageHandler.RESULT_OK);
                finish();
                break;
            case R.id.iv_edit:
                gotoActivityForResult(EditUserInfoActivity.class, userObject.toString(), 0);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(MessageHandler.RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }


    private void refreshClubsLayout(JSONArray array)
    {
        rvClubs.removeAllViews();
        //设置固定大小
        rvClubs.setHasFixedSize(true);
        //创建布局
        mLayoutManager = new GridLayoutManager(this, 3);
        //给RecyclerView设置布局管理器
        rvClubs.setLayoutManager(mLayoutManager);
        //创建适配器，并且设置
        mAdapter = new MyClubAdapter(this, array);
        rvClubs.setAdapter(mAdapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mAdapter));
        itemTouchHelper.attachToRecyclerView(rvClubs);
        rvClubs.addOnItemTouchListener(new OnRecyclerItemClickListener(rvClubs) {
//            @Override
//            public void onLongClick(RecyclerView.ViewHolder vh) {
//                //长按
//                itemTouchHelper.startDrag(vh);
//            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                super.onItemClick(vh);
                //点击
                MyClubAdapter.ViewHolder myClubViewHolder = (MyClubAdapter.ViewHolder)vh;
                myClubViewHolder.tvName.setFocusable(true);
                myClubViewHolder.tvName.requestFocus();


                System.out.println("点击事件    " + myClubViewHolder.tvName.getText().toString());
            }
        });
    }

    private void refreshBaseInfo()
    {
        try {
            String strBkUrl = userObject.getString("bk_url");
            String strPortrait = userObject.getString("avatar");
            String strNickName = userObject.getString("nickname");
            String strSignature = userObject.getString("signature");

            tvNickName.setText(strNickName);
            tvSignature.setText(strSignature);
            if(!strBkUrl.equals("")) {
                imageLoader.displayImage(strBkUrl, ivBkg, imageOptions);
            }
            imageLoader.displayImage(strPortrait, rivPortrait, portraitOptions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
