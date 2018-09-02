package com.simple.flowradiogroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * 1.测量宽度
 * 宽度为父布局的宽度，或者确定的值
 * 2.测量高度
 */

public class FlowRadioGroup extends RadioGroup {

    private final String TAG = this.getClass().getSimpleName();

    public FlowRadioGroup(Context context) {
        super(context);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int lineWidth = 0;
        int lineHeight = 0;
        int heightSize = 0;

        int count = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View childView;
        for (int i = 0; i < count; i++) {
            childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams params = ((MarginLayoutParams) childView.getLayoutParams());
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > size - getPaddingRight() - getPaddingLeft()) {//要换行了
                lineWidth = childWidth;
                heightSize += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == count - 1) {
                heightSize += lineHeight;
            }
        }

        setMeasuredDimension(size,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec) :
                        heightSize + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int heightSize = 0;
        View childView;

        for (int i = 0; i < count; i++) {
            childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            MarginLayoutParams params = ((MarginLayoutParams) childView.getLayoutParams());
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            if (lineWidth + childWidth + params.leftMargin + params.rightMargin >
                    width - getPaddingLeft() - getPaddingRight()) {//要换行了
                heightSize += lineHeight;
                lineWidth = 0;
            } else {
                lineHeight = Math.max(lineHeight, childHeight + params.topMargin + params.bottomMargin);
            }

            int left = lineWidth + params.leftMargin;
            int top = heightSize + params.topMargin;
            int right = left + childWidth;
            int bottom = top + childHeight;

            childView.layout(left, top, right, bottom);
            lineWidth += childWidth + params.leftMargin + params.rightMargin;
        }
    }
}
