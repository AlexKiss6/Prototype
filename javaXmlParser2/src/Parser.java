import java.io.*;
import java.util.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
@SuppressWarnings({ "unchecked", "rawtypes" })

public class Parser {

	public static void main(String[] args) {
		
		//writeXML();
		
		//readXML();
		
		
		SAXBuilder builder = new SAXBuilder();
		
		Document readDoc = null;
		try {
			readDoc =  builder.build(new File("./src/EventList.xml"));
		} catch (JDOMException | IOException e) {
			
			e.printStackTrace();
		}
		Element root = readDoc.getRootElement();
		
		String[] mytags = {"Food", "Social"};
		
		Vector tempList = new Vector(1,5);
		
		tempList.addAll(tagSearch(root, mytags));
		//System.out.println(root);
		
		printEvents(root, tempList);
		
		//String[] myList = {"Food", "Games"};
		
		//createEvent("Field Day", "IST", "3:00 P.M", "4:00 P.M", "Good times all day", myList);
	}

	public static void readXML(){
		SAXBuilder builder = new SAXBuilder();
		
		Document readDoc = null;
		try {
			readDoc =  builder.build(new File("./src/EventList.xml"));
		} catch (JDOMException | IOException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Root: " + readDoc.getRootElement());
		
		Element root = readDoc.getRootElement();
		
		//System.out.println("Group Name: " + root.getChild("Group").getChildText("Group_Name"));
		
		for (Element curElem: root.getChildren("Event")) {
			System.out.println("Event Name: " + curElem.getChildText("Event_Name"));
			System.out.println("Location: " + curElem.getChildText("Location"));
		}
		
	}
	
	private static void writeXML() {
		String[] Event_Names = {"Food Truck Rally", "Community Service", "Earth Day"};
		String[] Locations = {"LakeLand Square", "Lakeland Community Center", "TBD"};
		String[] Start_Times = {"4:00 PM", "4:15 PM", "4:30"};
		String[] End_Times = {"5:00 PM", "5:15 PM", "5:30"};
		String[] Description = {"Food", "Fun", "Happiness"};
		
		try{
		
		Document doc = new Document();
		Element theRoot = new Element("CorkBoard");
		doc.setRootElement(theRoot);
		
		
		for (int i = 0; i < 3; i++)
		{
			Element event = new Element("Event");

			event.setAttribute("Last_modified", Long.toString(System.currentTimeMillis()));
			
			//Element header = new Element("Header");
			
			Element name = new Element("Event_Name");
			name.setAttribute("Event_ID", "" + i);
			name.addContent(new Text(Event_Names[i]));
			
			Element location = new Element("Location");
			location.addContent(new Text(Locations[i]));
			
			Element begin_Time = new Element("Begin_Time");
			begin_Time.addContent(new Text(Start_Times[i]));
			
			Element end_Time =  new Element("End_Time");
			end_Time.addContent(new Text(End_Times[i]));
			
			Element desc = new Element("Description");
			desc.addContent(new Text(Description[i]));
			
			event.addContent(name);
			event.addContent(location);
			event.addContent(begin_Time);
			event.addContent(end_Time);
			event.addContent(desc);
			
			theRoot.addContent(event);
			
		}
		
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileOutputStream(new File("./src/EventList.xml")));
		
		System.out.println("Wrote to file");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Vector tagSearch(Element root, String text){
		
		Vector IDs = new Vector(5,5);
		for (Element curElem: root.getChildren("Event")) {
			for (Element nextElem: curElem.getChildren("Tags")){
				if (text.equals(nextElem.getChildText("Tag")))
				{
					IDs.addElement(curElem.getChild("Event_Name").getAttributeValue("Event_ID"));
					System.out.println("Event Name: " + curElem.getChildText("Event_Name"));
					System.out.println("Location: " + curElem.getChildText("Location"));
				}
			}
		}
		
		if (!root.getChildren("Group").isEmpty())
		{
			for (Element curElem: root.getChildren("Group"))
				IDs.addAll(tagSearch(curElem, text));
		}
		return IDs;
	}
	
	public static Vector tagSearch(Element root, String[] text){
		int validities = text.length;
		int amountValid = 0;
		Vector IDs = new Vector(5,5);
		for(Element firstElem: root.getChildren("Event")){
			//System.out.println(firstElem);
			Element secondElem = firstElem.getChild("Tags");
			//System.out.println(secondElem);
			for (Element thirdElem: secondElem.getChildren()){
				//System.out.println(thirdElem);
				for (String elem: text){
					if (elem.equals(thirdElem.getText()))
					{
						//System.out.println(amountValid + " " + elem);
						amountValid += 1;
					}
				}
			}
			if (amountValid == validities){
				IDs.addElement(firstElem.getChild("Event_Name").getAttributeValue("Event_ID"));
				//System.out.println("Event Name: " + firstElem.getChildText("Event_Name"));
				//System.out.println("Location: " + firstElem.getChildText("Location"));
			}
			amountValid = 0;
		}
		if (!root.getChildren("Group").isEmpty())
		{
			for (Element curElem: root.getChildren("Group"))
				IDs.addAll(tagSearch(curElem, text));
		}
		return IDs;
	}
	
	public static Vector dateSearch(Element root, TimeType bTime, TimeType eTime){
		TimeType Begin_Time = new TimeType();
		TimeType End_Time = new TimeType();
		Vector IDs = new Vector(5,5);
		
		for (Element firstElem: root.getChildren("Event")){
			Begin_Time.setTime(firstElem.getChildText("Begin_Time"));
			End_Time.setTime(firstElem.getChildText("End_Time"));
			
			if (bTime.getTime() <= Begin_Time.getTime() && eTime.getTime() >= End_Time.getTime()){
				IDs.add(firstElem.getChild("Event_Name").getAttributeValue("Event_ID"));
			}
		}
		if (!root.getChildren("Group").isEmpty())
		{
			for (Element curElem: root.getChildren("Group"))
				IDs.addAll(dateSearch(curElem, bTime, eTime));
		}
		return IDs;
	}
	
	public static void printEvent(Element root){
		if (root.getText().contains("Event")){
			System.out.println(root.getChildText("Event_Name"));
		}
	}
	
	public static void printEvents(Element root, Vector Ids){
		String tempString;
		for (Element firstElem: root.getChildren("Event")){
			for (int i = 0; i < Ids.size(); i ++){
				
				tempString = (String) Ids.get(i);
				System.out.println(firstElem.getChild("Event_Name").getAttributeValue("Event_ID"));
				//System.out.println(tempString.contentEquals((firstElem.getChild("Event_Name").getAttributeValue("Event_ID"))));
				if (tempString.contentEquals((firstElem.getChild("Event_Name").getAttributeValue("Event_ID")))){
					printEvent(firstElem);
				}
			}
		}
		if (!root.getChildren("Group").isEmpty())
		{
			for (Element curElem: root.getChildren("Group"))
				System.out.println("");//printEvents(curElem, Ids);
		}
	}
	
 	public static Element createEvent(String event, String location, String date, String start_time, String end_Time, String desc, String[] tags){
		
		Element Event = new Element("Event");
		Event.setAttribute("Last_Modified", Long.toString(System.currentTimeMillis()));
		
		Element Name = new Element("Event");
		Name.setAttribute("Event_ID", "" + findNewID());
		Name.addContent(event);
		
		Element Location = new Element("Location");
		Location.addContent(location);
		
		Element Date = new Element("Date");
		Date.addContent(date);
		
		Element Start_Time = new Element("Begin_Time");
		Start_Time.addContent(start_time);
		
		Element End_Time = new Element("End_Time");
		End_Time.addContent(end_Time);
		
		Element Desc = new Element("Description");
		Desc.addContent(desc);
		
		Element Tags = new Element("Tags");
		
		for (String tag: tags){
			Element Tag = new Element("Tag");
			Tag.addContent(tag);
			Tags.addContent(Tag);
		}
		
		Event.addContent(Name);
		Event.addContent(Location);
		Event.addContent(Date);
		Event.addContent(Start_Time);
		Event.addContent(End_Time);
		Event.addContent(Desc);
		Event.addContent(Tags);
		
		return Event;
		
	}
	
	private static int findNewID(){
		int curId = 0;
		int tempid;
		SAXBuilder builder = new SAXBuilder();
		
		Document readDoc = null;
		try {
			readDoc =  builder.build(new File("./src/EventList.xml"));
		} catch (JDOMException | IOException e) {
			
			e.printStackTrace();
		}
		Element root = readDoc.getRootElement();
		for (Element firstElem: root.getChildren("Event")){
			Element secondElem = firstElem.getChild("Event_Name");
			tempid = Integer.valueOf(secondElem.getAttributeValue("Event_ID"));
			if ( tempid > curId){
				curId = tempid;
			}
			
		}
		if (!root.getChildren("Group").isEmpty()){
			
			for (Element curElem: root.getChildren("Group")){
				tempid = findNewID(curElem) - 1;
				if (tempid > curId){
					curId = tempid;
				}
			}
		}
		
		return curId + 1;
	}
	
	private static int findNewID(Element root){
		int curId = 0;
		int tempid;
		for (Element firstElem: root.getChildren("Event")){
			Element secondElem = firstElem.getChild("Event_Name");
			tempid = Integer.valueOf(secondElem.getAttributeValue("Event_ID"));
			if ( tempid > curId){
				curId = tempid;
			}
			
		}
		if (!root.getChildren("Group").isEmpty()){
			
			for (Element curElem: root.getChildren("Group")){
				tempid = findNewID(curElem) - 1;
				if (tempid > curId){
					curId = tempid;
				}
			}
		}
		
		return curId + 1;
	}


}
