package com.wareroom.lib_base.widget.bottomnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wareroom.lib_base.R;

public class NormalTab extends BaseTab implements DragPointView.OnDragCallback {
    private OnMessageDragListener mOnDragListener;

    public NormalTab(@NonNull Context context) {
        this(context, null);
    }

    public NormalTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.dy_view_tab_normal_bottom_navigation, this, true);
        mIconImageView = findViewById(R.id.icon);
        mTitleTextView = findViewById(R.id.title);
        mMessagesView = findViewById(R.id.messages);
        mMessagesView.setDragListener(this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            mIconImageView.setImageResource(mCheckedDrawable);
            mTitleTextView.setTextColor(mCheckedTextColor);
        } else {
            mIconImageView.setImageResource(mDefaultDrawable);
            mTitleTextView.setTextColor(mDefaultTextColor);
        }
    }

    @Override
    public void setMessageNumber(int number) {
        mMessagesView.setMessageNumber(number);
    }

    @Override
    public void setHasMessage(boolean hasMessage) {
        mMessagesView.setHasMessage(hasMessage);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }


    @Override
    public void onDragOut() {
        if (mOnDragListener != null) {
            mOnDragListener.onDragOut(this);
            mMessagesView.setMessageNumber(0);
            mMessagesView.setHasMessage(false);
        }
    }

    public interface OnMessageDragListener {
        void onDragOut(BaseTab tab);
    }


    public void setOnDragListener(OnMessageDragListener onDragListener) {
        mOnDragListener = onDragListener;
    }
}
