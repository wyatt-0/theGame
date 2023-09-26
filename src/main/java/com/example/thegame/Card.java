package com.example.thegame;

import javafx.scene.image.Image;

public class Card {
    // constant variables
    public static final String DEFAULT_NAME = "Frog";

    public static final int DEFAULT_HEALTH = 1;
    public static final int DEFAULT_DAMAGE = 0;
    public static final int DEFAULT_SOULS = 1;



    private Image image;

    // instance variables
    private int health;
    private String name;
    private int damage;
    private int souls;


    // default constructor
    public Card() {
        setAll(DEFAULT_HEALTH,DEFAULT_DAMAGE,DEFAULT_SOULS,DEFAULT_NAME,null);
    }
    public Card(int health , int damage,int souls, String name,Image image) {
        if (!setAll(health, damage,souls, name,image)) {
            System.out.println("ERROR: Please give valid inputs");
            System.exit(0);
        }
    }
    public Card(Card cardCopy){
        this(cardCopy.getHealth(), cardCopy.getDamage(),cardCopy.getSouls(), cardCopy.getName(),cardCopy.getImage());

    }
    // setters
    public boolean setHealth(int health) {

        this.health = health;
        return true;
    }
    public boolean setDamage(int damage) {

        this.damage = damage;
        return true;
    }
    public boolean setSouls(int souls) {

        this.souls = souls;
        return true;
    }
    public boolean setImage(Image image) {

        this.image = image;
        return true;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }
    public boolean setAll(int health , int damage, int souls, String name, Image image) {

        return setHealth(health) && setDamage(damage) && setName(name) && setSouls(souls) && setImage(image);
    }


    public int getHealth() {
        return this.health;
    }
    public int getDamage() {
        return this.damage;
    }
    public int getSouls() {
        return this.souls;
    }
    public Image getImage() {
        return this.image;
    }
    public String getName() {
        return this.name;
    }



    public String toString() {

        return this.health + " " + this.damage + " " + this.name;
    }
}
