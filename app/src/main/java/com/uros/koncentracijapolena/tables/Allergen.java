package com.uros.koncentracijapolena.tables;

public class Allergen {
    public int id;
    public int typeId;
    public int textViewId;
    public String name;
    public String localName;

    public Allergen(int id,int typeId,String name,String localName){
        textViewId=0;
        this.id=id;
        this.typeId=typeId;
        this.name=name;
        this.localName=localName;
    }
}
