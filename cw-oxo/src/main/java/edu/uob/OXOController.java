package edu.uob;

import javax.imageio.plugins.tiff.TIFFImageReadParam;

public class OXOController {
    OXOModel gameModel;
    char rowLetter;
    int colNumber = 0;
    public OXOController(OXOModel model) {
        gameModel = model;
    }
    //For win detection
    //Checking row if there has someone achieve the WinThreshold.
    public boolean checkHor(OXOPlayer candidate){
        int count;
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            count = 0;
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                if (gameModel.getCellOwner(i, j) == candidate) {
                    count++;
                }
                else {
                    count = 0;
                }
                if(count >= gameModel.getWinThreshold()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkVer(OXOPlayer candidate){
        int count;
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
            count = 0;
            for (int j = 0; j < gameModel.getNumberOfRows(); j++) {
                if (gameModel.getCellOwner(j, i) == candidate){
                    count++;
                }
                else{
                    count = 0;
                }
                if(count >= gameModel.getWinThreshold()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDia(OXOPlayer candidate){
        int count;
        int rowMax = gameModel.getNumberOfRows();
        int colMax = gameModel.getNumberOfColumns();
        for (int i = 0; i < rowMax; i++) {
            for (int j = 0; j < colMax; j++) {
                count = 0;
                int colIndex = j;
                int rowIndex = i;
                //search right && top.
                while(rowIndex >= 0 && colIndex < colMax){
                    if (gameModel.getCellOwner(rowIndex,colIndex) == candidate){
                        count++;
                    }
                    else{
                        count = 0;
                    }
                    if(count >= gameModel.getWinThreshold()){
                        return true;
                    }
                    colIndex++;
                    rowIndex--;
                }
                //search right bottom.
                count =0;
                rowIndex = i;
                colIndex = j;
                while(rowIndex < rowMax && colIndex < colMax){
                    if(gameModel.getCellOwner(rowIndex,colIndex) == candidate){
                        count++;
                    }
                    else{
                        count = 0;
                    }
                    if(count >= gameModel.getWinThreshold()){
                        return true;
                    }
                    rowIndex++;
                    colIndex++;
                }
            }
        }
        return false;
    }

    public void checkWinner(){
        for (int i = 0; i < gameModel.getNumberOfPlayers(); i++) {
            if(checkHor(gameModel.getPlayerByNumber(i))||checkVer(gameModel.getPlayerByNumber(i))||checkDia(gameModel.getPlayerByNumber(i))){
                gameModel.setWinner(gameModel.getPlayerByNumber(i));
            }
        }
    }
    public void checkDrawn(){
        int takenNum = 0;
        int cellMax = gameModel.getNumberOfRows() * gameModel.getNumberOfColumns();
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                if (gameModel.getCellOwner(i,j) != null){
                    takenNum++;
                }
            }
        }
        if(takenNum >= cellMax){
            gameModel.setGameDrawn();
        }
    }
    public void handleIncomingCommand(String command) throws OXOMoveException {
        // Convert the command to uppercase for case insensitivity
        command = command.toUpperCase();
        // Extract the row letter and column number from the command
        if(!gameModel.gameState()) {
             rowLetter = command.charAt(0);
             colNumber = Character.getNumericValue(command.charAt(1)) - 1;

        // Get the current player and set the cell owner to be the current player
             OXOPlayer currentPlayer = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
             gameModel.setCellOwner(rowLetter - 'A', colNumber, currentPlayer);

        // Switch to the next player
             int nextPlayerNumber = (gameModel.getCurrentPlayerNumber() +1) % gameModel.getNumberOfPlayers();
             gameModel.setCurrentPlayerNumber(nextPlayerNumber);
        }
        //Win detection operation

        checkWinner();
        checkDrawn();
    }
    public void addRow() {
        if (!gameModel.gameState()){
            gameModel.addRow();
        }
    }
    public void removeRow() {
        if (!gameModel.gameState()){
            gameModel.removeRow();
        }
    }
    public void addColumn() {
        if (!gameModel.gameState()){
            gameModel.addColumn();
        }
    }
    public void removeColumn() {
        if (!gameModel.gameState()){
            gameModel.removeColumn();
        }
    }
    public void increaseWinThreshold() {}
    public void decreaseWinThreshold() {}
    public void reset() {
        gameModel.setCurrentPlayerNumber(0);
        gameModel.setWinner(null);
        //clean the view.
        for (int i = 0; i < gameModel.getNumberOfRows(); i++) {
            for (int j = 0; j < gameModel.getNumberOfColumns(); j++) {
                gameModel.setCellOwner(i, j, null);
            }
        }
        gameModel.removeDrawn();
    }
}
