package com.example.plusmoins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.plusmoins.game.Deck;
import com.example.plusmoins.game.Player;

import androidx.constraintlayout.widget.ConstraintLayout;

public class OpenGLActivity extends Activity {

    private MyGLSurfaceView myGlView;
    private MyGLRenderer myGLRenderer;

    private PopupWindow popupWindow;

    private TextView cptCard1;
    private TextView cptCard2;
    private TextView indicTurn;
    private TextView winText;


    private int[] starterCards = new int[7];
    Player p1 = new Player();
    Player p2 = new Player();
    boolean turn = true;
    boolean firstAction = true;
    Deck onPlay = new Deck();
    Deck pills = new Deck();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        setContentView(R.layout.activity_main);
        initGame();

        printRemainsCards();
        myGLRenderer = new MyGLRenderer();
        myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck()-1));
        myGLRenderer.setGuess(true);
        indicTurn = findViewById(R.id.indicTurn);
        printTurnPlayer();
        myGlView = (MyGLSurfaceView) findViewById(R.id.glView);
        myGlView.setRenderer(myGLRenderer);
    }


    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        //mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
    }

    public void initGame(){
        for(int i = 0; i < starterCards.length; i++){
            if(i == 0 || i == 6)
                starterCards[i] = 8;
            else
                starterCards[i] = 9;
        }
        starterCards = p1.starterDeck(starterCards);
        starterCards = p2.starterDeck(starterCards);

        cptCard1 = findViewById(R.id.cptCards);
        cptCard1.setText("Remaining\n Cards :" + p1.remainCard());
        cptCard2 = findViewById(R.id.cptCards2);
        cptCard2.setText("Remaining\n Cards :" + p2.remainCard());
        indicTurn = findViewById(R.id.indicTurn);
        indicTurn.setText("Turn : Player 1");

        for(int i = 0; i < starterCards.length; i++){
            if (starterCards[i] != 0)
                pills.addCard(i);
        }
        int i = 0;
    }

    public void endGame(){
        printRemainsCards();
        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout cl = findViewById(R.id.mainLayout);

        Button plus = findViewById(R.id.plus);
        plus.setEnabled(false);
        Button minus = findViewById(R.id.minus);
        minus.setEnabled(false);
        Button equals = findViewById(R.id.equals);
        equals.setEnabled(false);
        Button pass = findViewById(R.id.pass);
        pass.setEnabled(false);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
        // Initialize a new instance of popup window
        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Get a reference for the custom view close button
        Button closeButton = (Button) customView.findViewById(R.id.ib_close);
        winText = customView.findViewById(R.id.winText);
        if(p1.getDeckIsEmpty() == true)
            winText.setText("Joueur 1 a gagné !\n Appuyez pour rejouer.");
        else
            winText.setText("Joueur 2 a gagné !\n Appuyez pour rejouer.");

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);
                popupWindow.dismiss();
            }
        });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
        // Finally, show the popup window at the center location of root relative layout
        popupWindow.showAtLocation(cl, Gravity.CENTER,0,0);
    }

    public void clickPlus(View view){
        if(turn == true){           //Gestion du joueur 1 si il a appuyé sur +
            if(firstAction == true) {  //Si c'est son premier pari de son tour
                if (p1.getTopCard() > pills.getDeckList().get(pills.sizeDeck() - 1)) {  //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {    // Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else {      //si ce n'est plus son premier pari de son tour
                if (p1.getTopCard() > onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {  //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p1.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        } else {    //Gestion du joueur 2 si il a appuyé sur +
            if(firstAction == true) { //Si c'est son premier pari de son tour
                if (p2.getTopCard() > pills.getDeckList().get(pills.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else { //Si ce n'est pas son premier pari de son tour
                if (p2.getTopCard() > onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p2.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = true;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        }
    }

    public void clickMinus(View view){
        if(turn == true){           //Gestion du joueur 1 si il a appuyé sur -
            if(firstAction == true) {  //Si c'est son premier pari de son tour
                if (p1.getTopCard() < pills.getDeckList().get(pills.sizeDeck() - 1)) {  //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {    // Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else {      //si ce n'est plus son premier pari de son tour
                if (p1.getTopCard() < onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {  //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p1.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        } else {    //Gestion du joueur 2 si il a appuyé sur -
            if(firstAction == true) { //Si c'est son premier pari
                if (p2.getTopCard() < pills.getDeckList().get(pills.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else { //Si ce n'est pas son premier pari
                if (p2.getTopCard() < onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p2.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = true;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        }
    }

    public void clickEquals(View view){
        if(turn == true){           //Gestion du joueur 1 si il a appuyé sur =
            if(firstAction == true) {  //Si c'est son premier pari
                if (p1.getTopCard() == pills.getDeckList().get(pills.sizeDeck() - 1)) {  //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {    // Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else {      //si ce n'est plus son premier pari
                if (p1.getTopCard() == onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p1.getTopCard();
                    p1.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p1.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else {  //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p1.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p1.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        } else {    //Gestion du joueur 2 si il a appuyé sur -
            if(firstAction == true) { //Si c'est son premier pari
                if (p2.getTopCard() == pills.getDeckList().get(pills.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    turn = false;
                    printTurnPlayer();
                    printRemainsCards();
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                }
            } else { //Si ce n'est pas son premier pari
                if (p2.getTopCard() == onPlay.getDeckList().get(onPlay.sizeDeck() - 1)) { //Si il a deviné juste
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    int temp = p2.getTopCard();
                    p2.removeCardFromDeck(0);
                    onPlay.addCard(temp);
                    firstAction = false;
                    if(p2.getDeckIsEmpty() == true)
                        endGame();
                    else {
                        printRemainsCards();
                        myGLRenderer.setTopPillCard(onPlay.getDeckList().get(onPlay.sizeDeck() - 1));
                        myGLRenderer.setGuess(true);
                    }
                } else { //Si il n'a pas deviné juste
                    myGLRenderer.setPlayer(turn);
                    myGLRenderer.setGuess(false);
                    myGLRenderer.setTopDeckCard(p2.getTopCard());
                    try{
                        Thread.sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    myGLRenderer.setTopPillCard(pills.getDeckList().get(pills.sizeDeck() - 1));
                    myGLRenderer.setGuess(true);
                    p2.addCardListToDeck(0,onPlay);
                    onPlay.clearDeck();
                    firstAction = true;
                    turn = true;
                    printTurnPlayer();
                    printRemainsCards();
                }
            }
        }
    }

    public void clickPass(View view){
        pills.addCardList(onPlay);
        onPlay.clearDeck();
        firstAction = true;
        if(turn == true){
            turn = false;
            printTurnPlayer();
        } else {
            turn = true;
            printTurnPlayer();
        }
    }

    public void printTurnPlayer(){
        if(turn == true)
            indicTurn.setText("Turn : Player 1");
        else
            indicTurn.setText("Turn : Player 2");
    }

    public void printRemainsCards(){
        cptCard1 = findViewById(R.id.cptCards);
        cptCard1.setText("P1 Remaining\n Cards :" + p1.remainCard());
        cptCard2 = findViewById(R.id.cptCards2);
        cptCard2.setText("P2 Remaining\n Cards :" + p2.remainCard());
    }


}