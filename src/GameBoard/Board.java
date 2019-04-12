package GameBoard;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

import Multiplayer.CharacterHandler;
import Multiplayer.MultiplayerHandler;
import character.BaseCharacter;
import character.Cleric;
import character.Fighter;
import character.Rogue;
import character.Wizard;
import charutil.CharacterAndBoardUtil;
import charutil.CharacterAndBoardUtil.Pair;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.misc.Queue;

public class Board extends Application {
	public Square[][] box = new Square[10][15];

	BorderPane border;

	Button restart;

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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		connected = bootMultiplayer();
		
		turnHandler = new turnHandler(false);

		border = new BorderPane();
		Scene scene = new Scene(border, 1500, 1000);

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

	}

	/**
	 * Restarts the game Accessible by multiplayer
	 */
	public void restart() {
		buildBoard();
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
					game.add(box[row][column] = new Square(row, column, row, column), column, row);
				} else {
					game.add(box[row][column] = new Square(row, column, row, column), column, row);
				}
			}
		}

		border.setRight(restart);
		border.setCenter(game);
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
	public void styleSquares(HashSet<Pair> moves, String color) {
		for (Pair cords : moves) {
			box[cords.getX()][cords.getY()].setStyle("-fx-border-color: " + color + "; ");
		}

	}

	public class Square extends StackPane {
		private int xloc, yloc;
		private CharacterHandler c;
		private Image grassImg = new Image(getClass().getResource("grassTile.jpg").toExternalForm());
		private ImageView grass = new ImageView();
		private Image pathImg = new Image(getClass().getResource("pathTile.jpg").toExternalForm());
		private ImageView path = new ImageView();
		private Image knightRedImg = new Image(getClass().getResource("pixelKnightRed.png").toExternalForm());
		private ImageView knightRed = new ImageView();
		private Image wizardRedImg = new Image(getClass().getResource("pixelWizardRed.png").toExternalForm());
		private ImageView wizardRed = new ImageView();
		private Image clericRedImg = new Image(getClass().getResource("pixelClericRed.png").toExternalForm());
		private ImageView clericRed = new ImageView();
		private Image rogueRedImg = new Image(getClass().getResource("pixelRogueRed.png").toExternalForm());
		private ImageView rogueRed = new ImageView();
		private Image knightBlueImg = new Image(getClass().getResource("pixelKnightBlue.png").toExternalForm());
		private ImageView knightBlue = new ImageView();
		private Image wizardBlueImg = new Image(getClass().getResource("pixelWizardBlue.png").toExternalForm());
		private ImageView wizardBlue = new ImageView();
		private Image clericBlueImg = new Image(getClass().getResource("pixelClericBlue.png").toExternalForm());
		private ImageView clericBlue = new ImageView();
		private Image rogueBlueImg = new Image(getClass().getResource("pixelRogueBlue.png").toExternalForm());
		private ImageView rogueBlue = new ImageView();
		private int grassPlace, pathPlace = 0;

		// Used to create and update the board
		public Square(int xloc, int yloc, int row, int column) {
			this.setXloc(xloc);
			this.setYloc(yloc);

			grass.setImage(grassImg);
			grassPlace = 1;

			path.setImage(pathImg);

			pathPlace = 1;

			knightRed.setImage(knightRedImg);

			wizardRed.setImage(wizardRedImg);

			clericRed.setImage(clericRedImg);

			rogueRed.setImage(rogueRedImg);

			knightBlue.setImage(knightBlueImg);

			wizardBlue.setImage(wizardBlueImg);

			clericBlue.setImage(clericBlueImg);

			rogueBlue.setImage(rogueBlueImg);

			// Background color is white, boarders are black
			this.setStyle("-fx-border-color: black;");
			this.setPrefSize(100, 100);
			this.setOnMouseClicked(e -> mouseEvent());

			// movesTile.setFitWidth(99);
			// movesTile.setPreserveRatio(true);

			grass.setFitWidth(99);
			grass.setPreserveRatio(true);

			path.setFitWidth(99);
			path.setPreserveRatio(true);

			knightRed.setFitWidth(99);
			knightRed.setPreserveRatio(true);

			wizardRed.setFitWidth(99);
			wizardRed.setPreserveRatio(true);

			clericRed.setFitWidth(99);
			clericRed.setPreserveRatio(true);

			rogueRed.setFitWidth(99);
			rogueRed.setPreserveRatio(true);

			knightBlue.setFitWidth(99);
			knightBlue.setPreserveRatio(true);

			wizardBlue.setFitWidth(99);
			wizardBlue.setPreserveRatio(true);

			clericBlue.setFitWidth(99);
			clericBlue.setPreserveRatio(true);

			rogueBlue.setFitWidth(99);
			rogueBlue.setPreserveRatio(true);

			if (column != 7) {
				this.getChildren().add(grass);
			} else {
				this.getChildren().add(path);
			}

			if ((column == 1 && row == 1) || (column == 13 && row == 1)) {
				this.getChildren().add(knightRed);

				c = new CharacterHandler(new Fighter(), new Point(row, column), row == 1);
			}
			if ((column == 1 && row == 8) || (column == 13 && row == 8)) {
				this.getChildren().add(knightBlue);
				c = new CharacterHandler(new Fighter(), new Point(row, column), row == 1);
			}
			if (column == 7 && row == 1) {
				this.getChildren().add(wizardRed);

				c = new CharacterHandler(new Wizard(), new Point(row, column), row == 1);
			}
			if (column == 7 && row == 8) {
				this.getChildren().add(wizardBlue);
				c = new CharacterHandler(new Wizard(), new Point(row, column), row == 1);
			}
			if (column == 4 && row == 1) {
				this.getChildren().add(clericRed);

				c = new CharacterHandler(new Cleric(), new Point(row, column), row == 1);

			}
			if (column == 4 && row == 8) {
				this.getChildren().add(clericBlue);
				c = new CharacterHandler(new Cleric(), new Point(row, column), row == 1);
			}
			if (column == 10 && row == 1) {
				this.getChildren().add(rogueRed);
				c = new CharacterHandler(new Rogue(), new Point(row, column), row == 1);
			}
			if (column == 10 && row == 8) {
				this.getChildren().add(rogueBlue);
				c = new CharacterHandler(new Rogue(), new Point(row, column), row == 1);

			}
			
			if(c!=null) turnHandler.add(c);
		}

		/**
		 * Handles the mouse event generated by clicking this tile
		 */
		public void mouseEvent() {
			HashSet<Pair> moves = CharacterAndBoardUtil.tempMoveList(getXloc(), getYloc(), box);

			// if this square occupied
			if (c != null && tempSquare != null && tempSquare.c != null) {

				// If this square is the last square
				if (this.equals(tempSquare)) {
					c.setClicked(false);
					styleSquares(moves, "black");
					tempSquare = null;

					// if combat needs to happen
				} else if (tempSquare.getHandler().getTeam() != getHandler().getTeam()
						&& Math.abs(tempSquare.getHandler().getX() - this.getXloc()) <= 1
						&& Math.abs(tempSquare.getHandler().getY() - this.getYloc()) <= 1) {

					// if attacker won
					if (CharacterAndBoardUtil.tempHandleCombat(tempSquare.c.getCharacter(), c.getCharacter())) {
						styleSquares(moves, "black");
						removeCharacter();
						tempSquare.getHandler().setClicked(false);
						styleSquares(
								CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box),
								"black");
						c = null;
						if (connected)
							connection.sendRemoveCharacter(this);

						// defender won
					} else {
						tempSquare.removeCharacter();
						styleSquares(
								CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box),
								"black");
						tempSquare.c = null;
						if (connected)
							connection.sendRemoveCharacter(tempSquare);
						tempSquare = null;

					}
				}
			}
			// If character not selected
			else if (c != null && !c.getClicked()) {
				c.setClicked(true);
				tempSquare = this;
				styleSquares(moves, "yellow");

				// if no character present and square clicked
			} else if (c == null && tempSquare != null) {
				if (Math.abs(tempSquare.getHandler().getX() - this.getXloc()) <= 1
						&& Math.abs(tempSquare.getHandler().getY() - this.getYloc()) <= 1) { // if in current range
					if (connected)
						connection.sendCharacterMove(tempSquare, this);
					styleSquares(CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box),
							"black");
					setStyle("-fx-border-color: black;");
					this.c = tempSquare.c;
					c.setPoint(new Point(getXloc(), getYloc()));
					getChildren().add(tempSquare.getChildren().get(tempSquare.getChildren().size() - 1));
					tempSquare.getChildren().remove(getChildren().get(getChildren().size() - 1));
					c.setClicked(false);
					tempSquare.c = null;
					tempSquare = null;

				}

			}
		}

		/*
		 * private void whereYouCanGo(HashSet<Pair> yaMoves) { //Image movesTileImg =
		 * new Image(getClass().getResource("white.jpg").toExternalForm()); //ImageView
		 * movesTile = new ImageView(); //movesTile.setImage(movesTileImg); for(Pair
		 * cords : yaMoves) { if(box[cords.getX()][cords.getY()]==null) {
		 * box[cords.getX()][cords.getY()].setStyle("-fx-border-color: red;"); } } }
		 */

		public void setState() {

		}

		public BaseCharacter getCharacter() {
			return c.getCharacter();
		}

		public CharacterHandler getHandler() {
			return c;
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
			getChildren().remove(getChildren().get(getChildren().size() - 1));

		}

	}

	/**
	 * Class designed to handle a queue of characters
	 * @author Ben
	 *
	 */
	public class turnHandler {
		private Boolean team;
		private int loc;
		private int size;
		public Queue<CharacterHandler> characterList = new Queue<>();
		public turnHandler(Boolean startTeam) {
			team = startTeam;
			loc = 0;
			size = 0;
			
		}
		
		public void add(CharacterHandler character) {
			characterList.enqueue(character);
			size++;
		}
		
		public CharacterHandler getNextCharacter() {
			if(loc > size) return null;
			loc++;
			try {
				CharacterHandler temp = characterList.dequeue();
				characterList.enqueue(temp);
				return temp;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			return null;
			
		}
		
		public void resetLoc() {
			loc = 0;
		}
		
		public boolean hasNextCharacter() {
			return loc<size;
		}
		
		public void setTeam(Boolean team) {
			this.team = team;
		}
		
		public Boolean getTeam() {
			return team;
		}
		
		public void loopTest() {
			while (hasNextCharacter()) {
				System.out.println(getNextCharacter());
			}
			System.out.println("Toggling");
			resetLoc();
			while (hasNextCharacter()) {
				System.out.println(getNextCharacter());
			}
		}
	}

}
