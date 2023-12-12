package com.example.thegame;

import javafx.scene.image.Image;

import java.util.Objects;

public class Card {
    // constant variables
    public static final String DEFAULT_NAME = "Frog";

    public static final int DEFAULT_HEALTH = 1;
    public static final int DEFAULT_DAMAGE = 0;
    public static final int DEFAULT_SOULS = 1;

    public static final Sigil DEFAULT_SIGIL = Sigil.NONE;


    private Image image;

    // instance variables
    private int health;
    private String name;
    private int damage;
    private int souls;

    private Sigil sigil;
    private Image sigilImage;

    public static Card frog = new Card(1, 0, 0, "Frog", Sigil.NONE, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/frog2.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/noSigil.png"))));
    public static Card cat = new Card(3, 1, 1, "Cat", Sigil.HUNTER, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/cat.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/HunterSigil.png"))));
    public static Card hippo = new Card(6, 2, 3, "Hippo", Sigil.BLOODTHIRSTY, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Hippo.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/BloodthirstySigil.png"))));
    public static Card bear = new Card(4, 3, 3, "Bear", Sigil.BLOODTHIRSTY, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Bear.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/BloodthirstySigil.png"))));
    public static Card porcupine = new Card(3, 1, 1, "Porcupine", Sigil.BRAMBLE, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Porcupine.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/BrambleSigil.png"))));
    public static Card octopus = new Card(3, 1, 2, "Octopus", Sigil.ARMS, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Octopus.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/ArmsSigil.png"))));
    public static Card skunk = new Card(4, 1, 1, "Skunk", Sigil.SMELLY, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Skunk.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/SmellySigil.png"))));
    public static Card opossum = new Card(3, 1, 1, "Opossum", Sigil.UNDEAD, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Possum.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/UndeadSigil.png"))));
    public static Card fightingBird = new Card(2, 1, 1, "Pigeon", Sigil.FLIGHT, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Fighting Bird.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/flightSigil.png"))));
    public static Card turtle = new Card(6, 1, 2, "Turtle", Sigil.BRAMBLE, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/turtle.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/BrambleSigil.png"))));
    public static Card snake = new Card(3, 2, 2, "Snake", Sigil.POISON, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Snake.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/PoisonSigil.png"))));
    public static Card dog = new Card(3, 2, 2, "Dog", Sigil.BLOODTHIRSTY, new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/Dog.png"))), new Image(Objects.requireNonNull(Card.class.getResourceAsStream("/images/BloodthirstySigil.png"))));


    // default constructor
    public Card() {
        setAll(DEFAULT_HEALTH, DEFAULT_DAMAGE, DEFAULT_SOULS, DEFAULT_NAME, DEFAULT_SIGIL, null, null);
    }

    public Card(int health, int damage, int souls, String name, Sigil sigil, Image image, Image sigilImage) {
        if (!setAll(health, damage, souls, name, sigil, image, sigilImage)) {
            System.out.println("ERROR: Please give valid inputs");
            System.exit(0);
        }
    }

    public Card(Card cardCopy) {
        this(cardCopy.getHealth(), cardCopy.getDamage(), cardCopy.getSouls(), cardCopy.getName(), cardCopy.getSigil(), cardCopy.getImage(), cardCopy.getSigilImage());

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

    public boolean setSigil(Sigil sigil) {
        this.sigil = sigil;
        return true;
    }

    public boolean setSigilImage(Image sigilImage) {

        this.sigilImage = sigilImage;
        return true;
    }

    public boolean setAll(int health, int damage, int souls, String name, Sigil sigil, Image image, Image sigilImage) {

        return setHealth(health) && setDamage(damage) && setName(name) && setSouls(souls) && setSigil(sigil) && setImage(image) && setSigilImage(sigilImage);
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

    public Sigil getSigil() {
        return this.sigil;
    }

    public Image getSigilImage() {
        return this.sigilImage;
    }

    public String toString() {

        return this.health + " " + this.damage + " " + this.name;
    }
}
