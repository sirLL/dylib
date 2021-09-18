package com.wareroom.lib_base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.wareroom.lib_base.R;
import com.wareroom.lib_base.widget.ProgressView;


public class ProgressDialog extends Dialog {
    private ProgressView progressWheel;
    private TextView tvMessage;
    private String message;

    public ProgressDialog(Context context) {
        this(context, "");
    }

    public ProgressDialog(Context context, String message) {
        super(context, R.style.DYPromptDialog);
        this.message = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_base_dialog_progress);
        progressWheel = findViewById(R.id.progressBarTwo);
        tvMessage = findViewById(R.id.tv_notice);
        tvMessage.setText(TextUtils.isEmpty(message) ? "加载中..." : message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressWheel.resetCount();
        progressWheel.setText("电银");
        progressWheel.startSpinning();
    }


    public void setMessage(String message) {
        this.message = message;
        if (tvMessage != null) {
            tvMessage.setText(TextUtils.isEmpty(message) ? "加载中..." : message);
        }
    }



}
