package cz.krtinec.telka;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class ImageCache {
	private static ImageCache instance = new ImageCache();
	
	private Map<String, Drawable> cache;

	protected ImageCache() {
		this.cache = new WeakHashMap<String, Drawable>();
	}
	
	public static ImageCache getInstance() {
		return instance;
	}
	
	public Drawable getImage(String url, Context ctx) {
		Drawable d = cache.get(url);
		if (d == null) {
			d = this.fetchImage(url, ctx);			
			cache.put(url, d);
		}
		return d;
	}
	
	protected Drawable fetchImage(String iconUrl) {
		return this.fetchImage(iconUrl, null);
	}
	
	private Drawable fetchImage(String iconUrl, Context ctx) {
		
		URL url;
		try {
			url = new URL(iconUrl);
			InputStream is = (InputStream) url.getContent();
			Drawable d = Drawable.createFromStream(is, "src");				
			return d;

		} catch (IOException e) {
			Log.e("Main", "IOException while fetching...");
			Drawable d = Drawable.createFromPath("res/telka.png");
			return d;
		}
	}
}
