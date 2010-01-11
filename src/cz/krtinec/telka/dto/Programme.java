package cz.krtinec.telka.dto;

import java.io.Serializable;
import java.util.Date;

import cz.krtinec.telka.provider.ProviderUtils;

public class Programme implements Serializable {
	public Date start;
	public Date stop;
	public String title;
	public String description;
	public String iconURL;
	public String channelId;
	public String vps;
	private transient State state;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((stop == null) ? 0 : stop.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Programme other = (Programme) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (stop == null) {
			if (other.stop != null)
				return false;
		} else if (!stop.equals(other.stop))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	public State getState() {
		if (this.state == null) {
			this.state = ProviderUtils.determineState(this.start, this.stop);
			
		}
		return this.state;
	}	
}
