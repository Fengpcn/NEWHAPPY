package edu.uob;

import edu.uob.OXOMoveException.*;
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
        int rowNUmber;
        //Handling exceptions regarding command length
        if (command.length() != 2) {
            throw new InvalidIdentifierLengthException(command.length());
        }
        //Handling exceptions about first character type
        if (!Character.isLetter(command.charAt(0))) {
            throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, command.charAt(0));
        }
        //Handling exceptions about second character type
        if (!Character.isDigit(command.charAt(1))) {
            throw new InvalidIdentifierCharacterException(RowOrColumn.COLUMN, command.charAt(1));
        }
        // Convert the command to uppercase for case insensitivity
        command = command.toUpperCase();
        // Extract the row letter and column number from the command
        if(!gameModel.gameState()) {
             rowLetter = command.charAt(0);
             colNumber = Character.getNumericValue(command.charAt(1)) - 1;
             rowNUmber = rowLetter - 'A';
            //Handling out-of-board exceptions
            if (rowNUmber < 0 || rowNUmber >= gameModel.getNumberOfRows()) {
                throw new OutsideCellRangeException(RowOrColumn.ROW, rowNUmber);
            }
            if (colNumber < 0 || colNumber >= gameModel.getNumberOfColumns()) {
                throw new OutsideCellRangeException(RowOrColumn.COLUMN, colNumber);
            }
            //Handling cell already taken Exception
            if (gameModel.getCellOwner(rowNUmber, colNumber) != null) {
                throw new CellAlreadyTakenException(rowNUmber, colNumber);
            }
        // Get the current player and set the cell owner to be the current player
             OXOPlayer currentPlayer = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
             gameModel.setCellOwner(rowNUmber, colNumber, currentPlayer);

        // Switch to the next player
             int nextPlayerNumber = (gameModel.getCurrentPlayerNumber() +1) % gameModel.getNumberOfPlayers();
             gameModel.setCurrentPlayerNumber(nextPlayerNumber);
        }
        //Win and drawn detection operation
        checkWinner();
        checkDrawn();
    }
    public void addRow() {
        gameModel.addRow();
        gameModel.removeDrawn();
    }
    public void removeRow() {
        if (!gameModel.gameState()){
            gameModel.removeRow();
        }
    }
    public void addColumn() {
        gameModel.addColumn();
        gameModel.removeDrawn();
    }
    public void removeColumn() {
        if (!gameModel.gameState()){
            gameModel.removeColumn();
        }
    }
    public void increaseWinThreshold() {
        gameModel.increaseWinThreshold();
    }

    public void decreaseWinThreshold() {
        if (gameModel.getWinThreshold()>3){
            gameModel.decreaseWinThreshold();
        }
    }
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
