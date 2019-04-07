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
			this.xloc = xloc;
			this.yloc = yloc;
			
			
			grass.setImage(grassImg);
			grassPlace = 1;
			
			
			
			path.setImage(pathImg);
			pathPlace =1;
			
			
			
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
			
			//movesTile.setFitWidth(99);
			//movesTile.setPreserveRatio(true);

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
			
			if(column != 7)
			{
				this.getChildren().add(grass);
			}
			else
			{
				this.getChildren().add(path);
			}
			
			if((column == 1 && row == 1) || (column == 13 && row == 1))
			{
				this.getChildren().add(knightRed);
				c = new Fighter();
			}
			if((column == 1 && row == 8) || (column == 13 && row == 8))
			{
				this.getChildren().add(knightBlue);
				c = new Fighter();
			}
			if(column == 7 && row == 1)
			{
				this.getChildren().add(wizardRed);
				c = new Wizard();
			}
			if(column == 7 && row == 8)
			{
				this.getChildren().add(wizardBlue);
				c = new Wizard();
			}
			if(column == 4 && row == 1)
			{
				this.getChildren().add(clericRed);
				c = new Cleric();
			}
			if(column == 4 && row == 8)
			{
				this.getChildren().add(clericBlue);
				c = new Cleric();
			}
			if(column == 10 && row == 1)
			{
				this.getChildren().add(rogueRed);
				c = new Rogue();
			}
			if(column == 10 && row == 8)
			{
				this.getChildren().add(rogueBlue);
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
			//boolean bool = true;
			if(c != null)
			{
				HashSet<Pair> moves = CharacterAndBoardUtil.tempMoveList(xloc, yloc, box);
				//whereYouCanGo(moves);
				for(Pair cords : moves) {
					
						box[cords.getX()][cords.getY()].setStyle("-fx-border-color: yellow;");
						//System.out.println("Congrats you clicked");
				}
			}
		}
		
		/*private void whereYouCanGo(HashSet<Pair> yaMoves)
		{
			//Image movesTileImg = new Image(getClass().getResource("white.jpg").toExternalForm());
			//ImageView movesTile = new ImageView();
			//movesTile.setImage(movesTileImg);
			for(Pair cords : yaMoves) {
				if(box[cords.getX()][cords.getY()]==null)
				{
					box[cords.getX()][cords.getY()].setStyle("-fx-border-color: red;");
				}
			}
		}*/

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
