package cn.dianyinhuoban.hm.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.dianyinhuoban.hm.R;
import cn.dianyinhuoban.hm.util.ToolUtil;

public class MessageDialog extends Dialog {

    private TextView tvTitle;
    private TextView tvContent;
    private String mTitle;
    private String mContent;
    private OnConfirmClickListener mOnConfirmClickListener;

    public MessageDialog(@NonNull Context context) {
        super(context, R.style.MessageDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        tvContent = findViewById(R.id.tv_content);
        tvTitle = findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }

        tvContent.setText(mContent);
        findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        findViewById(R.id.tv_ok).setOnClickListener(v -> {
            if (mOnConfirmClickListener != null) {
                mOnConfirmClickListener.onConfirmClick(this);
            } else {
                dismiss();
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = (int) (ToolUtil.getScreenWidth(getContext()) * 0.7f);
        window.setAttributes(layoutParams);
    }

    private MessageDialog setTitle(String title) {
        mTitle = title;
        if (tvTitle != null) {
            tvTitle.setText(mTitle);
            if (TextUtils.isEmpty(mTitle)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public MessageDialog setMessage(String message) {
        mContent = message;
        if (tvContent != null) {
            tvContent.setText(mContent);
        }
        return this;
    }

    public MessageDialog setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        mOnConfirmClickListener = onConfirmClickListener;
        return this;
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(MessageDialog dialog);
    }
}
