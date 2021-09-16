package cn.dianyinhuoban.hm.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.wareroom.lib_base.widget.edittext.InputCodeView;

import java.util.Random;

import cn.dianyinhuoban.hm.R;
import cn.dianyinhuoban.hm.mvp.setting.view.ResetPayPasswordActivity;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;

public class PayPwdDialog extends Dialog implements InputCodeView.OnCodeCompleteListener {

    private String[] pwdKeyNum = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "back"};
    /**
     * 键盘是否随机
     */
    private boolean isNumRand = false;
    private OnInputCodeListener onInputCodeListener;

    public PayPwdDialog(@NonNull Context context) {
        this(context, 0);
    }

    public PayPwdDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.BottomDialog);
    }

    private InputCodeView mCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_password);
        mCodeText = findViewById(R.id.code_pwd_view);
        mCodeText.setOnCodeCompleteListener(this);
        mCodeText.setEnabled(false);
        findViewById(R.id.cancel_iv).setOnClickListener(v -> dismiss());
        findViewById(R.id.forgot_pwd_tv).setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), ResetPayPasswordActivity.class));
            dismiss();
        });
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        randNum();

        onBindKeyView(findViewById(R.id.key_layout), 3);
        setBottom();
    }

    /**
     * 设置键盘数字随机
     */
    private void randNum() {
        if (!isNumRand) {
            return;
        }
        for (int i = 0; i < pwdKeyNum.length; i++) {
            int rand = new Random().nextInt(11);
            //确保空数据和back数据位置不变
            if (i == 9 || i == 11 || rand == 9 || rand == 11) {
                continue;
            }
            String temp = pwdKeyNum[i];
            pwdKeyNum[i] = pwdKeyNum[rand];
            pwdKeyNum[rand] = temp;
        }
    }


    /**
     * 设置数字键盘是否随机
     *
     * @param isNumRand true 随机生成 false 正常显示
     * @return
     */
    public PayPwdDialog setNumRand(boolean isNumRand) {
        this.isNumRand = isNumRand;
        return this;
    }

    /**
     * 设置在底部打开
     */
    private void setBottom() {
        getWindow().setGravity(BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
    }


    /**
     * 数字键盘绑定数据
     */
    private void onBindKeyView(LinearLayout contentView, int num) {
        for (int index = 0; index < pwdKeyNum.length; index++) {
            String value = pwdKeyNum[index];
            if (index % num == 0) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                int height = dip2px(50f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(CENTER_HORIZONTAL);
                linearLayout.setBackgroundColor(Color.parseColor("#DDDDDD"));
                linearLayout.setLayoutParams(params);
                contentView.addView(linearLayout);
            }
            if (contentView.getChildAt(contentView.getChildCount() - 1) instanceof LinearLayout) {
                TextView customTv = new TextView(getContext());
                customTv.setTextSize(24f);
                customTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                customTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (value == "back") {
                    customTv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pwd_view_delete_bg));
                    customTv.setTag(value);
                } else if (value.isEmpty()) {
                    customTv.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    customTv.setText(value);
                }
                customTv.setGravity(CENTER);
                customTv.setTextColor(Color.parseColor("#333333"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                params.topMargin = 1;
                if (((index + 1) % num) != 0) {
                    params.rightMargin = 1;
                }
                customTv.setLayoutParams(params);
                customTv.setOnClickListener(tv -> {
                            if (!customTv.getText().toString().isEmpty()) {
                                mCodeText.addText(customTv.getText().toString());
                            }
                            if (customTv.getTag() == "back") {
                                mCodeText.removeText();
                            }
                        }
                );
                ((LinearLayout) contentView.getChildAt(contentView.getChildCount() - 1)).addView(customTv);
            }
        }

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return
     */
    private int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public PayPwdDialog setInputComplete(OnInputCodeListener onInputCodeListener) {
        this.onInputCodeListener = onInputCodeListener;
        return this;
    }


    @Override
    public void inputCodeComplete(String verificationCode) {
//        dismiss();
        if (onInputCodeListener != null) {
            onInputCodeListener.inputCodeComplete(this, verificationCode);
        }
    }

    @Override
    public void inputCodeInput(String verificationCode) {
        if (onInputCodeListener != null) {
            onInputCodeListener.inputCodeInput(this, verificationCode);
        }
    }

    public interface OnInputCodeListener {
        /**
         * 完成输入
         *
         * @param dialog
         * @param verificationCode
         */
        void inputCodeComplete(Dialog dialog, String verificationCode);

        /**
         * 未完成输入
         *
         * @param dialog
         * @param verificationCode
         */
        void inputCodeInput(Dialog dialog, String verificationCode);
    }
}
