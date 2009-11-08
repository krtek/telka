package cz.krtinec.telka.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

import static cz.krtinec.telka.Constants.PROGRESS_WIDTH;

public class ProgressIndicator extends View {
	private ShapeDrawable outer;
	private ShapeDrawable inner;
	private ShapeDrawable progress;
		
	private Rect maxSize = new Rect();
	private Rect innerSize = new Rect();
	private boolean running;
	private int percent;
		
	public ProgressIndicator(Context context, boolean running, int percent) {
		super(context);
		this.running = running;
		this.percent = percent;
		outer = new ShapeDrawable(new RectShape());
		outer.getPaint().setColor(Color.LTGRAY);
		inner = new ShapeDrawable(new RectShape());
		inner.getPaint().setColor(Color.BLACK);
		progress = new ShapeDrawable(new RectShape());
		progress.getPaint().setColor(Color.LTGRAY);
	}

	protected void onDraw(Canvas canvas) {
        outer.draw(canvas);
        inner.draw(canvas);
        progress.draw(canvas);
    }

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			maxSize.set(5, 0, right - 15, PROGRESS_WIDTH);
			outer.setBounds(maxSize);
			innerSize = determineInnerBounds(maxSize);
			inner.setBounds(innerSize);	
			Rect progressRect = determineProgressBounds(innerSize);
			if (progressRect != null) {
				this.progress.setBounds(progressRect);
			}
		}
	}

	private Rect determineProgressBounds(Rect size) {
		if (!running) {
			return null;
		}
		int length = size.right - size.left;
		int right = size.left + ((length / 100) * percent); 
				
		return new Rect(size.left, size.top, right, size.bottom);
	}

	private static Rect determineInnerBounds(Rect size) {
		return new Rect(size.left + 1, size.top + 1, size.right - 1, size.bottom - 1);
	}
	
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
