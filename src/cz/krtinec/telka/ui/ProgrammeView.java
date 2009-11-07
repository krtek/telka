package cz.krtinec.telka.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.krtinec.telka.Constants;
import cz.krtinec.telka.ImageCache;
import cz.krtinec.telka.ImageCache16;
import cz.krtinec.telka.R;
import cz.krtinec.telka.CompatibilityUtils;
import cz.krtinec.telka.dto.Programme;
import cz.krtinec.telka.dto.State;

public class ProgrammeView extends LinearLayout {

	private TextView title;
	private TextView desc;
	private TextView time;
	private ImageView icon;
	
	private Handler handler = new Handler();
		
	private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm"); 
	
	public ProgrammeView(Context context, Programme programme) {		
		super(context);
		
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
        /*
        if (programme.state == State.RUNNING) {
        	title.setTextColor(Color.WHITE);
        } else if (programme.state == State.OVER) {
        	title.setTextColor(Color.DKGRAY);
        } else {
        	title.setTextColor(Color.LTGRAY);
        }
        */
        
        title.setTextColor(Color.LTGRAY);
        
        text.addView(title, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        time = new TextView(context);
        time.setTextSize(18);
        time.setText(formatTime(programme.start, programme.stop));
        text.addView(time, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        desc = new TextView(context);
        desc.setText(programme.description);
        desc.setTextSize(14);
        text.addView(desc, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
             
        addView(text, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private String formatTime(Date start, Date stop) {		
		return FORMAT.format(start) + " - " + FORMAT.format(stop);
	}
	
	public void setProgramme(Programme p) {
		this.title.setText(p.title);
		this.desc.setText(p.description);
		this.time.setText(formatTime(p.start, p.stop));		
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

