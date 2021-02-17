package application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Movie {
    private SimpleStringProperty name, director, country, actors, language;
    private SimpleIntegerProperty year, duration;
    private SimpleBooleanProperty seen;
    private SimpleFloatProperty rating;

    public Movie(String name, String director, String country,  String actors, String language, String year, String duration,  int seen, String rating) {

        if (!year.equals("")) {
            this.year = new SimpleIntegerProperty(Integer.parseInt(year));
        } else {
            this.year = new SimpleIntegerProperty(0);
        }
        if (!duration.equals("")) {
            this.duration = new SimpleIntegerProperty(Integer.parseInt(duration));
        } else {
            this.duration = new SimpleIntegerProperty(0);
        }

        if (!rating.equals("")) {
            this.rating = new SimpleFloatProperty(Float.parseFloat(rating));
        } else {
            this.rating = new SimpleFloatProperty(0);
        }

        this.name = new SimpleStringProperty(name);
        this.director = new SimpleStringProperty(director);

        this.seen = new SimpleBooleanProperty(false);
        if(seen == 1){this.seen = new SimpleBooleanProperty(true);}

        this.country = new SimpleStringProperty(country);
        this.actors = new SimpleStringProperty(actors);
        this.language = new SimpleStringProperty(language);
        this.rating = new SimpleFloatProperty(Float.parseFloat(rating));

    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country.get();
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country.set(country);
    }


    /**
     * @return the actors
     */
    public String getActors() {
        return actors.get();
    }

    /**
     * @param actors the actors to set
     */
    public void setActors(String actors) {
        this.actors.set(actors);
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language.get();
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language.set(language);
    }

    /**
     * @return the rating
     */
    public Float getRating() {
        return rating.get();
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Float rating) {
        this.rating.set(rating);
    }


    /**
     * @return the director
     */
    public String getDirector() {
        return director.get();
    }

    /**
     * @param director the director to set
     */
    public void setDirector(String director) {
        this.director.set(director);
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year.get();
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year.set(year);
    }

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration.get();
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration.set(duration);
    }

    /**
     * @return the seen
     */
    public int getSeen() {
        int i = 0;
        if(this.seen.getValue()){i = 1;}
        return i;
    }

    /**
     * @param seen the seen to set
     */
    public void setSeen(int seen) {
        this.seen.set(false);
        if(seen == 1)
        this.seen.set(true);
    }

    @Override
    public String toString() {
        int pr = 0;
        if(seen.get())
            pr = 1;
        else pr = 0;
  return new String(name.get() + ";" + director.get() + ";" + country.get()  + ";" + actors.get() + ";" + language.get() + ";" + year.get() + ";" + duration.get() + ";"
                + String.valueOf(pr) + ";"
                + String.valueOf(rating.get()) + System.lineSeparator());
    }
}
