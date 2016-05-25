package com.ly.flower.activity.club;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.ly.flower.R;
import com.ly.flower.base.BaseActivity;
import com.ly.flower.network.AscynHttpUtil;
import com.ly.flower.network.SendInfo;
import com.ly.flower.base.BaseFunction;
import com.ly.flower.share.MessageHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by admin on 2016/3/25.
 */
public class PostTopicActivity extends BaseActivity {
    private PostTopicActivity mInstance;
    private LayoutInflater inflater = null;

    private EditText etTitle ;
    private EditText etContent ;

    private HorizontalScrollView hsvImageView;
    private LinearLayout llImageView;
    private TextView tvImageInfo;

    private String cid = "";
    private ArrayList<String> sourceList = new ArrayList<>();
    private final int UPLOAD_IMAGE      = 1;
    private final int POST_TOPIC        = 2;


    @ Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode == MessageHandler.REQUEST_IMAGE && resultCode == RESULT_OK){
            sourceList = data.getStringArrayListExtra(
                    MultiImageSelectorActivity.EXTRA_RESULT);
            refreshImagesLayout();
        }
    }

    @Override
    public void init() {
        setView(R.layout.activity_post_topic);
        mInstance = this;
        inflater =(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
        fillView();
    }

    private void initView()
    {
        etTitle = (EditText) this.findViewById(R.id.et_title);
        etContent = (EditText) this.findViewById(R.id.et_content);

        tvImageInfo = (TextView) this.findViewById(R.id.tv_img_info);
        hsvImageView = (HorizontalScrollView) this.findViewById(R.id.hsv_images_view);
        llImageView = (LinearLayout) this.findViewById(R.id.ll_images_view);

        etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hsvImageView.setVisibility(View.VISIBLE);
                } else if (sourceList.size() <= 0) {
                    hsvImageView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void fillView() {
        cid = getIntent().getStringExtra("data");
        setTitle(R.string.str_post_topic);
        setLeftText(R.string.str_cancel);
        setRightText(R.string.str_send);
        refreshImagesLayout();
    }

    @Override
    public void doClickLeftTextAction() {
        finish();
    }

    @Override
    public void doClickRightTextAction() {
        if (!checkInputString())
            return;

        if (sourceList.size() > 0)
            uploadImage();
        else
            postTopic(new JSONArray());
    }

    private void refreshImagesLayout()
    {
        final int length = sourceList.size();
        llImageView.removeAllViews();
        if (length <= 0) {
            setImageEmpty();
            addSelectNewImage();
            return;
        }

        tvImageInfo.setText(getImagesInfo(length));
        tvImageInfo.setVisibility(View.VISIBLE);

        for (int i = 0; i < length; i++) {
            final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_selected_images, llImageView, false);
            final SelectImageViewHolder viewHolder = new SelectImageViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_image);
            viewHolder.ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            ImageLoader.getInstance().displayImage("file://" + sourceList.get(i), viewHolder.imageView);
            viewHolder.ivDelete.setTag(sourceList.get(i));
            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    llImageView.removeView(view);
                    removeImageFromArrayList((String) viewHolder.ivDelete.getTag());
                    tvImageInfo.setText(getImagesInfo(sourceList.size()));
                    if (sourceList.size() == 0) {
                        setImageEmpty();
                    }
                    else if (length == 9 && sourceList.size() == 8) {
                        addSelectNewImage();
                    }
                }
            });
            llImageView.addView(view);
        }//end for

        if (sourceList.size() < 9) {
            addSelectNewImage();
        }//end if
    }

    private void setImageEmpty()
    {
        tvImageInfo.setVisibility(View.GONE);
        llImageView.removeAllViews();
    }

    private void addSelectNewImage()
    {
        final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_selected_images, llImageView, false);
        final SelectImageViewHolder viewHolder = new SelectImageViewHolder();

        viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_image);
        viewHolder.ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.ivDelete.setVisibility(View.GONE);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                jumpSelectImageActivity(mInstance, MessageHandler.REQUEST_IMAGE, sourceList);
            }
        });
        llImageView.addView(view);
    }

    private String getImagesInfo(int len)
    {
        return "已选择" + String.valueOf(len) + "张图片，还可选择"+ String.valueOf(8 - len) + "张图片";
    }

    private void removeImageFromArrayList(String url)
    {
        for (int i = 0; i < sourceList.size(); i++) {
            if (url.equals(sourceList.get(i))) {
                sourceList.remove(i);
            }
        }
    }

    private void jumpSelectImageActivity(Activity instance, int request, ArrayList<String> defaultList)
    {
        Intent intent = new Intent(instance, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultList);
        instance.startActivityForResult(intent, request);
    }

    private boolean checkInputString()
    {
        String strTitle = etTitle.getText().toString();
        String strSubTitle = etContent.getText().toString();

        if (strTitle.trim().equals(""))
        {
            showAlertMessage(R.string.tip_title_empty);
            return false;
        }

        if (strSubTitle.trim().equals(""))
        {
            showAlertMessage(R.string.tip_content_empty);
            return false;
        }
        return true;
    }

    private void postTopic(JSONArray array)
    {
        String strTitle = etTitle.getText().toString();
        String strSubTitle = etContent.getText().toString();

        String strUrl = AscynHttpUtil.getAbsoluteUrlString(mInstance,
                AscynHttpUtil.URL_CLUB_ADD_TOPIC);
        String strInfo = SendInfo.getAddTopicClubSendInfo(mInstance, "0", cid, strTitle, strSubTitle, array);
        AscynHttpUtil.post(mInstance, strUrl, strInfo, getResponseHandler(POST_TOPIC));
        showProgressBar(R.string.tip_submiting);
    }

    private void uploadImage()
    {
        AscynHttpUtil.uploadMutilImage(mInstance, sourceList, getResponseHandler(UPLOAD_IMAGE));
        showProgressBar(R.string.tip_uploading);
    }

    private void cbPostTopic()
    {
        showInfoMessage(R.string.tip_post_success);
        finish();
    }

    private void cbUploadImage(byte[] responsebody)
    {
        try {
            JSONObject object = new JSONObject(new String(responsebody));
            JSONArray array = object.getJSONArray("data");
            postTopic(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ResponseHandlerInterface getResponseHandler(final int type)
    {
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] headers, byte[] responsebody) {

                if (BaseFunction.verifyResult(new String(responsebody), clSnackContainer)) {
                    switch (type)
                    {
                        case UPLOAD_IMAGE:
                            cbUploadImage(responsebody);
                            break;

                        case POST_TOPIC:
                            dismissProgressBar();
                            cbPostTopic();
                            break;
                    }
                }else {
                    dismissProgressBar();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] headers, byte[] responsebody, Throwable err) {
                dismissProgressBar();
                showAlertMessage(err.getMessage());
            }
        };
    }

    public class SelectImageViewHolder {
        public ImageView imageView;
        public ImageView ivDelete;
    }
}
