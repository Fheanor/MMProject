package com.ihm.mymuseum;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Julian on 15/11/2016.
 */

public class Tools {

    private static final String PREFERENCE_NAME = "Settings";

    /*TODO : use SharedPreferences instead of this ! */
    public static boolean configSelects = false;
    public static boolean userSelects = false;
    public static boolean isEnfant = false;
    public static boolean isMalvoyant = false;
    public static String oeuvre;
    public static boolean initGesture=false;


    public static List<Oeuvre> getOeuvres(AssetManager am, String filename){
        List<Oeuvre> oeuvres = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(am.open(filename));

            /*Element element=doc.getDocumentElement();
            element.normalize();*/

            NodeList nList = doc.getElementsByTagName("oeuvre");

            for (int i=0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    Oeuvre oeuvre = new Oeuvre(getValue("nom", elem), getValue("description", elem),
                                                getValue("date", elem), getValue("artiste", elem),
                                                getValue("infoArtiste", elem),
                                                getValue("audiodescription", elem));
                    oeuvres.add(oeuvre);
                }
            }

        } catch (Exception e) {e.printStackTrace();}
        return oeuvres;
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static void setPreference(Context context, String key, Object value){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(value instanceof Integer){
            editor.putInt(key, (int)value);
        }else if(value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        }else if(value instanceof Float) {
            editor.putFloat(key, (float) value);
        }else if(value instanceof Long) {
            editor.putLong(key, (long) value);
        }else if(value instanceof String) {
            editor.putString(key, (String) value);
        }else{
            return; //incorrect type for value
        }
        editor.commit();
    }

    public static SharedPreferences getPreferrence(Context context){
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static int randomInt(){
        return (int)Math.random()*10000;
    }

}
