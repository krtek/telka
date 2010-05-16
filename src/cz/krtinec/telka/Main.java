package cz.krtinec.telka;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import cz.krtinec.telka.dto.Channel;
import cz.krtinec.telka.dto.Programme;
import cz.krtinec.telka.ui.ProgrammeView;
import cz.krtinec.telka.ui.ScrollableListView;
import cz.krtinec.telka.ui.TelkaPreferences;

import static cz.krtinec.telka.Constants.CZECH;

public class Main extends ListActivity {
	private static final String CURRENT_CHANNEL_KEY = "currentChannel";
	private IProgrammeProvider provider;
	private Channel[] channels;
	private Integer currentChannel;
	private TextView channel;
	private GestureDetector detector;
	ProgressDialog dialog;
	private Handler handler = new Handler();
	
	private static final int RELOAD_MENU = 0;
	private static final int PREFS_MENU = 1;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        setContentView(R.layout.main);        
        dialog = ProgressDialog.show(this, "Telka", getString(R.string.loading), true);
        //requestWindowFeature(Window.FEATURE_PROGRESS);

        currentChannel = 0;
        if (savedInstanceState != null) {
        	currentChannel = savedInstanceState.getInt(CURRENT_CHANNEL_KEY);
        }
        
        new StartupThread(this).start();
        
        this.detector = new GestureDetector(this, new MyGesturesDetector(this));
        this.detector.setIsLongpressEnabled(true);        
    }  
    
    
    
        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, RELOAD_MENU, 0, R.string.reload).setIcon(R.drawable.ic_menu_refresh);
		menu.add(Menu.NONE, PREFS_MENU, 1, R.string.preferences).setIcon(android.R.drawable.ic_menu_preferences);		
		return true;
	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case RELOAD_MENU: { 		
			final ProgressDialog dialog = ProgressDialog.show(this, "Telka", getString(R.string.loading), true);
			final Context context = this;
			Runnable reloadThread = new Runnable() {
				@Override
				public void run() {
					ProviderFactory.getProvider(context).reload();					
					dialog.cancel();
				}				
			};			
			new Thread(reloadThread).start();
			
			return true;			
		}
		case PREFS_MENU: {
			Intent settingsActivity = new Intent(getBaseContext(), TelkaPreferences.class);			
			startActivity(settingsActivity);
			return true;
		}}
		return false;
		
	}




	@Override
	protected void onStart() {
		super.onStart();
		/*
		 * To enable tracing, android.permission.WRITE_EXTERNAL_STORAGE must be set to true! 
		 */
		//Debug.startMethodTracing("telka");
	}



	@Override
	protected void onStop() {
		super.onStop();
		//Debug.stopMethodTracing();		
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_CHANNEL_KEY, currentChannel);
	}	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Programme p = provider.getProgrammes(channels[currentChannel]).get(info.position);
        String nInterval = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("notification.interval", "5");
        
        addNotification(p, Integer.parseInt(nInterval) * 60 * 1000);
        //Toast.makeText(this, "Item: " + p.title, Toast.LENGTH_SHORT).show();        	
        return true; 
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.context_menu_header);
        menu.add(R.string.add_notification); 
	}

	private void scrollToCurrentTime(ListView lw, int position) {
		lw.setSelection(position);
	}
	
	private void addNotification(Programme p, int notificationInterval) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra(Constants.SHOW_NAME, p.title);
		intent.putExtra(Constants.SHOW_START_TIME, p.start.getTime());
		intent.putExtra(Constants.SHOW_ID, p.hashCode());
		intent.setData((Uri.parse("custom://"+p.start.getTime()))); 
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		//Log.i("Main", "PendingIntent: " + pendingIntent);
		
		Log.i("Main", "Will notify " + notificationInterval + " [ms] before start.");
		am.set(AlarmManager.RTC_WAKEUP, p.start.getTime() - notificationInterval, pendingIntent);
		//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 60 * 1000, pendingIntent);
		Toast.makeText(this, R.string.added_notification, Toast.LENGTH_SHORT).show(); 
	}

	class StartupThread extends Thread {
		private Context context;
		private int reloadInterval;
		
		public StartupThread(Context context) {
			this.context = context;
			String sInterval = PreferenceManager.getDefaultSharedPreferences(context).getString("reload.interval", "6");
			this.reloadInterval = Integer.parseInt(sInterval) * 60 * 60 * 1000;
		}

		@Override
		public void run() {
	        provider = ProviderFactory.getProvider(context);
	        
	        final Collection<Channel> enabledChannels = provider.getEnabledChannels(this.reloadInterval);
			channels = enabledChannels.toArray(
	        		new Channel[enabledChannels.size()]);
	        
	        Arrays.sort(channels, new Comparator<Channel>() {
				public int compare(Channel o1, Channel o2) {
					Collator c = Collator.getInstance(CZECH);
					return c.compare(o1.displayName, o2.displayName);
				}       
	        });
	        handler.post(new Runnable() {

				public void run() {
			        channel = (TextView) findViewById(R.id.channel);
			        channel.setText(channels[currentChannel].displayName);               
			        setListAdapter(new TVListAdapter(context, provider.getProgrammes(channels[currentChannel])));
			        ((ScrollableListView)findViewById(android.R.id.list)).setGestureDetector(detector);
			        scrollToCurrentTime((ListView)findViewById(android.R.id.list), provider.nowPlaying(channels[currentChannel]));
			        registerForContextMenu((ListView)findViewById(android.R.id.list));
			        dialog.cancel();
				}
	        	
	        });
		}
		
	}


	class MyGesturesDetector extends GestureDetector.SimpleOnGestureListener {
		private long lastCall = 0; 
		Context context = null;
		
		public MyGesturesDetector(Context context) {
			this.context = context;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,				
				float distanceX, float distanceY) {						
			if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > 10) {
				long now = System.currentTimeMillis();							
				if ((now - lastCall) < 300) {
					//ignore this call - it's too soon
					Log.i("Gestures", "Last called " + (now - lastCall) + "ms ago, ignored!");
					return false;
				}
				lastCall = now;
				if (distanceX>0) {
					//move right
					currentChannel++;
					if (currentChannel > channels.length - 1) {
						currentChannel = channels.length - 1;
					}
				} else {
					//move left
					currentChannel--;
					if (currentChannel < 0) {
						currentChannel = 0;
					}
				}
				
				setListAdapter(new TVListAdapter(context, provider.getProgrammes(channels[currentChannel])));
				channel.setText(channels[currentChannel].displayName);
				scrollToCurrentTime((ListView)findViewById(android.R.id.list), provider.nowPlaying(channels[currentChannel]));
												
				return true;
			} else {
				return false;
			}
			
			
		}    	
    } 
}

class TVListAdapter extends BaseAdapter {
	private Context context;
	private List<Programme> list;
	
    public TVListAdapter(Context context, List<Programme> list) {
        this.context = context;
        this.list = list;
    }
	public int getCount() {
		return list.size();
	}
	public Object getItem(int position) {
		return list.get(position);
	}
	
	public long getItemId(int position) {
		return list.get(position).hashCode();
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			return new ProgrammeView(context, list.get(position));
		} else {
			((ProgrammeView)convertView).setProgramme(list.get(position));
			return convertView;
		}
	}  
}
