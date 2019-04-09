package Multiplayer;

import character.BaseCharacter;

import java.awt.*;
/**
 * Creates a handler for character objects
 * @author Ben
 *
 */
public class CharacterHandler {
    private BaseCharacter character;
    private Point point;
    private Boolean team;
    /**
     * Creates a character handler
     * @param character The character subclass to be stores
     * @param point the location of the character
     */
    public CharacterHandler(BaseCharacter character, Point point, Boolean Team){
        this.character = character;
        this.point = point;
        setTeam(Team);

    }

    /**
     * Fetches character object
     * @return The character stored at this location
     */
    public BaseCharacter getCharacter(){
        return character;
    }

    /**
     *Sets the character at this location
     * @param character The character to be stored here
     */
    public void setCharacter(BaseCharacter character){
        this.character = character;
    }

    /**
     * Returns the location in a java.awt.point format
     * @return the point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Sets the point
     * @param point the location 
     */
    public void setPoint(Point point) {
        this.point = point;
    }
    
    /**
     * Returns the x location of this object
     * @return X location
     */
    public int getX() {
    	return (int)point.getX();
    }
    
    /**
     * Returns the y location of this object
     * @return Y location
     */
    public int getY() {
    	return (int)point.getY();
    }

	public Boolean getTeam() {
		return team;
	}

	public void setTeam(Boolean team) {
		this.team = team;
	}
}
