package net.chinzer.seismicincidenttracker;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Rss {
    private static final String urlSource ="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";


    public static List<SeismicIncident> seismicIncidents()throws IOException, XmlPullParserException{
         return  parse(retrieve());
    }

    private static InputStream retrieve() throws IOException {
        URL url;
        URLConnection yc;
        url = new URL(urlSource);
        yc = url.openConnection();
        yc.setConnectTimeout(5000);
        yc.setReadTimeout(10000);
        return yc.getInputStream();
    }

    private static List<SeismicIncident> parse(InputStream seismicIncidentsXML) throws XmlPullParserException, IOException{
        List<SeismicIncident> seismicIncidents = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        int eventType = xpp.getEventType();
        xpp.setInput(seismicIncidentsXML, null);
        boolean inItemTag = false;
        boolean inLinkTag = false;
        boolean inDescriptionTag = false;
        String descriptionToParse = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equalsIgnoreCase("item")){
                    inItemTag = true;
                }
                else if (xpp.getName().equalsIgnoreCase("description")){
                    inDescriptionTag = true;
                }
                else if (xpp.getName().equalsIgnoreCase("link")){
                    inLinkTag = true;
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                if (xpp.getName().equalsIgnoreCase("item")){
                    inItemTag = false;
                    inDescriptionTag = false;
                    inLinkTag = false;
                }
                else if (xpp.getName().equalsIgnoreCase("description")) {
                    inDescriptionTag = false;
                }
                else if (xpp.getName().equalsIgnoreCase("link")){
                    inLinkTag = false;
                }

            } else if(eventType == XmlPullParser.TEXT) {
                if(inDescriptionTag && inItemTag){
                    descriptionToParse = xpp.getText();

                }
                else if(inLinkTag && inItemTag){
                    if (descriptionToParse != ""){
                        seismicIncidents.add(parseDescription(descriptionToParse, xpp.getText()));
                        descriptionToParse = "";
                    }
                }
            }
            eventType = xpp.next();
        }
        return seismicIncidents;
    }

    private static SeismicIncident parseDescription(String description, String link){
        ArrayList<String> rawSeismicIncident = new ArrayList<>();
        String[] descriptionParts = description.split(";");
        for(String info : descriptionParts){
            String[]keyValue = info.split(":", 2);
            rawSeismicIncident.add(keyValue[1].trim());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss xxxx");
        OffsetDateTime dateTime = OffsetDateTime.parse(rawSeismicIncident.get(0) + " +0000",  formatter);
        String locality = capitalizeWords(rawSeismicIncident.get(1).toLowerCase());
        String[]latLong = rawSeismicIncident.get(2).split(",");
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);
        int depth = Integer.parseInt(rawSeismicIncident.get(3).replaceAll("[^\\d.]", ""));
        double magnitude = Double.parseDouble(rawSeismicIncident.get(4));
        return new SeismicIncident(dateTime, depth, magnitude, locality, latitude, longitude, link);
    }

    public static String capitalizeWords(String str){
        String[] words = str.split("\\s");
        String capitalizeWords="";
        for(String w:words){
            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWords += String.format("%s%s %s", first.toUpperCase(), afterfirst, " ");
        }
        words = capitalizeWords.trim().split(",");
        if (words.length > 1) {
            capitalizeWords = words[0].substring(0, 1).toUpperCase() + words[0].substring(1) + ", " + words[1].substring(0, 1).toUpperCase() + words[1].substring(1);
        }
        return capitalizeWords.trim();
    }
}
