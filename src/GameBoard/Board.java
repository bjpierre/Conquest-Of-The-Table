package GameBoard;

import java.awt.Point;
import java.util.HashSet;

import Multiplayer.CharacterHandler;
import Multiplayer.MultiplayerHandler;
import character.BaseCharacter;
import character.Cleric;
import character.Fighter;
import character.Rogue;
import character.Wizard;
import charutil.AIUtil;
import charutil.CharacterAndBoardUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
		//those in the first row are team 1, second team 2
		characters = new BaseCharacter[2][5];
		
		connected = bootMultiplayer();

		turnHandler = new turnHandler(false, true);

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
	public void styleSquares(HashSet<Point> moves, String color) {
		for (Point cords : moves) {
			box[(int) cords.getX()][(int) cords.getY()].setStyle("-fx-border-color: " + color + "; ");
		}

	}

	public class Square extends StackPane {
		private int xloc, yloc;
		private BaseCharacter c;
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
			this.setPrefSize(76, 76);
			this.setOnMouseClicked(e -> mouseEvent());

			// movesTile.setFitWidth(99);
			// movesTile.setPreserveRatio(true);

			grass.setFitWidth(75);
			grass.setPreserveRatio(true);

			path.setFitWidth(75);
			path.setPreserveRatio(true);

			knightRed.setFitWidth(75);
			knightRed.setPreserveRatio(true);

			wizardRed.setFitWidth(75);
			wizardRed.setPreserveRatio(true);

			clericRed.setFitWidth(75);
			clericRed.setPreserveRatio(true);

			rogueRed.setFitWidth(75);
			rogueRed.setPreserveRatio(true);

			knightBlue.setFitWidth(75);
			knightBlue.setPreserveRatio(true);

			wizardBlue.setFitWidth(75);
			wizardBlue.setPreserveRatio(true);

			clericBlue.setFitWidth(75);
			clericBlue.setPreserveRatio(true);

			rogueBlue.setFitWidth(75);
			rogueBlue.setPreserveRatio(true);

			if (column != 7) {
				this.getChildren().add(grass);
			} else {
				this.getChildren().add(path);
			}

			if (column == 1 && row == 1) {
				this.getChildren().add(knightRed);
				c = new Fighter(1, 1, false);
				characters[0][4] = c;
			}
			if (column == 13 && row == 1) {
				this.getChildren().add(knightRed);
				c = new Fighter(1, 1, false);
				characters[0][0] = c;
			}
			if (column == 1 && row == 8) {
				this.getChildren().add(knightBlue);
				c = new Fighter(1, 8, true);
				characters[1][0] = c;
			}
			if (column == 13 && row == 8) {
				this.getChildren().add(knightBlue);
				c = new Fighter(13, 8, true);
				characters[1][4] = c;
			}
			if (column == 7 && row == 1) {
				this.getChildren().add(wizardRed);
				c = new Wizard(7,1,false);
				characters[0][1] = c;
			}
			if (column == 7 && row == 8) {
				this.getChildren().add(wizardBlue);
				c = new Wizard(7,1,true);
				characters[1][1] = c;
			}
			if (column == 4 && row == 1) {
				this.getChildren().add(clericRed);
				c = new Cleric(4,1,false);
				characters[0][2] = c;
			}
			if (column == 4 && row == 8) {
				this.getChildren().add(clericBlue);
				c = new Cleric(4,8,true);
				characters[1][2] = c;
			}
			if (column == 10 && row == 1) {
				this.getChildren().add(rogueRed);
				c = new Rogue(10,1,false);
				characters[0][3] = c;
			}
			if (column == 10 && row == 8) {
				this.getChildren().add(rogueBlue);
				c = new Rogue(10,8,true);
				characters[1][3] = c;
			}
		}

		/**
		 * Handles the mouse event generated by clicking this tile
		 */
		public void mouseEvent() {
			HashSet<Point> moves = CharacterAndBoardUtil.tempMoveList(getXloc(), getYloc(), box);
			Point here = new Point(xloc, yloc);
			// if this square occupied and not an ai's turn
			if (!(turnHandler.vsAI && turnHandler.team) && c != null && tempSquare != null && tempSquare.c != null) {

				// If this square is the last square
				if (this.equals(tempSquare)) {
					c.setClicked(false);
					styleSquares(moves, "black");
					tempSquare = null;

					// if combat needs to happen
				} else if (tempSquare.c.getTeam() != c.getTeam()
						&& CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box).contains(here)) {
					// if attacker won
					if (CharacterAndBoardUtil.tempHandleCombat(tempSquare.c, c)) {
						styleSquares(moves, "black");
						removeCharacter();
						c.setClicked(false);
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
					turnHandler.endTurn();
				}
			}
			// If character not selected
			else if (c != null && !c.getClicked()) {
				c.setClicked(true);
				tempSquare = this;
				styleSquares(moves, "yellow");

				// if no character present and square clicked
			} else if (c == null && tempSquare != null) {

				if (CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box).contains(here))
				{
					if (connected)
						connection.sendCharacterMove(tempSquare, this);

					styleSquares(CharacterAndBoardUtil.tempMoveList(tempSquare.getXloc(), tempSquare.getYloc(), box),
							"black");
					setStyle("-fx-border-color: black;");
					this.c = tempSquare.c;
					this.c.setLoc(xloc, yloc);
					getChildren().add(tempSquare.getChildren().get(tempSquare.getChildren().size() - 1));
					tempSquare.getChildren().remove(getChildren().get(getChildren().size() - 1));
					c.setClicked(false);
					tempSquare.c = null;
					tempSquare = null;
					turnHandler.endTurn();
				}

			}
		}

		public void setState() {

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
			getChildren().remove(getChildren().get(getChildren().size() - 1));

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

		public void endTurn() {
			team = !team;
			if(vsAI && team == true)
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
