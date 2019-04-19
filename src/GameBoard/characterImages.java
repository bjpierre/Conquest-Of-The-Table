package GameBoard;

import GameBoard.Board;
import GameBoard.Board.Square;
import character.BaseCharacter;
import character.Cleric;
import character.Fighter;
import character.Rogue;
import character.Wizard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class characterImages{

	public characterImages(Square s, int row, int column, BaseCharacter[][] characters) {

		if ((column == 1 && row == 1) || (column == 13 && row == 1)) {
			setKnight(s, "red", row, column, characters);
		}
		if ((column == 1 && row == 8) || (column == 13 && row == 8)) {
			setKnight(s, "blue", row, column, characters);
		}
		if (column == 7 && row == 1) {
			setWizard(s, "red", row, column, characters);
		}
		if (column == 7 && row == 8) {
			setWizard(s, "blue", row, column, characters);
		}
		if (column == 4 && row == 1) {
			setCleric(s, "red", row, column, characters);
		}
		if (column == 4 && row == 8) {
			setCleric(s, "blue", row, column, characters);
		}
		if (column == 10 && row == 1) {
			setRogue(s, "red", row, column, characters);
		}
		if (column == 10 && row == 8) {
			setRogue(s, "blue", row, column, characters);
		}
	}

	public void setKnight(Square s, String color, int row, int column, BaseCharacter[][] characters)
	{
		if(color.compareTo("red") == 0)
		{
			Image knightRedImg = new Image(getClass().getResource("pixelKnightRed.png").toExternalForm());
			ImageView knightRed = new ImageView();
			knightRed.setImage(knightRedImg);
			knightRed.setFitWidth(75);
			knightRed.setPreserveRatio(true);
			s.getChildren().add(knightRed);
			s.c = new Fighter(row,column,false);
			characters[0][0] = s.c;
		}
		else if(color.compareTo("blue")==0)
		{
			Image knightBlueImg = new Image(getClass().getResource("pixelKnightBlue.png").toExternalForm());
			ImageView knightBlue = new ImageView();
			knightBlue.setImage(knightBlueImg);
			knightBlue.setFitWidth(75);
			knightBlue.setPreserveRatio(true);
			s.getChildren().add(knightBlue);
			s.c = new Fighter(row,column,true);
			characters[1][0] = s.c;
		}
	}
	
	public void setWizard(Square s, String color, int row, int column, BaseCharacter[][] characters)
	{
		if(color.compareTo("red") == 0)
		{
			Image wizardRedImg = new Image(getClass().getResource("pixelWizardRed.png").toExternalForm());
			ImageView wizardRed = new ImageView();
			wizardRed.setImage(wizardRedImg);
			wizardRed.setFitWidth(75);
			wizardRed.setPreserveRatio(true);
			s.getChildren().add(wizardRed);
			s.c = new Wizard(row,column,false);
			characters[0][1] = s.c;
		}
		else if(color.compareTo("blue")==0)
		{
			Image wizardBlueImg = new Image(getClass().getResource("pixelWizardBlue.png").toExternalForm());
			ImageView wizardBlue = new ImageView();
			wizardBlue.setImage(wizardBlueImg);
			wizardBlue.setFitWidth(75);
			wizardBlue.setPreserveRatio(true);
			s.getChildren().add(wizardBlue);
			s.c = new Wizard(row,column,true);
			characters[1][1] = s.c;
		}
	}
	
	public void setCleric(Square s, String color, int row, int column, BaseCharacter[][] characters)
	{
		if(color.compareTo("red") == 0)
		{
			Image clericRedImg = new Image(getClass().getResource("pixelClericRed.png").toExternalForm());
			ImageView clericRed = new ImageView();
			clericRed.setImage(clericRedImg);
			clericRed.setFitWidth(75);
			clericRed.setPreserveRatio(true);
			s.getChildren().add(clericRed);
			s.c = new Cleric(row,column,false);
			characters[0][2] = s.c;
		}
		else if(color.compareTo("blue")==0)
		{
			Image clericBlueImg = new Image(getClass().getResource("pixelClericBlue.png").toExternalForm());
			ImageView clericBlue = new ImageView();
			clericBlue.setImage(clericBlueImg);
			clericBlue.setFitWidth(75);
			clericBlue.setPreserveRatio(true);
			s.getChildren().add(clericBlue);
			s.c = new Cleric(row,column,true);
			characters[1][2] = s.c;
		}
	}
	
	public void setRogue(Square s, String color, int row, int column, BaseCharacter[][] characters)
	{
		if(color.compareTo("red") == 0)
		{
			Image rogueRedImg = new Image(getClass().getResource("pixelRogueRed.png").toExternalForm());
			ImageView rogueRed = new ImageView();
			rogueRed.setImage(rogueRedImg);
			rogueRed.setFitWidth(75);
			rogueRed.setPreserveRatio(true);
			s.getChildren().add(rogueRed);
			s.c = new Rogue(row,column,false);
			characters[0][3] = s.c;
		}
		else if(color.compareTo("blue")==0)
		{
			Image rogueBlueImg = new Image(getClass().getResource("pixelRogueBlue.png").toExternalForm());
			ImageView rogueBlue = new ImageView();
			rogueBlue.setImage(rogueBlueImg);
			rogueBlue.setFitWidth(75);
			rogueBlue.setPreserveRatio(true);
			s.getChildren().add(rogueBlue);
			s.c = new Rogue(row,column,true);
			characters[1][3] = s.c;
		}
	}
	
}
