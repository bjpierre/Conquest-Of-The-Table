package GameBoard;

import GameBoard.characterImages;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

import Multiplayer.CharacterHandler;
import Multiplayer.MultiplayerHandler;
import character.BaseCharacter;
import charutil.AIUtil;
import charutil.CharacterAndBoardUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.misc.Queue;

public class Board extends Application {
	public Square[][] box = new Square[10][15];
	private BaseCharacter[][] characters;

	BorderPane border;

	Button restart;

	Label text;

	String old;

	/**
	 * Temp variable to track moving a unit
	 */
	protected Boolean moveCharacter;

	protected Square tempSquare;

	/**
	 * Connection to multi-player handler
	 */
	protected MultiplayerHandler connection;

	/**
	 * Variable to keep track of whether the server is connected or not
	 */
	protected Boolean connected;
	/**
	 * Local class to handle turns
	 */
	protected turnHandler turnHandler;

	public double sceneH;

	public double sceneW;

	protected Boolean clientTeam;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		sceneH = screenSize.getHeight();
		sceneW = screenSize.getWidth();

		// those in the first row are team 1, second team 2
		characters = new BaseCharacter[2][5];

		connected = bootMultiplayer();

		if (clientTeam != null) {
			System.out.println("You are representing team " + (clientTeam ? "blue!" : "red!"));
		}
		turnHandler = new turnHandler(false, VsPlayerPrompt());

		border = new BorderPane();
		Scene scene = new Scene(border, sceneW, sceneH);

		restart = new Button("Restart Game");
		restart.setPadding(new Insets(10, 10, 10, 10));
		restart.setOnAction(e -> {
			if (connected)
				connection.sendRestart();
			restart();
		});

		buildBoard();
		stage.setTitle("Temp");
		stage.setScene(scene);
		stage.show();
		// Override on close process to add leaving server to list of commands
		stage.setOnCloseRequest(event -> {
			if (connected)
				connection.leaveServer();

		});

