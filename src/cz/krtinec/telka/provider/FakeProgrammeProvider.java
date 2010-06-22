package cz.krtinec.telka.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import cz.krtinec.telka.IProgrammeProvider;
import cz.krtinec.telka.dto.Channel;
import cz.krtinec.telka.dto.Programme;

public class FakeProgrammeProvider implements IProgrammeProvider {
	
	private Map <Channel, List<Programme>> channels;
	private Calendar today = GregorianCalendar.getInstance();
	
	public FakeProgrammeProvider(Context context) {
		this.channels = new HashMap<Channel, List<Programme>>();
		for (int i=0; i<10; i++) {
			Channel ch = new Channel("CH" + i, "Channel " + i);
			List<Programme> progs = new ArrayList<Programme>();
			for (int j=0; j<24; j++) {
				Programme p = new Programme();
				p.channelId = ch.id;
				p.title = "Program " + j;
				p.description = "Film z prostředí americké věznice";
				Calendar temp = (Calendar) today.clone();
				temp.set(Calendar.MINUTE, 0);
				temp.set(Calendar.SECOND, 0);
				temp.set(Calendar.HOUR_OF_DAY, j);
				p.start = temp.getTime();
				temp.set(Calendar.HOUR_OF_DAY, j+1);
				p.stop = temp.getTime();
				progs.add(p);
			}
			this.channels.put(ch, progs);
		}
	}

	@Override
	public void disable(Channel channel) {
		throw new UnsupportedOperationException("Operation disable() not supported."); 

	}

	@Override
	public void enable(Channel channel) {
		throw new UnsupportedOperationException("Operation enable() not supported.");

	}

	@Override
	public Collection<Channel> getAllChannels(int reloadInterval) {
		return channels.keySet();
	}

	@Override
	public Collection<Channel> getEnabledChannels(int reloadInterval) {
		return channels.keySet();
	}

	@Override
	public List<Programme> getProgrammes(Channel channel) {
		return channels.get(channel);
	}

	@Override
	public Integer nowPlaying(Channel channel) {
		List<Programme> programmes = channels.get(channel);
		int i=0;
		for (Programme p : programmes) {			
			if (p.getState().isRunning()) {
				//Log.d("ProgrammeProvider", "isRunning() returns " + p);
				return i;		
			}
			i++;
		}
		return i;
	}

	@Override
	public void reload() {
		// do nothing

	}

}
