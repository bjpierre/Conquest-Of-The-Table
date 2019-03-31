package GameBoard;

import Multiplayer.MultiplayerHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
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

	MultiplayerHandler connection;
	
	Boolean connected;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			connection = new MultiplayerHandler("localhost", 4444, box);
			connected = true;
		}catch(Exception E) {
			//E.printStackTrace();
			System.out.println("Unable to connect to server");
			connected = false;
		}

		border = new BorderPane();
		GridPane game = new GridPane();
		Scene scene = new Scene(border, 1500, 1000);

		restart = new Button("Restart Game");
		restart.setOnAction(e -> restart());

		int row, column;

		for (row = 0; row < 10; row++) {
			for (column = 0; column < 15; column++) {
				game.add(box[row][column] = new Square(row, column), column, row);
			}
		}

		border.setTop(restart);
		border.setCenter(game);

		stage.setTitle("Temp");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if(connected) connection.leaveServer();

			}

		});
	}

	private void restart() {
		if(connected) connection.restart();
		int row, column;

		for (row = 0; row < 10; row++) {
			for (column = 0; column < 15; column++) {
				box[row][column].setState();
			}
		}
	}

	public class Square extends StackPane {
		private Image xImg;
		private ImageView x;
		private int xloc, yloc;

		// Used to create and update the board
		public Square(int xloc, int yloc) {
			this.xloc = xloc;
			this.yloc = yloc;
			// Background color is white, boarders are black
			this.setStyle("-fx-border-color: black;-fx-background-color: white;");
			this.setPrefSize(100, 100);
			this.setOnMouseClicked(e -> mouseEvent());

			// The X players markers
			xImg = new Image(getClass().getResource("x.jpg").toExternalForm());

			x = new ImageView(xImg);

			x.setFitWidth(66);
			x.setPreserveRatio(true);

			this.getChildren().addAll(x);
			x.setVisible(false);
		}

		// Gets mouse event
		public void mouseEvent() {
			if (!x.isVisible()) {
				x.setVisible(true);
				if(connected) connection.createX(xloc, yloc);
			} else {

			}
		}

		public void setState() {
			x.setVisible(false);
		}

		public int getX() {
			return xloc;
		}

		public int getY() {
			return yloc;
		}
	}
}
