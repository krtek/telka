package cz.krtinec.telka.provider;

import gnu.java.nio.charset.Windows1250;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.util.Xml;

import cz.krtinec.telka.CannotLoadProgrammeException;
import cz.krtinec.telka.IProgrammeProvider;
import cz.krtinec.telka.dto.Channel;
import cz.krtinec.telka.dto.Programme;

/**
 * Default implementation.
 * @author krtek
 *
 */
public class ProgrammeProvider implements IProgrammeProvider {
	
	private Map<Channel, List<Programme>> channels = null;
	private static final String URL = 
		"http://xmltv.arcao.com/xml.php?gids[]=ct1.ceskatelevize.cz&" +
			"gids[]=ct2.ceskatelevize.cz&" +
			"gids[]=nova.nova.cz&" +
			"gids[]=prima.iprima.cz&" +
			"gids[]=ct24.ct24.cz&" +
			"gids[]=ct4sport.ct24.cz&" +
			"gids[]=ocko.idnes.cz&" +
			"gids[]=cinema.nova.cz&" +
			"gids[]=cinema.nova.cz";

	public void disable(Channel channel) {
		throw new NoSuchMethodError("Not yet implemented!");
	}

	public void enable(Channel channel) {
		throw new NoSuchMethodError("Not yet implemented!");
	}

	public Collection<Channel> getAllChannels() {
		if (this.channels == null) {
			this.channels = loadChannels();
		}
		return channels.keySet();
	}

	public Collection<Channel> getEnabledChannels() {
		return getAllChannels();
	}

	public List<Programme> getProgrammes(Channel channel) {
		//channels cannot be null -> or can?
		return channels.get(channel);
	}		
	
	public Integer nowPlaying(Channel channel) {
		List<Programme> programmes = channels.get(channel);
		int i = 0;
		Date now = new Date();
		for (Programme p : programmes) {			
			if (p.start.compareTo(now) < 0 && p.stop.compareTo(now) > 0) {
				return i;
			}
			i++;
		}
		return 0;
	}

	private Map<Channel, List<Programme>> loadChannels() {
		try {
			Log.i("ProgrammeProvider", "Going to parse programmes");
			HttpURLConnection uc = (HttpURLConnection) new URL(URL).openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            InputStream is = uc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, new Windows1250()));
            ProgrammeHandler handler = new ProgrammeHandler();
            long start = System.currentTimeMillis();
           	Xml.parse(br, handler);
           	long stop = System.currentTimeMillis();
			Log.i("ProgrammeProvider", "Parsed in " + (stop - start) + " millis");
			return handler.getChannels();
		} catch (Exception e) {
			Log.e("ProgrammeProvider", "Exception: ", e);
			throw new CannotLoadProgrammeException("Cannot load TV programme" ,e);
		}  

	}	

}
