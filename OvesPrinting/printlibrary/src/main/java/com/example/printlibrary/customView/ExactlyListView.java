package com.example.printlibrary.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***************************************
 * 
 * @author lwli
 * @modify zhengjb
 * @date 2014-9-1 
 * @time 12:30:15
 * Class description: Display the largest ListView without sliding
 * 
 **************************************/
public class ExactlyListView extends ListView {
	public ExactlyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ExactlyListView(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	      int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
