package cz.krtinec.telka;

import java.util.Collection;
import java.util.List;

import android.content.Context;

import cz.krtinec.telka.dto.Channel;
import cz.krtinec.telka.dto.Programme;

public interface IProgrammeProvider {
	
	/**
	 * 
	 * @param reloadInterval in milliseconds.
	 * @return
	 */
	public Collection<Channel> getAllChannels(int reloadInterval);
	public Collection<Channel> getEnabledChannels(int reloadInterval);
	public void enable(Channel channel);
	public void disable(Channel channel);		
	public List<Programme> getProgrammes(Channel channel);
	public Integer nowPlaying(Channel channel);
	public void reload();	
}
