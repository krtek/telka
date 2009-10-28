package cz.krtinec.telka.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

public class ScrollableListView extends ListView {
	private GestureDetector detector;
	
	public ScrollableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ScrollableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ScrollableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(!detector.onTouchEvent(event)) {
    		return super.onTouchEvent(event);
    	} else {
    		return true;
    	}
	}  
    
    public void setGestureDetector(GestureDetector detector) {
    	this.detector = detector;
    }  	
}
