package GameBoard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Board extends Application
{
	public Square[][] box = new Square[10][15];
	
	BorderPane border;
	
	Button restart;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		border = new BorderPane();
		GridPane game = new GridPane();
		Scene scene = new Scene(border, 1500, 1000);
		
		restart = new Button("Restart Game");
		restart.setOnAction(e -> restart());
		
		int row, column;
		
		for(row = 0; row < 10; row++)
		{
			for(column = 0; column < 15; column++)
			{
				game.add(box[row][column] = new Square(), column, row);
			}
		}
		
		border.setTop(restart);
		border.setCenter(game);
		
		stage.setTitle("Temp");
		stage.setScene(scene);
		stage.show();
	}
	
	private void restart()
	{
		int row, column;
		
		for(row = 0; row < 10; row++)
		{
			for(column = 0; column < 15; column++)
			{
				box[row][column].setState();
			}
		}
	}
	
	public class Square extends StackPane
	{
		private Image xImg;
		private ImageView x;

		//Used to create and update the board
		public Square()
		{
			//Background color is white, boarders are black
			this.setStyle("-fx-border-color: black;-fx-background-color: white;");
			this.setPrefSize(100, 100);
			this.setOnMouseClicked(e -> mouseEvent());

			//The X players markers
			xImg = new Image(getClass().getResource("x.jpg").toExternalForm());

			x = new ImageView(xImg);

			x.setFitWidth(66);
			x.setPreserveRatio(true);

			this.getChildren().addAll(x);
			x.setVisible(false);
		}
		
		//Gets mouse event
		private void mouseEvent()
		{
			if(!x.isVisible())
			{
				x.setVisible(true);
			}
			else
			{
				
			}
		}
		
		public void setState()
		{
				x.setVisible(false);
		}
	}
}
