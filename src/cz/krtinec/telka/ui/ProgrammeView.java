package cz.krtinec.telka.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cz.krtinec.telka.ImageCache;
import cz.krtinec.telka.ImageCache16;
import cz.krtinec.telka.R;
import cz.krtinec.telka.CompatibilityUtils;
import cz.krtinec.telka.dto.Programme;
import cz.krtinec.telka.dto.State;

import static cz.krtinec.telka.ui.UIUtils.determinePercent;

public class ProgrammeView extends LinearLayout {
	

	private TextView title;
	private TextView desc;
	private TextView time;
	private ImageView icon;
	private ProgressBar progress;
	
	
	private Handler handler = new Handler();
		
	private static final DateFormat FORMAT = new SimpleDateFormat("HH:mm"); 
	
	public ProgrammeView(Context context, Programme programme) {			
		super(context);
		 LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View v = vi.inflate(R.layout.row, null);
		 title = (TextView) v.findViewById(R.id.title);
		 title.setText(programme.title);
		 desc = (TextView) v.findViewById(R.id.desc);
		 desc.setText(programme.description);
		 time = (TextView) v.findViewById(R.id.time);
		 time.setText(formatTime(programme.start, programme.stop));
		 icon = (ImageView) v.findViewById(R.id.Icon);
         if (programme.iconURL != null && !"".equals(programme.iconURL)) {
             new IconLoaderThread(handler, icon, programme.iconURL).start();
         }
		 progress = (ProgressBar) v.findViewById(R.id.progress);
		 if (programme.getState().isRunning()) {
			 progress.setProgress(determinePercent(programme.start, programme.stop));
		 } else {			 
			 progress.setProgress(0);
		 }
		 addView(v);
	}



	private static String formatTime(Date start, Date stop) {		
		return FORMAT.format(start) + " - " + FORMAT.format(stop);
	}
		
	public void setProgramme(Programme p) {
		this.title.setText(p.title);	
		this.desc.setText(p.description);
		this.time.setText(formatTime(p.start, p.stop));		
		if (p.getState().isRunning()) {			
			this.progress.setProgress(determinePercent(p.start, p.stop));
		} else {			
			this.progress.setProgress(0);
		}		
		
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

