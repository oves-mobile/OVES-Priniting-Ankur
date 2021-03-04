package com.example.printlibrary.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.printlibrary.R;

/**
 * Created by jc on 2017/11/4.
 *
 */

@SuppressLint("AppCompatCustomView")
public class TabButton extends Button {
    private int normal_bg_res;
    private int selected_bg_res;

    public TabButton(Context context) {
        super(context);
    }

    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
        normal_bg_res = typeArray.getResourceId(R.styleable.TabButton_normal_bg_res, 0);
        selected_bg_res = typeArray.getResourceId(R.styleable.TabButton_selected_bg_res, 0);

        typeArray.recycle();
    }

    /*
     * This is originally intended to customize a method to be called in Activity,
     * But after writing, I found that there is already a method with the same name in the TextView of Button’s parent class,
     *  so the customization becomes an overwrite, but it doesn’t matter and does not affect the effect
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setBackgroundResource(selected_bg_res);
            setTextColor(Color.WHITE);
        } else {
            setBackgroundResource(normal_bg_res);
            setTextColor(Color.GRAY);
        }
    }

}