		stage.setFullScreen(true);

	}

	/**
	 * Restarts the game Accessible by multiplayer
	 */
	public void restart() {
		buildBoard();
	}

	public Boolean VsPlayerPrompt() {
		int response = JOptionPane.showConfirmDialog(null, "Would you like to play against AI?", "Mode Select",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return response == JOptionPane.YES_OPTION;
	}

	public void setText(String t) {
		text = new Label(t);
		text.setPadding(new Insets(10, 10, 10, 10));
		this.border.setBottom(text);
	}

	/**
	 * Rebuilds the board to the default state
	 */
	private void buildBoard() {
		GridPane game = new GridPane();
		int row, column;
		for (row = 0; row < 10; row++) {
			for (column = 0; column < 15; column++) {
				if (column == 7) {
					game.add(box[row][column] = new Square(row, column, row, column, characters), column, row);
				} else {
					game.add(box[row][column] = new Square(row, column, row, column, characters), column, row);
				}
			}
		}

		border.setRight(restart);
		border.setCenter(game);
		old = "Start!";
		setText(old);
		moveCharacter = false; // Selecting Character
		tempSquare = null; // No square being moved ATM

	}

	/**
	 * launches a multiplayer connectoin to the server host
	 * 
	 * @return status of the connection, false if aborted, true if successful
	 */
	private boolean bootMultiplayer() {
		try {
			connection = new MultiplayerHandler("localhost", 5656, this);
			Thread.sleep(500);
			clientTeam = connection.getTeam();
			return true;
		} catch (Exception E) {
			System.out.println("Unable to connect to server");
			return false;
		}
	}

	/**
	 * Sets the style of grid saures
	 * 
	 * @param moves
	 *            The places to set the color of
	 * @param color
	 *            the outline to use
	 */
	public void styleSquares(HashSet<Point> moves, String color) {
		for (Point cords : moves) {
			box[(int) cords.getX()][(int) cords.getY()].setStyle("-fx-border-color: " + color + "; ");
		}

	}

	/**
	 * Gets a turn handler
	 * 
	 * @return the turn handler
	 */
	public turnHandler getTurnHandler() {
		return turnHandler;
	}

	public class Square extends StackPane {
		private int xloc, yloc;
		public BaseCharacter c;
		private Image grassImg = new Image(getClass().getResource("grassTile.jpg").toExternalForm());
		private ImageView grass = new ImageView();
		private Image pathImg = new Image(getClass().getResource("pathTile.jpg").toExternalForm());
		private ImageView path = new ImageView();
		private Boolean grassPlace, pathPlace = false;

		// Used to create and update the board
		public Square(int xloc, int yloc, int row, int column, BaseCharacter[][] characters) {
			this.setXloc(xloc);
			this.setYloc(yloc);

			grass.setImage(grassImg);
			grassPlace = true;

			path.setImage(pathImg);

			pathPlace = true;

			// Background color is white, boarders are black
			this.setStyle("-fx-border-color: black;");
			this.setPrefSize(76, 76);
			this.setOnMouseClicked(e -> mouseEvent());
			this.setOnMouseEntered(e -> mouseHover());
			// movesTile.setFitWidth(99);
			// movesTile.setPreserveRatio(true);

			grass.setFitWidth(75);
			grass.setPreserveRatio(true);

			path.setFitWidth(75);
			path.setPreserveRatio(true);

			if (column != 7) {
				this.getChildren().add(grass);
			} else {
				this.getChildren().add(path);
			}

			@SuppressWarnings("unused")
			characterImages s = new characterImages(this, row, column, characters);

			// if (c != null)
			// turnHandler.addCharacter(c);
		}

		public void mouseHover() {
			if (this.c != null) {
				old = text.getText();
				setText("HP: " + this.c.getHP() + " Name: " + this.c.getName());
			} else {
				setText(old);
			}
		}

		/**
		 * Handles the mouse event generated by clicking this tile
		 */
		public void mouseEvent() {
			HashSet<Point> moves;
			Point here = new Point(xloc, yloc);

			if (clientTeam != null && !turnHandler.vsAI && turnHandler.team == clientTeam) {
				return;
			}

			// If it is AI's turn, don't do anything
			if (turnHandler.vsAI && turnHandler.team == true)
				return;

			if (tempSquare == null) {
				moves = CharacterAndBoardUtil.tempMoveList(xloc, yloc, box);
				// first click
				if (c != null && !c.getClicked() && c.getTeam() == turnHandler.team) {
					c.setClicked(true);
					tempSquare = this;
					styleSquares(moves, "yellow");
				}
			} else {
				moves = CharacterAndBoardUtil.tempMoveList(tempSquare.xloc, tempSquare.yloc, box);
				// second click
				if (this.equals(tempSquare)) {
					// unselect
					c.setClicked(false);
					styleSquares(moves, "black");
					tempSquare = null;
				} else if (moves.contains(here)) {
					// select move or attack
					if (c == null) {
						// send the move to other player
						if (connected)
							connection.sendCharacterMove(tempSquare, this);

						// style the squares back
						styleSquares(moves, "black");
						setStyle("-fx-border-color: black;");

						// moves sprites
						List<Node> l = tempSquare.getChildren();
						Node sprite = l.get(l.size() - 1);
						this.addCharacter(tempSquare.c, sprite);
						tempSquare.removeCharacter();

						c.setClicked(false);

						turnHandler.endTurn();
					} else if (tempSquare.c.getTeam() != c.getTeam()) {
						// combat
						if (CharacterAndBoardUtil.handleCombat(tempSquare.getCharacter(), c)) {
							System.out.println("PLAYER " + (turnHandler.team ? 2 : 1) + " HITs");
							if (connected)
								connection.sendRemoveCharacter(this);
							removeCharacter();
							this.getChildren().remove(this.getChildren().size() - 1);
						} else {
							System.out.println("PLAYER " + (turnHandler.team ? 2 : 1) + " MISSes");
						}

						styleSquares(moves, "black");
						tempSquare.c.setClicked(false);
						turnHandler.endTurn();
					}
				}
			}
		}

		public BaseCharacter getCharacter() {
			return c;
		}

		public CharacterHandler getHandler() {
			return null;
		}

		public int getXloc() {
			return xloc;
		}

		public void setXloc(int xloc) {
			this.xloc = xloc;
		}

		public int getYloc() {
			return yloc;
		}

		public void setYloc(int yloc) {
			this.yloc = yloc;
		}

		/**
		 * removes the character object and sprite associated with this square
		 */
		public void removeCharacter() {
			c = null;
			// this.getChildren().remove(this.getChildren().size()-1);
		}

		public void addCharacter(BaseCharacter bc, Node sprite) {
			c = bc;
			c.setLoc(xloc, yloc);
			getChildren().add(sprite);
		}

		public String toString() {
			return "(" + xloc + ", " + yloc + ")";
		}

		public Boolean getGrassPlace() {
			return grassPlace;
		}


		public Boolean getPathPlace() {
			return pathPlace;
		}

	}

	/**
	 * Class designed to handle a queue of characters
	 * 
	 * @author Ben
	 *
	 */
	public class turnHandler {
		private boolean team, vsAI;
		private int loc;
		private int size;
		public Queue<BaseCharacter> characterList = new Queue<>();
		public BaseCharacter current;

		public turnHandler(boolean startTeam, boolean isVsAI) {
			team = startTeam;
			loc = 0;
			size = 0;
			vsAI = isVsAI;
		}

		/**
		 * adds a character to the que
		 * 
		 * @param character
		 */
		public void addCharacter(BaseCharacter character) {
			characterList.enqueue(character);
			size++;
		}

		/**
		 * returns the next character in the list
		 * 
		 * @return the next character, null if at end of que;
		 */
		public BaseCharacter getNextCharacter() {
			if (loc > size)
				return null;
			loc++;
			try {
				BaseCharacter temp = characterList.dequeue();
				characterList.enqueue(temp);
				return temp;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;

		}

		public void endTurnMultiplayer() {
			team = !team;
			tempSquare = null;
		}

		public void endTurn() {
			team = !team;
			tempSquare = null;
			if (connected)
				connection.sendToggleTurn();
			if (vsAI && team == true)
				playAI();
		}

		private void playAI() {
			AIUtil.AIUpdateBoard(characters, box);
			team = !team;
		}

		/**
		 * restacks the que;
		 */
		public void restackQue() {
			loc = 0;
		}

		/**
		 * Are we at the end of a loop?
		 * 
		 * @return resetLoc() not needed if true
		 */
		public boolean hasNextCharacter() {
			return loc < size;
		}

		/**
		 * sets the team
		 * 
		 * @param team
		 *            true for team one, false for team two
		 */
		public void setTeam(boolean team) {
			this.team = team;
		}

		/**
		 * Returns team in play
		 * 
		 * @return true for team one, false for team two
		 */
		public boolean getTeam() {
			return team;
		}

		/**
		 * demo method to prove a it works
		 */
		public void loopTest() {
			while (hasNextCharacter()) {
				System.out.println(getNextCharacter());
			}
			System.out.println("Toggling");
			restackQue();
			while (hasNextCharacter()) {
				System.out.println(getNextCharacter());
			}
		}
	}

}
