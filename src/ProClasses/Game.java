package openGLTests.main;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Game 
{
	private Player player;
	private TileMap tileMap;
	
	private ArrayList<FanTile> fans;
	private int curFans;
	private int width=1280,height=720;


	public Game()
	{
		fans = new ArrayList<FanTile>();
		tileMap = new TileMap("testMap.txt",32);
		player = new Player(tileMap, 50,-tileMap.getHeight()+20);
	}
	public void getInput()
	{
		player.getInput();
	}

	public void update()
	{
		//System.out.println("updating player");
		player.update();
		//System.out.println("updating tileMap");
		tileMap.update((1280/2)-(int)player.getX(),(720/2)-(int)player.getY());
		//System.out.println("updating fans");
		curFans = tileMap.getFanXCoords().size();
		if(curFans>fans.size())
		{
			fans.add(new FanTile());
		}
	}

	public void render()
	{
		/*try{Thread.sleep(0);}
		catch(Exception e){}*/
		glPushMatrix();
		{
			glClearColor(0,0,0,0);
			glColor4f(255,255,255,255);
			//System.out.println("rendering tileMap");
			tileMap.render();
			//System.out.println("rendering player");
			player.render(tileMap.getX(), tileMap.getY());
			//System.out.println("rendering fans");
			for(int i=0;i<fans.size();i++)
			{
				try{fans.get(i).render(tileMap.getFanXCoords().get(i), tileMap.getFanYCoords().get(i), tileMap.getX(), tileMap.getY());}
				catch(Exception e){}
				
			}
			player.drawHitbox();
		}
		glPopMatrix();
	}

	public float getX(){return player.getX();}
	public float getY(){return player.getY();}
	public float getWidth(){return player.getWidth();}
	public float getHeight(){return player.getHeight();}
}
