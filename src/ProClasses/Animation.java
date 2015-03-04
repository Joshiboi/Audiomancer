package openGLTests.main;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
import java.io.*;


public class Animation 
{
	private Texture[] frames;
	private int delay;
	private int waitTime;
	private int waitFrame;
	private int size;
	private int current;
	private long startTime;
	private boolean wait;
	private int frameCount;
	private float width,height;

	public Animation(int size, String pathToTexture, int frames, int waitFrame, int millis, int waitMillis)
	{
		this.frames = new Texture[frames];
		for(int i=0;i<frames;i++)
		{
			this.frames[i] = loadTexture(pathToTexture+(i+1));
		}
		delay=millis;
		waitTime = waitMillis;
		this.waitFrame = waitFrame;
		this.size=size;
		frameCount=frames;
		startTime=System.nanoTime();
	}

	public void render(float x, float y, boolean flipped, boolean loop)
	{
		glEnable (GL_BLEND);
		glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		long passedTime;
		width=x+size;
		height=y+size;

		passedTime = System.nanoTime() - startTime;

		if(passedTime/1000000 >= delay)
		{
			if(current==waitFrame){wait=true;}

			if(wait && passedTime/1000000 < waitTime){}

			else
			{
				current++;
				wait=false;
				passedTime=0;
				startTime=System.nanoTime();
			}
		}

		if(current>=frameCount && loop)
		{
			current=0;
		}
		else if(current>=frameCount && loop)
		{
			current=frameCount-1;
		}
		
		frames[current].bind();
		if(flipped)
		{
			glBegin(GL_QUADS);
			{
				//lower left
				glTexCoord2f(0,1);
				glVertex2f(x,y);
	
				//top left
				glTexCoord2f(0,0);
				glVertex2f(x,height);
	
				//top right
				glTexCoord2f(1,0);
				glVertex2f(width,height);
				
				//lower right
				glTexCoord2f(1,1);
				glVertex2f(width,y);
			}
			glEnd();
		}
		
		else
		{
			glBegin(GL_QUADS);
			{
				//lower left
				glTexCoord2f(1,1);
				glVertex2f(x,y);
	
				//top left
				glTexCoord2f(1,0);
				glVertex2f(x,height);
	
				//top right
				glTexCoord2f(0,0);
				glVertex2f(width,height);
				
				//lower right
				glTexCoord2f(0,1);
				glVertex2f(width,y);
			}
			glEnd();
		}
	}
	
	public void setCurrent(int i)
	{
		current=i;
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
}
