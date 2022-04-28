package com.example.olio_ht;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MovieDataManager {
    private static MovieDataManager mdm = null;
    MovieObject movie;

    /* Singleton */
    private MovieDataManager() {
    }

    public static MovieDataManager getInstance() {
        if (mdm == null) {
            mdm = new MovieDataManager();
        }
        return mdm;
    }

    /* This method gets the data from Finnkino's API, returns a list of movies */
    public ArrayList<MovieObject> getMovies(String nameFilter, String url) {
        ArrayList<MovieObject> allMovies = new ArrayList<>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc = docBuilder.parse(url);
            xmlDoc.getDocumentElement().normalize();
            NodeList rawNodes = xmlDoc.getDocumentElement().getElementsByTagName("Show");
            for (int i = 0; i < rawNodes.getLength(); i++) {
                Node node = rawNodes.item(i);
                movie = new MovieObject();
                /* Here we add the values for MovieObject */
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    movie.setMovieNameFi(element.getElementsByTagName("Title").item(0).getTextContent());
                    movie.setMovieNameEn(element.getElementsByTagName("OriginalTitle").item(0).getTextContent());
                    movie.setMovieDate(LocalDateTime.parse(element.getElementsByTagName("dttmShowStart").item(0).getTextContent()));
                    movie.setLength(element.getElementsByTagName("LengthInMinutes").item(0).getTextContent());
                    movie.setAuditorium(element.getElementsByTagName("TheatreAndAuditorium").item(0).getTextContent());
                    Element element2 = (Element) element.getElementsByTagName("SpokenLanguage").item(0);
                    movie.setSpokenLanguage(element2.getElementsByTagName("ISOTwoLetterCode").item(0).getTextContent());
                }
                /* If the user leaves moviename empty, all movies are added to list. If user inputs a name, only the movies with same name are added */
                if(nameFilter.length() == 0) {
                    allMovies.add(movie);
                } else if(movie.getMovieNameFi().equalsIgnoreCase(nameFilter)) {
                    allMovies.add(movie);
                } else {
                    if(movie.getMovieNameEn().equalsIgnoreCase(nameFilter)) {
                        allMovies.add(movie);
                        }
                    }
                }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return allMovies;
    }
}