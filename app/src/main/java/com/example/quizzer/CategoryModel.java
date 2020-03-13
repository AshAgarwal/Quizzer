package com.example.quizzer;

public class CategoryModel {
    private int Sets;
    private String Url;
    private String Name;

    public CategoryModel(){}        // Default Constructor for Firebase

    public CategoryModel(int sets, String url, String name) {
        Sets = sets;
        Url = url;
        Name = name;
    }

    public int getSets() {
        return Sets;
    }

    public void setSets(int sets) {
        Sets = sets;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
