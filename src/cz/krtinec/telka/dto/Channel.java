package cz.krtinec.telka.dto;

import java.io.Serializable;

public class Channel implements Serializable {
	public String id;
	public String displayName;
	
	
	public Channel(String id) {
		this.id = id;		
	}
	
	public Channel (String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Channel other = (Channel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
