package Multiplayer;

import character.BaseCharacter;

import java.awt.*;

public class CharacterHandler {
    private BaseCharacter character;
    private Point point;

    public CharacterHandler(BaseCharacter character, Point point){
        this.character = character;
        this.point = point;

    }

    public BaseCharacter getCharacter(){
        return character;
    }

    public void setCharacter(BaseCharacter character){
        this.character = character;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
