package ProClasses;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


import javax.swing.ImageIcon;

public class Audiomancer_shockwave extends JPanel {
	private TileMap tileMap;
	private int x,y;
	private int width, height;
	private int xSpd;
	private boolean colliding;
	
	private boolean topLeft, topRight, botLeft, botRight;
	
    private boolean topLeftLeft, topLeftTop, botLeftLeft, botLeftBot, topRightRight, topRightTop, botRightRight, botRightBot;
	
	public Audiomancer_shockwave(TileMap tm, int _x, int _y) {
		tileMap = tm;
		x = _x;
		y = _y;
	}
	
	public int checkLRCollisions (int tempx, int currCol) {
        int tox = x + xSpd;
        
        calculateCorners(tox, y);


        if (botLeftLeft || topLeftLeft || topRightRight || botRightRight || topLeft || topRight || botLeft || botRight)
        {
        	colliding=true;
        }
        else
        {
        	tempx +=xSpd;
        	colliding=false;
        }
    	return tempx;
    }
	private void calculateCorners(int _x, int _y)
    {
    	topLeftLeft = false;
    	topLeftTop = false;
    	botLeftLeft = false;
    	botLeftBot = false;
    	topRightRight = false;
    	topRightTop = false;
    	botRightRight = false;
    	botRightBot = false;
    	
    	
    	
    	
    	
    	int leftTile = tileMap.getColTile((int) (_x));
        int rightTile = tileMap.getColTile((int) (_x +width)-Math.abs(xSpd));
        int topTile = tileMap.getRowTile((int) (_y));
        int bottomTile = tileMap.getRowTile((int) (_y +height));
        
        topLeft = tileMap.getTile(topTile, leftTile) == 0 || tileMap.getTile(topTile, leftTile) == 2 || tileMap.getTile(topTile, leftTile) == 3 || tileMap.getTile(topTile, leftTile) == 4 || tileMap.getTile(topTile, leftTile) == 5 || tileMap.getTile(topTile, leftTile) == 6;
        topRight = tileMap.getTile(topTile, rightTile) == 0 || tileMap.getTile(topTile, rightTile) == 2 || tileMap.getTile(topTile, rightTile) == 3 || tileMap.getTile(topTile, rightTile) == 4 || tileMap.getTile(topTile, rightTile) == 5 || tileMap.getTile(topTile, rightTile) == 6;
        botLeft = tileMap.getTile(bottomTile, leftTile) == 0 || tileMap.getTile(bottomTile, leftTile) == 2 || tileMap.getTile(bottomTile, leftTile) == 3 || tileMap.getTile(bottomTile, leftTile) == 4 || tileMap.getTile(bottomTile, leftTile) == 5 || tileMap.getTile(bottomTile, leftTile) == 6;
        botRight = tileMap.getTile(bottomTile, rightTile) == 0 || tileMap.getTile(bottomTile, rightTile) == 2 || tileMap.getTile(bottomTile, rightTile) == 3 || tileMap.getTile(bottomTile, rightTile) == 4 || tileMap.getTile(bottomTile, rightTile) == 5 || tileMap.getTile(bottomTile, rightTile) == 6;
        
        
        
        
        
        if (topLeft && (leftTile * tileMap.getTileSize() < tileMap.getColTile(x) * tileMap.getTileSize())) {
        	topLeftLeft = true;
        } else if (topLeft){
        	topLeftTop = true;
        }
        
        if (botLeft && (leftTile * tileMap.getTileSize() < tileMap.getColTile(x) * tileMap.getTileSize())) {
        	botLeftLeft = true;
        } else if (botLeft){
        	botLeftBot = true;
        }
        
        if (topRight && (rightTile * tileMap.getTileSize() > tileMap.getColTile(x) * tileMap.getTileSize())) {
        	topRightRight = true;
        } else if (topRight){
        	topRightTop = true;
        }
        
        if (botRight && (rightTile * tileMap.getTileSize() > tileMap.getColTile(x) * tileMap.getTileSize())) {
        	botRightRight = true;
        } else if (botRight){
        	botRightBot = true;
        }
        
        
    }	
	public void draw(Graphics g, int current)
    {
    	//int animID=boltID;
		int animFrames=8; 
        
        Image[] shockwave = new Image[animFrames];
        ImageIcon[] shockwaveImages = new ImageIcon[animFrames];
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            shockwaveImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Slam Effect/shockwave_"+(i+1)+".png"));
            shockwave[i] = shockwaveImages[i].getImage();
        }
        width = shockwave[animFrames-1].getWidth(null);
        height = shockwave[animFrames-1].getHeight(null);
        
        g2d.drawImage(shockwave[current],x+tileMap.getX(),y+tileMap.getY(),this);
    }
	
	public boolean isColliding(){return colliding;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getX(){return x;}
	public int getY(){return y;}

}
