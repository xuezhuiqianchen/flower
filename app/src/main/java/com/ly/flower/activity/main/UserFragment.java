package com.ly.flower.activity.main;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.common.utils.DimensionUtils;
import com.ly.flower.R;
import com.ly.flower.activity.user.MySuggestActivity;
import com.ly.flower.activity.user.SettingActivity;
import com.ly.flower.activity.user.MyCommentActivity;
import com.ly.flower.activity.user.MyTopicActivity;
import com.ly.flower.activity.user.UserInfoActivity;
import com.ly.flower.base.BaseFragment;
import com.ly.flower.memory.GlobalStatic;
import com.ly.flower.base.DataStructure;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/15.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener{
    public final static String TAG = "UserFragment";
    private final static int[] IMAGE_ID_LIST = {R.drawable.personal_topic_icon,
            R.drawable.personal_comment_icon, R.drawable.personal_suggest_icon,
            R.drawable.personal_set_icon};

    private final static int[] STRING_ID_LIST = {R.string.str_my_topic, R.string.str_my_comment,
            R.string.str_my_suggest, R.string.str_setting};


    private RelativeLayout rlUserInfo;
    private RoundedImageView rivPortrait;
    private TextView tvNickname;
    private TextView tvHometown;

    private RelativeLayout rlMyTopic;
    private RelativeLayout rlMyComment;
    private RelativeLayout rlMySuggest;
    private RelativeLayout rlSetting;

    private TextView tvNum;

    public UserFragment(MainActivity instance, LayoutInflater inflater) {
        super(instance, inflater);
    }

    @Override
    public void initView() {
        rlFragmentView = (RelativeLayout) inflater.inflate(R.layout.fragment_user, null);
        rlUserInfo = (RelativeLayout) rlFragmentView.findViewById(R.id.rl_user_info);
        rivPortrait = (RoundedImageView) rlFragmentView.findViewById(R.id.riv_portrait);
        tvNickname = (TextView) rlFragmentView.findViewById(R.id.tv_nickname);
        tvHometown = (TextView) rlFragmentView.findViewById(R.id.tv_hometown);
        tvNum = (TextView) rlFragmentView.findViewById(R.id.tv_num);
        initListView();

        rlUserInfo.setOnClickListener(this);
    }

    @Override
    public void getData() {
    }

    @Override
    public void refreshView() {
        rivPortrait.setCornerRadius((float) DimensionUtils.dip2px(mInstance, 100));
        String strUserInfo = GlobalStatic.getSharedString(mInstance, DataStructure.uid);
        try {
            JSONObject object = new JSONObject(strUserInfo);
            String strPortrait = object.getString("avatar");
            tvNickname.setText(object.getString("nickname"));
            tvHometown.setText(object.getString("hometown"));
            tvNum.setText("NO." + object.getString("number"));

            ImageLoader.getInstance().displayImage(strPortrait, rivPortrait);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListView()
    {
        rlMyTopic = (RelativeLayout) rlFragmentView.findViewById(R.id.v_my_topic);
        rlMyComment = (RelativeLayout) rlFragmentView.findViewById(R.id.v_my_comment);
        rlMySuggest = (RelativeLayout) rlFragmentView.findViewById(R.id.v_my_suggest);
        rlSetting = (RelativeLayout) rlFragmentView.findViewById(R.id.v_setting);

        rlMyTopic.setOnClickListener(this);
        rlMyComment.setOnClickListener(this);
        rlMySuggest.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

        ArrayList<RelativeLayout> relativeLayoutList = new ArrayList<>();
        relativeLayoutList.add(rlMyTopic);
        relativeLayoutList.add(rlMyComment);
        relativeLayoutList.add(rlMySuggest);
        relativeLayoutList.add(rlSetting);

        for (int i = 0; i < 4; i++)
        {
            TextView tvName = (TextView) relativeLayoutList.get(i).findViewById(R.id.tv_name);
            ImageView ivIcon = (ImageView)relativeLayoutList.get(i).findViewById(R.id.iv_icon);
            ivIcon.setImageResource(IMAGE_ID_LIST[i]);
            tvName.setText(mInstance.getString(STRING_ID_LIST[i]));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_user_info:
                mInstance.gotoActivity(UserInfoActivity.class);
                break;

            case R.id.v_my_topic:
                mInstance.gotoActivity(MyTopicActivity.class);
                break;

            case R.id.v_my_comment:
                mInstance.gotoActivity(MyCommentActivity.class);
                break;

            case R.id.v_my_suggest:
                mInstance.gotoActivity(MySuggestActivity.class);
                break;

            case R.id.v_setting:
                mInstance.gotoActivity(SettingActivity.class);
                break;
        }
    }
}
