package com.example.plusmoins.game;

import com.example.plusmoins.game.Deck;

import java.util.Random;

public class Player {
    private Deck deck;

    public Player() {
        this.deck = new Deck();
    }

    public int[] starterDeck(int[] cptCards){
        Random rand = new Random();
        int select;
        int x;
        for(int i = 0; i < 30; i++){
            select = rand.nextInt(7);
            x = 0;
            while (x == 0) {
                if (cptCards[select] == 0)
                    select = rand.nextInt(7);
                else
                    x = 1;
            }
            this.deck.addCard(select);
            cptCards[select]--;
        }
        return cptCards;
    }

    public void addCardListToDeck(int indice, Deck deck){
        this.deck.addCardList(indice,deck);
    }

    public int getTopCard(){
        return this.deck.getDeckList().get(0);
    }

    public void removeCardFromDeck(int pos){
        this.deck.removeCard(pos);
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int remainCard(){
        return this.deck.sizeDeck();
    }

    public boolean getDeckIsEmpty(){
        return this.deck.deckEmpty();
    }

}
