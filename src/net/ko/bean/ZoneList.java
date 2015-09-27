package net.ko.bean;

import java.net.URL;
import java.util.ArrayList;
import net.ko.creator.Activator;
import net.ko.utils.KXmlFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.templates.Template;
import org.osgi.framework.Bundle;
import org.w3c.dom.NodeList;

public class ZoneList {
	private boolean isLoaded=false;
	private ArrayList<ZoneXml> zones;
	private KXmlFile xmlFile;
	public ZoneList() {
		super();
		zones=new ArrayList<>();
	}
	public void loadZonesList(){
		xmlFile=new KXmlFile();
		try {
			Bundle bundle=Platform.getBundle(Activator.PLUGIN_ID);
			Path path = new Path("net/ko/templates/java");
			URL fileURL = FileLocator.find(bundle, path, null);
			xmlFile.loadFromFile(fileURL.toURI().toString()+"zones.xml");
			initZones();
			isLoaded=true;
		} catch (Exception e){
			isLoaded=false;
		}
	}
	private void initZones() {
		NodeList nodes=xmlFile.getChildNodes();
		for(int i=0;i<nodes.getLength();i++){
			if(nodes.item(i).getNodeName().equals("zone")){
				ZoneXml zone=new ZoneXml();
				zone.initFromXML(nodes.item(i));
				zones.add(zone);
			}
		}
	}
	public boolean isLoaded() {
		return isLoaded;
	}
	public ArrayList<ZoneXml> getZones() {
		return zones;
	}
	public Template[] getTemplates(){
		Template[] result=new Template[zones.size()];
		for(int i=0;i<zones.size();i++)
			result[i]=zones.get(i).getTemplate();
		return result;
	}
	public ZoneXml getZoneMatch(String value){
		boolean find=false;
		ZoneXml result=null;
		int i=0;
		while(!find && i<zones.size()){
			find=zones.get(i).matchWith(value);
			i++;
		}
		if(find)
			result=zones.get(i-1);
		return result;
	}
	public void add(ZoneXml zone){
		zones.add(zone);
	}
}
