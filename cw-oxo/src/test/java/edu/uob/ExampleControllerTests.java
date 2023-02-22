package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import edu.uob.OXOMoveException.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

class ExampleControllerTests {
  private OXOModel model;
  private OXOController controller;

  // Make a new "standard" (3x3) board before running each test case (i.e. this method runs before every `@Test` method)
  // In order to test boards of different sizes, winning thresholds or number of players, create a separate test file (without this method in it !)
  @BeforeEach
  void setup() {
    model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    controller = new OXOController(model);
  }

  // This next method is a utility function that can be used by any of the test methods to _safely_ send a command to the controller
  void sendCommandToController(String command) {
    // Try to send a command to the server - call will timeout if it takes too long (in case the server enters an infinite loop)
    // Note: this is ugly code and includes syntax that you haven't encountered yet
    String timeoutComment = "Controller took too long to respond (probably stuck in an infinite loop)";
    assertTimeoutPreemptively(Duration.ofMillis(1000), () -> controller.handleIncomingCommand(command), timeoutComment);
  }

  // Test simple move taking and cell claiming functionality
  @Test
  void testBasicMoveTaking() throws OXOMoveException {
    // Find out which player is going to make the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a move
    sendCommandToController("a1");
    // Check that A1 (cell [0,0] on the board) is now "owned" by the first player
    String failedTestComment = "Cell a1 wasn't claimed by the first player";
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0), failedTestComment);
  }

  // Test out basic win detection
  @Test
  void testBasicWin() throws OXOMoveException {
    // Find out which player is going to make the first move (they should be the eventual winner)
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("a2"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player

    // a1, a2, a3 should be a win for the first player (since players alternate between moves)
    // Let's check to see whether the first moving player is indeed the winner
    String failedTestComment = "Winner was expected to be " + firstMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(firstMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
    //Test out win detection for Row line.
  void testBasicWinInRow_OWin() throws OXOMoveException {
    // Mark the second player.
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 1);
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("b3"); // Second player
    String failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }

  @Test
  void testBasicWinInCol_OWin() throws OXOMoveException {
    // Mark the second player.
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 1);
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("c2"); // Second player
    String failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }

  //Normal test for basic win in diagonal in 3*3 board.
  @Test
  void testBasicWinInDia_OWin_3_3() throws OXOMoveException {
    // Mark the second player.
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 1);
    // Make a bunch of moves for the two players
    sendCommandToController("a2"); // First player
    sendCommandToController("a1"); // Second player
    sendCommandToController("b1"); // First player
    sendCommandToController("b2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("c3"); // Second player
    String failedTestComment = "Winner was expected to be " + secondMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(secondMovingPlayer, model.getWinner(), failedTestComment);
  }

  //Test when board is standard(3*3), the drawn is right.
  @Test
  void drawnTest_3_3() throws OXOMoveException {
    // Mark the second player.
    sendCommandToController("a1");
    sendCommandToController("a2");
    sendCommandToController("a3");
    sendCommandToController("b2");
    sendCommandToController("b1");
    sendCommandToController("b3");
    sendCommandToController("c1");
    sendCommandToController("c2");
    sendCommandToController("c3");
    String failedTestComment = "The game statement should be drawn, but it's not";

  }

  @Test
  void testMorePeople() throws OXOMoveException {
    model.addPlayer(new OXOPlayer('Y'));
    //Mark the third player
    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber() + 2);
    // Make a bunch of moves for the two players
    sendCommandToController("a1"); // First player
    sendCommandToController("a2"); // Second player
    sendCommandToController("a3"); // First player
    sendCommandToController("b1"); // Second player
    sendCommandToController("b2"); // First player
    sendCommandToController("b3"); // Second player
    sendCommandToController("c2"); // Second player
    sendCommandToController("c1"); // First player
    sendCommandToController("c3"); // Second player
    String failedTestComment = "Winner was expected to be " + thirdMovingPlayer.getPlayingLetter() + " but wasn't";
    assertEquals(thirdMovingPlayer, model.getWinner(), failedTestComment);
  }


  //Exception test.
  // InvalidIdentifierLengthException
  @Test
  void testInvalidLength() throws OXOMoveException {
    // Check that the controller throws a suitable exception when it gets an invalid command
    String failedTestComment = "Controller failed to throw an InvalidIdentifierLengthException for command `abc123`";
    // The next lins is a bit ugly, but it is the easiest way to test exceptions (soz)
    assertThrows(InvalidIdentifierLengthException.class, () -> sendCommandToController("a123"), failedTestComment);
  }
  // Invalid characters
  @Test
  void testInvalidCharacter() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw an InvalidCharacterLengthException for this condition";
    assertThrows(InvalidIdentifierCharacterException.class, () -> sendCommandToController("11"), failedTestComment);
  }

  //Cell taken.
  //When O is trying to take X's cell.
  @Test
  void testCellTaken() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw CellAlreadyTakenException for this condition";
    sendCommandToController("a1");//X has been set at a1.
    assertThrows(CellAlreadyTakenException.class, () -> sendCommandToController("a1"), failedTestComment);
  }

  //Out of bounds.
  //When X is trying to take cell in out of bounds.
  @Test
  void testOutOfBounds() throws OXOMoveException {
    String failedTestComment = "Controller failed to throw OutsideCellRangeException for this condition";
    assertThrows(OutsideCellRangeException.class, () -> sendCommandToController("D4"), failedTestComment);
  }

  //
}