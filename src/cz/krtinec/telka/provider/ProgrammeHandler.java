package cz.krtinec.telka.provider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;
import cz.krtinec.telka.Constants;
import cz.krtinec.telka.dto.Channel;
import cz.krtinec.telka.dto.Programme;

class ProgrammeHandler extends DefaultHandler {
	private static final String PROGRAMME = "programme";
	private static final String CHANNEL = "channel";
	private Map<Channel, List<Programme>> channels;
	/** Programme we are currently reading */
	private Programme currentProgramme;
	/** Channel we are currently reading */
	private Channel currentChannel;
	/** Fake channel for help with Map */
	private Channel processingChannel = new Channel("null");
	private String element;
	//2009041523250 +0100
	private DateFormat format;
	
	private static final boolean DLS = TimeZone.getDefault().inDaylightTime(new Date());	
	
	public ProgrammeHandler() {
		super();
		Log.i("ProgrammeHandler", "Creating handler...");
		this.channels = new HashMap<Channel, List<Programme>>();
		format = new SimpleDateFormat("yyyyMMddHHmmss ZZZZZ");
		format.setLenient(false);
		Log.i("ProgrammeHandler", "Created...");
	}
	

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length);
		if (s.trim().length() == 0) {
			return;
		}
		if ("title".equals(element)) {
			currentProgramme.title = s;
		} else if ("desc".equals(element)) {
			currentProgramme.description = s;
		} else if ("display-name".equals(element)) {
			currentChannel.displayName = s;
		}
		
	}


	public void endElement(String uri, String localName, String name)
			throws SAXException {
		//Log.d("ProgrammeHandler", "end element: " + localName + ", " + name);
		if (localName.equals(PROGRAMME)) {
			currentProgramme.state = ProviderUtils.determineState(
					currentProgramme.start, currentProgramme.stop);
			/*
			Log.d("ProgrammeHandler", currentProgramme.title + ", Start: " + currentProgramme.start + 
					", Stop: " + currentProgramme.stop + ", State: " + currentProgramme.state);
			*/
			channels.get(processingChannel).add(currentProgramme);
		} else if (localName.equals(CHANNEL)) {
			channels.put(currentChannel, new ArrayList<Programme>());
		}
		
	}


	public void startDocument() throws SAXException {
		Log.d("ProgrammeHandler", "start document");
		
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		//Log.d("ProgrammeHandler", "start element: " + localName + ", " + name);
		element = localName;
		if (localName.equals(PROGRAMME)) {
			currentProgramme = new Programme();
			try {
				currentProgramme.start = moveIfDaylightsaving(format.parse(atts.getValue("start")));
				currentProgramme.stop = moveIfDaylightsaving(format.parse(atts.getValue("stop")));
				currentProgramme.vps = atts.getValue("vps");
				processingChannel.id = atts.getValue("channel");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (localName.equals(CHANNEL)) {
			currentChannel = new Channel(atts.getValue("id"));
			
		} else if (localName.equals("icon")) {
			currentProgramme.iconURL = atts.getValue("src");
		}
	}


	/** Fixes bug in XMLTV */
	private Date moveIfDaylightsaving(Date d) {
		if (DLS) {
			d.setTime(d.getTime() - Constants.HOUR);
		} 
		return d;
		
	}


	public Map<Channel, List<Programme>> getChannels() {
		return channels;
	}	

}
