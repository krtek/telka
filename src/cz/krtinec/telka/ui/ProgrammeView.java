package cz.krtinec.telka.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.krtinec.telka.ImageCache;
import cz.krtinec.telka.ImageCache16;
import cz.krtinec.telka.R;
import cz.krtinec.telka.CompatibilityUtils;
import cz.krtinec.telka.dto.Programme;
import cz.krtinec.telka.dto.State;

import static cz.krtinec.telka.Constants.PROGRESS_WIDTH;
import static cz.krtinec.telka.ui.UIUtils.determinePercent;

public class ProgrammeView extends LinearLayout {

	private TextView title;
	private TextView desc;
	private TextView time;
	private ImageView icon;
	private ProgressIndicator progress;
	
	private Handler handler = new Handler();
		
	private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm"); 
	
	public ProgrammeView(Context context, Programme programme) {			
		super(context);
		//Log.d("ProgrammeView", programme.title + ":" + programme.start + " - " + programme.stop);
		
		this.setOrientation(HORIZONTAL);
		
		LinearLayout text = new LinearLayout(context);
		text.setOrientation(VERTICAL);
		
		
		icon = new ImageView(context);
		icon.setImageResource(R.drawable.telka);
		if (programme.iconURL != null && !"".equals(programme.iconURL)) {
			new IconLoaderThread(handler, icon, programme.iconURL).start();
		}
		
        addView(icon, new LinearLayout.LayoutParams(60, 80));
		       
        title = new TextView(context);
        title.setTextSize(20);
        title.setText(programme.title);
        
        final int textColor = determineColor(programme.getState());
		title.setTextColor(textColor);
        
		        
        //title.setTextColor(Color.LTGRAY);
        
        text.addView(title, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));       

        time = new TextView(context);
        time.setTextSize(18);
        time.setText(formatTime(programme.start, programme.stop));
        time.setTextColor(textColor);
        
        text.addView(time, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        progress = new ProgressIndicator(context, programme.getState().isRunning(), 
        		programme.getState().isRunning() ? determinePercent(programme.start, programme.stop) : 0);
                        
        text.addView(progress, LayoutParams.FILL_PARENT, PROGRESS_WIDTH);        
        
        desc = new TextView(context);
        desc.setText(programme.description);
        desc.setTextSize(14);
        desc.setTextColor(textColor);
        
        text.addView(desc, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
             
        addView(text, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));             
        
	}



	private static String formatTime(Date start, Date stop) {		
		return FORMAT.format(start) + " - " + FORMAT.format(stop);
	}
	
	private static int determineColor(final State state) {
      /*  if (state == State.RUNNING) {
        	return Color.WHITE;
        } else if (state == State.OVER) {
        	return Color.DKGRAY;
        } else {
        	return Color.LTGRAY;
        }
        */
		return Color.LTGRAY;
	}
	
	public void setProgramme(Programme p) {
		this.title.setText(p.title);
		final int textColor = determineColor(p.getState());		
		this.title.setTextColor(textColor);
		this.desc.setText(p.description);
		this.desc.setTextColor(textColor);
		this.time.setText(formatTime(p.start, p.stop));
		this.time.setTextColor(textColor);		
		this.progress.setRunning(p.getState().isRunning());
		if (p.getState().isRunning()) {
			this.progress.setPercent(determinePercent(p.start, p.stop));
		} else {
			this.progress.setPercent(0);			
		}
		this.invalidate();
		
		this.icon.setImageResource(R.drawable.telka);
		if (p.iconURL != null && !"".equals(p.iconURL)) {
			new IconLoaderThread(handler, this.icon, p.iconURL).start();
		}
	}

	class IconLoaderThread extends Thread {
		private Handler handler;
		private ImageView icon;
		private String url;
		public IconLoaderThread(Handler handler, ImageView icon, String url) {
			this.handler = handler;
			this.icon = icon;
			this.url = url;
		}
		@Override
		public void run() {
			final Drawable drawable;
			if (CompatibilityUtils.hasSettingTargetDensityOnBitmapDrawable()) {
				drawable = ImageCache16.getInstance().getImage(url, getContext());
			}else{
				drawable = ImageCache.getInstance().getImage(url, getContext());
			}
			handler.post(new Runnable() {

				public void run() {
					icon.setImageDrawable(drawable);					
				}
				
			});
		}	
	}
}

