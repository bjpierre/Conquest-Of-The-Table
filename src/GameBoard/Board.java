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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		// Connect to multi-player or set flag indicating no connection
		try {
			connection = new MultiplayerHandler("localhost", 5656, this);
			connected = true;
		} catch (Exception E) {
			System.out.println("Unable to connect to server");
			connected = false;
		}

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

	public class Square extends StackPane {
		private int xloc, yloc;
		private CharacterHandler c;
		private Image grassImg = new Image(getClass().getResource("grassTile.jpg").toExternalForm());
		private ImageView grass = new ImageView();
		private Image pathImg = new Image(getClass().getResource("pathTile.jpg").toExternalForm());
		private ImageView path = new ImageView();
		private Image knightImg = new Image(getClass().getResource("pixelKnight.png").toExternalForm());
		private ImageView knight = new ImageView();
		private Image wizardImg = new Image(getClass().getResource("pixelWizard.png").toExternalForm());
		private ImageView wizard = new ImageView();
		private Image clericImg = new Image(getClass().getResource("pixelCleric.png").toExternalForm());
		private ImageView cleric = new ImageView();
		private Image rogueImg = new Image(getClass().getResource("pixelRogue.png").toExternalForm());
		private ImageView rogue = new ImageView();
		private int grassPlace, pathPlace = 0;

		// Used to create and update the board
		public Square(int xloc, int yloc, int row, int column) {
			this.setXloc(xloc);
			this.setYloc(yloc);

			grass.setImage(grassImg);
			grassPlace = 1;

			path.setImage(pathImg);
			pathPlace = 1;

			knight.setImage(knightImg);

			wizard.setImage(wizardImg);

			cleric.setImage(clericImg);

			rogue.setImage(rogueImg);

			// Image movesTileImg = new
			// Image(getClass().getResource("white.jpg").toExternalForm());
			// ImageView movesTile = new ImageView();
			// movesTile.setImage(movesTileImg);

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

			knight.setFitWidth(99);
			knight.setPreserveRatio(true);

			wizard.setFitWidth(99);
			wizard.setPreserveRatio(true);

			cleric.setFitWidth(99);
			cleric.setPreserveRatio(true);

			rogue.setFitWidth(99);
			rogue.setPreserveRatio(true);

			if (column != 7) {
				this.getChildren().add(grass);
			} else {
				this.getChildren().add(path);
			}

			if ((column == 1 && row == 1) || (column == 1 && row == 8) || (column == 13 && row == 1)
					|| (column == 13 && row == 8)) {
				this.getChildren().add(knight);
				c = new CharacterHandler(new Fighter(), new Point(row, column), row == 1);
			}
			if ((column == 7 && row == 1) || (column == 7 && row == 8)) {
				this.getChildren().add(wizard);
				c = new CharacterHandler(new Wizard(), new Point(row, column), row == 1);
			}
			if ((column == 4 && row == 1) || (column == 4 && row == 8)) {
				this.getChildren().add(cleric);
				c = new CharacterHandler(new Cleric(), new Point(row, column), row == 1);
			}
			if ((column == 10 && row == 1) || (column == 10 && row == 8)) {
				this.getChildren().add(rogue);
				c = new CharacterHandler(new Rogue(), new Point(row, column), row == 1);
			}
		}

		// Gets mouse event
		/*
		 * EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
		 * public void handle(MouseEvent e) { EventType<MouseEvent> NULL = null;
		 * if(MouseEvent.MOUSE_CLICKED!=NULL) { int x =
		 * MouseEvent.MOUSE_CLICKED.getSceneX(); int y =
		 * MouseEvent.MOUSE_CLICKED.getSceneY(); }
		 * 
		 * 
		 * } };
		 */
		public void mouseEvent() {
			HashSet<Pair> moves = CharacterAndBoardUtil.tempMoveList(getXloc(), getYloc(), box);
			// if another character is moving and this is not a valid square
			if (c != null && tempSquare != null && tempSquare.c != null) {
				System.out.println("In first box");
				if (tempSquare.getHandler().getTeam() != getHandler().getTeam()
						&& Math.abs(tempSquare.getHandler().getX() - this.getXloc()) <= 1
						&& Math.abs(tempSquare.getHandler().getY() - this.getYloc()) <= 1) {
					if (CharacterAndBoardUtil.tempHandleCombat(tempSquare.c.getCharacter(), c.getCharacter())) { // if
																													// attacker
																													// won
						System.out.println("Attacker Won");
						styleSquares(moves, "black");
						getChildren().remove(getChildren().get(getChildren().size() - 1));
						c = null;
						if (connected)
							connection.sendRemoveCharacter(this);

					} else {
						System.out.println("Defender won");
						tempSquare.getChildren().remove(tempSquare.getChildren().get(getChildren().size() - 1));
						styleSquares(
								CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box),
								"black");
						tempSquare.c = null;
						tempSquare = null;
						if (connected)
							connection.sendRemoveCharacter(tempSquare);

					}
				}
			}
			// If character not selected
			else if (c != null && !c.getClicked()) {
				c.setClicked(true);
				tempSquare = this;
				styleSquares(moves, "yellow");

				// If character selected
			} else if (c != null && c.getClicked()) {
				c.setClicked(false);
				styleSquares(moves, "none");
				tempSquare.c = null;
				tempSquare = null;

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
}
