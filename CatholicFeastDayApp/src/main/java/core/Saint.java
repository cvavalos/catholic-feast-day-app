package main.java.core;
import java.util.List;

public class Saint {
    private String name;
    private List<String> patronages;
    private String biography;

    public Saint(String name, List<String> patronages, String biography) {
        this.name = name;
        this.patronages = patronages;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public List<String> getPatronages() {
        return patronages;
    }

    public String getBiography() {
        return biography;
    }
}
