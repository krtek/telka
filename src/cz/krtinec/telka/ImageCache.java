package cz.krtinec.telka;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.WeakHashMap;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class ImageCache {
	private static ImageCache instance = new ImageCache();
	
	private Map<String, Drawable> cache;

	private ImageCache() {
		this.cache = new WeakHashMap<String, Drawable>();
	}
	
	public static ImageCache getInstance() {
		return instance;
	}
	
	public Drawable getImage(String url) {
		Drawable d = cache.get(url);
		if (d == null) {
			d = fetchImage(url);			
			cache.put(url, d);
		}
		return d;
	}
	
	private Drawable fetchImage(String iconUrl) {
		Log.i("Main", "fetching " + iconUrl);
		
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
