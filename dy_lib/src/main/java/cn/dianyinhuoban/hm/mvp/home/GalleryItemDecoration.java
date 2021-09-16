package cn.dianyinhuoban.hm.mvp.home;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = "GalleryItemDecoration";

    public static int mPageMargin = 5;          // 每一个页面默认页边距
    public static int mLeftPageVisibleWidth = 10; // 中间页面左右两边的页面可见部分宽度

    public static int mItemComusemY = 0;
    public static int mItemComusemX = 0;

    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //这个类的方法应该只会被调用k次 是一个专门对item操作的类

        final int position = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter().getItemCount();

        parent.post(() -> {
            LinearLayoutManager lm = (LinearLayoutManager) parent.getLayoutManager();

            if (lm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                onSetHoritiontalParams(parent, view, position, itemCount);
            } else {
                onSetVerticalParams(parent, view, position, itemCount);
            }
        });
    }

    private void onSetVerticalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth();
        int itemNewHeight = parent.getHeight() - dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);

        mItemComusemY = itemNewHeight + dpToPx(2 * mPageMargin);

        // 适配第0页和最后一页没有左页面和右页面，让他们保持左边距和右边距和其他项一样
        int topMargin = position == 0 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);
        int bottomMargin = position == itemCount - 1 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);

        setLayoutParams(itemView, 0, topMargin, 0, bottomMargin, itemNewWidth, itemNewHeight);
    }

    private void onSetHoritiontalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        //注意 我们xml中定义了padding，所以才没有贴顶
        int itemNewWidth = parent.getWidth() - dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
        int itemNewHeight = parent.getHeight();

        mItemComusemX = itemNewWidth + dpToPx(2 * mPageMargin);//这个引用啥意思 平均单个item整个的宽度

        //mPageMargin就是页之间的边距 默认是0 由于有padding 所以没有贴在一起 我觉得设计的非常不合理 但是无伤大雅

        int leftMargin = position == 0 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : dpToPx(mPageMargin);

        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth, int itemHeight) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;
        }

        // 这里的if考虑到的是缩放动画情况
        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
