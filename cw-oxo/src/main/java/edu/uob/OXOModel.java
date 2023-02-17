package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class OXOModel {

    private ArrayList<OXOPlayer> cells;
    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    public int wid;
    public int hei;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        cells = new ArrayList<OXOPlayer>();
        wid = numberOfRows;
        hei = numberOfColumns;
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    //Return width(rows) of the board.
    public int getNumberOfRows() {
        return wid;
    }
    //Return height(columns) of the board.
    public int getNumberOfColumns() {
        return hei;
    }
    //Get the owner of this cell.
    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        if (rowNumber * wid + colNumber + 1 > cells.size()) {
            return new OXOPlayer(' ');
        }
        if(cells.isEmpty()){
            return new OXOPlayer(' ');
        }
        return cells.get(rowNumber* wid + colNumber);
    }
    //Set the owner of this cell.
    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        if(cells.isEmpty()){
            cells.ensureCapacity(wid * hei);
            OXOPlayer p1 = new OXOPlayer(' ');
            for(int i = 0; i < wid * hei; i++){
                cells.add(p1);
            }
        }
        cells.set(rowNumber * wid + colNumber, player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }

    public boolean isGameDrawn() {
        return gameDrawn;
    }
    public void addRow() {
        wid = getNumberOfRows();
        if (wid < 9) {
            wid = wid + 1;
        }
        //待补充：需要提示超出界限。
    }
    public void removeRow(){
        wid = getNumberOfRows();
        if(wid > 3){
            wid = wid - 1;

        }
        //待补充：需要提示超出界限。
    }
    public void addColumn(){
        hei = getNumberOfColumns();
        if(hei < 9){
            hei = hei + 1;

        }
        //
    }
    public void removeColumn(){
        hei = getNumberOfColumns();
        if(hei > 3){
            hei = hei - 1;
        }
        //
    }
}
