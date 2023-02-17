package edu.uob;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OXOController {
    OXOModel gameModel;
    OXOView gameView;
    char rowLetter;
    int colNumber = 0;
    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        // Convert the command to uppercase for case insensitivity
        command = command.toUpperCase();
        if(command.equalsIgnoreCase("A")){
            addRow();
        }
        // Extract the row letter and column number from the command
        rowLetter = command.charAt(0);
        colNumber = Character.getNumericValue(command.charAt(1)) - 1;

        // Get the current player and set the cell owner to be the current player
        OXOPlayer currentPlayer = gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber());
        gameModel.setCellOwner(rowLetter - 'A', colNumber, currentPlayer);

        // Switch to the next player
        int nextPlayerNumber = (gameModel.getCurrentPlayerNumber() +1) % gameModel.getNumberOfPlayers();
        gameModel.setCurrentPlayerNumber(nextPlayerNumber);

    }

    public void addRow() {
        gameModel.addRow();
    }
    public void removeRow() {
        gameModel.removeRow();
    }
    public void addColumn() {
        gameModel.addColumn();
    }
    public void removeColumn() {
        gameModel.removeColumn();
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
    }
}
