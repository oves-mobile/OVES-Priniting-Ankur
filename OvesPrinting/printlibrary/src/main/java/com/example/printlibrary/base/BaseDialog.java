package com.example.printlibrary.base;

import android.app.Activity;
import android.app.Dialog;

import com.example.printlibrary.R;


/***************************************
 *
 * @date 2017-6-14
 * @time 2:39:01 PM Class description: basic dialog
 * 
 **************************************/
public abstract class BaseDialog extends Dialog implements
		android.view.View.OnClickListener {
	/**
	 * @param context
	 */
	protected Activity context;

	public BaseDialog(Activity context) {
		super(context, R.style.Dialog);
		this.context = context;
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}
	
	public BaseDialog(Activity context, String transparent) {//Transparent background
        super(context, R.style.Dialog_transparent);
        this.context = context;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

	public BaseDialog(Activity context, int resId) {
		super(context, R.style.dialog_bottom);
		this.context = context;
		setContentView(resId);
	}

	public BaseDialog(Activity context, int resId, int styleId) {
		super(context, styleId);
		this.context = context;
		setContentView(resId);
	}

	public void init() {
		initView();
		initData();
		initListener();
	}

	public void initView() {
	}

	public void initData() {
	}

	public void initListener() {
	}
}
