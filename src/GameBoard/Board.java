package GameBoard;

import java.util.HashSet;

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
	 * Connection to multi-player handler
	 */
	MultiplayerHandler connection;
	
	/**
	 * Variable to keep track of whether the server is connected or not
	 */
	Boolean connected;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		
		//Connect to multi-player or set flag indicating no connection
		try {
			connection = new MultiplayerHandler("lcoalhost", 4444, box);
			connected = true;
		}catch(Exception E) {
			System.out.println("Unable to connect to server");
			connected = false;
		}

		border = new BorderPane();
		GridPane game = new GridPane();
		Scene scene = new Scene(border, 1500, 1000);

		restart = new Button("Restart Game");
		restart.setPadding(new Insets(10, 10, 10, 10));
		restart.setOnAction(e -> restart());

		int row, column;

		for (row = 0; row < 10; row++) {
			for (column = 0; column < 15; column++) {
				if(column == 7)
				{
					game.add(box[row][column] = new Square(row, column, row, column), column, row);
				}
				else
				{
					game.add(box[row][column] = new Square(row, column, row, column), column, row);
				}
			}
		}
		border.setRight(restart);
		border.setCenter(game);

		stage.setTitle("Temp");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			//Override on close process to add leaving server to list of commands
			@Override
			public void handle(WindowEvent event) {
				if(connected) connection.leaveServer();

			}

		});
		
	}

	private void restart() {
		if(connected) connection.sendRestart();
		int row, column;

		for (row = 0; row < 10; row++) {
			for (column = 0; column < 15; column++) {
				box[row][column].setState();
			}
		}
	}

	public class Square extends StackPane {
		private int xloc, yloc;
		private BaseCharacter c;

		// Used to create and update the board
		public Square(int xloc, int yloc, int row, int column) {
			this.xloc = xloc;
			this.yloc = yloc;
			
			Image grassImg = new Image(getClass().getResource("grassTile.jpg").toExternalForm());
			ImageView grass = new ImageView();
			grass.setImage(grassImg);
			
			
			Image pathImg = new Image(getClass().getResource("pathTile.jpg").toExternalForm());
			ImageView path = new ImageView();
			path.setImage(pathImg);
			
			
			Image knightImg = new Image(getClass().getResource("pixelKnight.png").toExternalForm());
			ImageView knight = new ImageView();
			knight.setImage(knightImg);
			
			Image wizardImg = new Image(getClass().getResource("pixelWizard.png").toExternalForm());
			ImageView wizard = new ImageView();
			wizard.setImage(wizardImg);
			
			Image clericImg = new Image(getClass().getResource("pixelCleric.png").toExternalForm());
			ImageView cleric = new ImageView();
			cleric.setImage(clericImg);
			
			Image rogueImg = new Image(getClass().getResource("pixelRogue.png").toExternalForm());
			ImageView rogue = new ImageView();
			rogue.setImage(rogueImg);
			
			Image movesTileImg = new Image(getClass().getResource("white.jpg").toExternalForm());
			ImageView movesTile = new ImageView();
			movesTile.setImage(movesTileImg);
			
			// Background color is white, boarders are black
			this.setStyle("-fx-border-color: black;");
			this.setPrefSize(100, 100);
			this.setOnMouseClicked(e -> mouseEvent());
			
			movesTile.setFitWidth(99);
			movesTile.setPreserveRatio(true);

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
			
			if(column != 7)
			{
				this.getChildren().add(grass);
			}
			else
			{
				this.getChildren().add(path);
			}
			
			if((column == 1 && row == 1) || (column == 1 && row == 8) || (column == 13 && row == 1) || (column == 13  && row == 8))
			{
				this.getChildren().add(knight);
				c = new Fighter();
			}
			if((column == 7 && row == 1) || (column == 7 && row == 8))
			{
				this.getChildren().add(wizard);
				c = new Wizard();
			}
			if((column == 4 && row == 1) || (column == 4 && row == 8))
			{
				this.getChildren().add(cleric);
				c = new Cleric();
			}
			if((column == 10 && row == 1) || (column == 10 && row == 8))
			{
				this.getChildren().add(rogue);
				c = new Rogue();
			}
		}

		// Gets mouse event
		/*EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				EventType<MouseEvent> NULL = null;
				if(MouseEvent.MOUSE_CLICKED!=NULL)
				{
					int x = MouseEvent.MOUSE_CLICKED.getSceneX();
					int y = MouseEvent.MOUSE_CLICKED.getSceneY();
				}
				
				
			}
		};*/
		public void mouseEvent() {
			if(connected) connection.createX(xloc, yloc);
			if(c != null)
			{
				HashSet<Pair> moves = CharacterAndBoardUtil.moveList(xloc, yloc, box);
				whereYouCanGo(moves);
			}
		}
		
		private void whereYouCanGo(HashSet<Pair> yaMoves)
		{
			Image movesTileImg = new Image(getClass().getResource("white.jpg").toExternalForm());
			ImageView movesTile = new ImageView();
			movesTile.setImage(movesTileImg);
			for(Pair cords : yaMoves) {
				if(box[cords.getY()][cords.getX()]==null)
				{
					box[cords.getY()][cords.getX()].setStyle("-fx-border-color: red;");
				}
			}
		}

		public void setState() {

		}

		public int getX() {
			return xloc;
		}

		public int getY() {
			return yloc;
		}
		
		public BaseCharacter getCharacter() {
			return c;
		}
	}
}
