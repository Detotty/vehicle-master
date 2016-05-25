package pk.roadpartner.model;

/**
 * Created by Gazi Rimon on 3/29/2016.
 */
// This is used to map the JSON keys to the object by GSON
public class Question {

    String title;
    String link;

    @Override
    public String toString() {
        return(title);
    }
}