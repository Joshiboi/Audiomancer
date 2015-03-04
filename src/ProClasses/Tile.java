package openGLTests.main;
import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
public class Tile
{
	private double x,y;
	private int width,height;
	private String URL;
	private int ID;
	private boolean saved;
	private boolean removed;
	private Texture texture;
	private boolean overlayable;
	private int backgroundTileID;
	private int maxX;
	private int maxY;
	private boolean collidable;
	private boolean fan;
	
	public Tile(int _x, int _y, String _URL, int backID)
	{
		collidable=true;
		backgroundTileID=backID;
		URL = _URL;
		texture = loadTexture("resources/Textures/Tiles/"+URL);
		ID = Integer.parseInt(_URL.replace("tile","").replace(".png",""));
		if(ID==9 || ID == 10){overlayable=true;}
		if(ID==6 || ID==99 || ID==12 || ID==1 || ID==7){collidable=false;}
		if(ID==6 || ID==7){fan=true;}
		x=_x;
		y=-_y;
		width = 32;
		height = 32;
		if(maxX>1280){maxX=1280;}
		if(maxY>720){maxY=720;}
	}
	public void update(int x, int y)
	{
		this.x-=x;
		this.y-=y;
	}
	public void render()
	{
		glEnable (GL_BLEND);
		glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		texture.bind();
		glBegin(GL_QUADS);
		{
			//lower left
			glTexCoord2f(0,1);
			glVertex2f((float)x,(float)y);

			//top left
			glTexCoord2f(0,0);
			glVertex2f((float)x,(float)y+height);

			//top right
			glTexCoord2f(1,0);
			glVertex2f((float)x+width,(float)y+height);
			
			//lower right
			glTexCoord2f(1,1);
			glVertex2f((float)x+width,(float)y);
		}
		glEnd();
	}
	
	private Texture loadTexture(String s)
	{
		try
		{
			return TextureLoader.getTexture("png", new FileInputStream(s+".png"));
		}
		catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}

		return null;
	}
	
	public double getX() {return x;}
	public double getY() {return y;}	
	public int getWidth() {return width;}	
	public int getHeight() {return height;}
	public String getURL() {return URL;}
	public int getID() {return ID;}
	public boolean isSaved(){return saved;}
	public boolean isRemoved(){return removed;}
	public int getBackgroundTileID(){return backgroundTileID;}
	public boolean isOverlayable(){return overlayable;}
	public boolean isCollidable() {return collidable;}
	public boolean isFan(){return fan;}
	
	public void setFan(boolean i){fan=i;}
	public void setCollidable(boolean collidable) {this.collidable = collidable;}
	public void setX(double x) {this.x = x;}
	public void setY(double y) {this.y = y;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
	public void setURL(String uRL) {URL = uRL;}
	public void setID(int iD) {ID = iD;}
	public void setSaved(boolean i){saved=i;}
	public void setBackgroundTileID(int i){backgroundTileID=i;}
	public void setOverlayable(boolean i){overlayable=i;}
}
