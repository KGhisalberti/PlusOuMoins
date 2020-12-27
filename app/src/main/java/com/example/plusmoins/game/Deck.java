package com.example.plusmoins.game;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Integer> deckList;

    public Deck() {
        this.deckList = new ArrayList<>();
    }

    public void addCard(int card){
        this.deckList.add(card);
    }

    public void addCardList(Deck deck){
        this.deckList.addAll(deck.getDeckList());
    }

    public void addCardList(int pos, Deck deck){
        this.deckList.addAll(pos, deck.getDeckList());
    }

    public void removeCard(int pos){
        this.deckList.remove(pos);
    }

    public List<Integer> getDeckList() {
        return deckList;
    }

    public void setDeckList(List<Integer> deckList) {
        this.deckList = deckList;
    }

    public int sizeDeck(){
        return this.deckList.size();
    }

    public boolean deckEmpty(){
        return this.deckList.isEmpty();
    }

    public void clearDeck(){
        this.deckList.clear();
    }
}
