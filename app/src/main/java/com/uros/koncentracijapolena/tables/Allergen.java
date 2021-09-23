package com.uros.koncentracijapolena.tables;

public class Allergen {
    public int id;
    public String name;
    public String localName;
    public Allergen(int id, String name, String localName){
        this.id=id;
        this.name=name;
        this.localName=localName;
    }
}
