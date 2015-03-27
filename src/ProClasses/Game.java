package openGLTests.main;

import static org.lwjgl.opengl.GL11.*;

public class Game 
{
	private Player player;
	private TileMap tileMap;

	public Game()
	{
		tileMap = new TileMap("testMap.txt",32);
		player = new Player(tileMap, tileMap.getPlayerSpawnX(),tileMap.getPlayerSpawnY());
	}
	public void getInput()
	{
		player.getInput();
	}

	public void update()
	{
		player.update();
		tileMap.update((1280/2)-(int)player.getX(),(720/2)-(int)player.getY());
	}

	public void render()
	{
		glPushMatrix();
		{
			glClearColor(0,0,0,0);
			glColor4f(255,255,255,255);
			tileMap.render();
			player.render(tileMap.getX(), tileMap.getY());
		}
		glPopMatrix();
	}

	public float getX(){return player.getX();}
	public float getY(){return player.getY();}
	public float getWidth(){return player.getWidth();}
	public float getHeight(){return player.getHeight();}
}
