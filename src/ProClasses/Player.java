package openGLTests.main;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.*;
import static org.lwjgl.opengl.GL11.*;

public class Player 
{
	private Animation walk;
	private Animation stand;
	private Animation jump;
	private Animation fall;
	private Animation shoot;

	private TileMap tileMap;

	private ArrayList<Integer> collidableTileX;
	private ArrayList<Integer> collidableTileY;
	private ArrayList<Integer> fanTileX;
	private ArrayList<Integer> fanTileY;
	private ArrayList<Integer> fanIDs;

	private int x,y,width,height;
	private int xSpd,ySpd,xVel;
	private int jumpSpeed;
	private int gravity;
	private int size=32;

	private boolean left,right;
	private boolean walking,jumping;
	private boolean falling, shooting, attacking, inAir;
	private boolean colV, colH;
	private int characterHitboxDiffs=8;
	private int prevX, prevY;
	private boolean skipFall=false;
	private boolean foundCorner = false;
	private double fanDist;
	private double fanV = 10;
	private double playerYPos;
	private boolean moved=false;
	

	public Player(TileMap tm, int x, int y)
	{
		//System.out.println("spawning at: ("+x+","+y+")");
		this.x=x;
		this.y=y;
		tileMap = tm;

		width=size;
		height=size;

		ySpd=-gravity;
		xVel=2;
		jumpSpeed=8;
		gravity = 1;
		right=true;

		initAnims();
	}

	public void update()
	{
	    //System.out.println(ySpd);
		//check collisions and move
		checkFanCollisions();
		checkCollisions();
		prevX = x;
		prevY = y;
		if(colH && !colV){y+=ySpd;}
		else if(colV && !colH){x+=xSpd; if(ySpd<0){jumping=false; ySpd++;}}
		else if(colV && colH)
		{
			if(ySpd<0){jumping=false;}
			ySpd=0;
		}
		else
		{
			x+=xSpd;
			y+=ySpd;
		}
		if(ySpd>=-(jumpSpeed*2) && !skipFall){skipFall=true; ySpd-=gravity;}
		else if(skipFall){skipFall=false;}
		if(ySpd<-1 && !inAir && !falling)
		{
			fall.setCurrent(0); 
			falling=true;
		}
		if(ySpd>-1){falling=false;}
	}

	public void render(int tmx, int tmy)
	{
		//render the correct animation for the player state in the correct orientation
		//true = mirrored images 
		//false = unchanged images
		if(left && !jumping && walking && !inAir){walk.render(x+tmx,y+tmy,true,true);}
		else if(right && !jumping && walking && !inAir){walk.render(x+tmx,y+tmy,false,true);}

		else if(jumping && right){jump.render(x+tmx, y+tmy, true,true);}
		else if(jumping && left){jump.render(x+tmx, y+tmy, false,true);}

		else if(falling && right){fall.render(x+tmx, y+tmy, true,false);}
		else if(falling && left){fall.render(x+tmx, y+tmy, false,false);}
		
		else if(inAir && right){jump.render(x+tmx, y+tmy, true,true);}
		else if(inAir && left){jump.render(x+tmx, y+tmy, false,true);}
		
		else
		{
			if(left){stand.render(x+tmx,y+tmy,false,true);}
			else if(right){stand.render(x+tmx,y+tmy,true,true);}
		}
	}

	public boolean isColliding(int x1, int y1, int w, int h, int x2, int y2, int w2, int h2)
	{
		Rectangle r1 = new Rectangle(x1,y1,w,h);
		Rectangle r2 = new Rectangle(x2,y2,w2,h2);
		return r1.intersects(r2);
	}
	public void checkCollisions()
	{
		colV=false;
		colH=false;
		collidableTileX = tileMap.getCollidableXCoords();
		collidableTileY = tileMap.getCollidableYCoords();
		int[] indeces = new int[collidableTileX.size()];
		boolean collision=false;
		int index=0;
		int tempX=(int)((1280/2)+xSpd)+characterHitboxDiffs;
		int tempY=(int)((720/2)+ySpd);
		for(int i=0;i<collidableTileX.size();i++)
		{
			int tileY = collidableTileY.get(i);
			int tileX = collidableTileX.get(i);
			if(isColliding(tempX,tempY,(int)width,(int)height,tileX, tileY, 32,32))
			{
				indeces[index]=i;
				index++;
				collision=true;
			}
		}
		if(collision)
		{
			for(int i=0;i<index;i++)
			{
				if(isColliding(tempX,tempY-ySpd, width-(characterHitboxDiffs*2), height, collidableTileX.get(indeces[i]),  collidableTileY.get(indeces[i]), 32,32)){colH=true;}
				if(isColliding(tempX-xSpd, tempY, width-(characterHitboxDiffs*2), height, collidableTileX.get(indeces[i]),  collidableTileY.get(indeces[i]), 32,32)){colV=true;}
			}
			if(colV && colH)
			{
				if(prevX == x && !foundCorner){}
				else if((int)(Math.abs(xSpd))>1 && (int)(Math.abs(ySpd))>1)
				{
					System.out.println("corner collision");
					foundCorner=true;
					if(Math.abs(xSpd)>Math.abs(ySpd))
					{
						y-=ySpd;
						x-=xSpd;
						colH=false;
					}
					else if(Math.abs(ySpd)>Math.abs(xSpd))
					{
						x-=xSpd;
						y-=ySpd;
						colV=false;
					}
				}
			}
			else
			{
				foundCorner=false;
			}
		}
	}
	public void checkFanCollisions()
	{
		int index=0;
		fanIDs = tileMap.getFanIDs();
		fanTileX = tileMap.getFanXCoords();
		fanTileY = tileMap.getFanYCoords();
		for(int i=0;i<fanTileX.size();i++)
		{
			fanDist = tileMap.getTile(fanIDs.get(i)).getFanHeight();
			if(640+20>fanTileX.get(i) && 640+8<fanTileX.get(i)+32)
			{
				if(360+8>fanTileY.get(i) && 360-8<fanTileY.get(i)+(fanDist*32)+24)
				{
					fanV = tileMap.getTile(fanIDs.get(i)).getVelocity();
					index = i;
					if(ySpd <=2){ySpd+=(int)(Math.round(fanV*( ((fanDist*32 - playerYPos)  / (fanDist*32)) ) ));} //bounce at top of fan
					else{ySpd = (int)(Math.round(fanV*( ((fanDist*32 - playerYPos)  / (fanDist*32)) ) ))+2;} // move up through fan
					System.out.println(index);
					inAir=true;
					if(index>0){break;}
				}
				
			}
			else if(inAir && ySpd==0){inAir=false;}
			playerYPos=Math.abs(360-fanTileY.get(index)-32);
		}
	}
	public void getInput()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			right=true;
			left=false;
			walking=true;
			xSpd=xVel;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			left=true;
			right=false;
			walking=true;
			xSpd=-xVel;
		}
		else{xSpd=0; walking=false;}

		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !jumping && !inAir)
		{
			ySpd=jumpSpeed;
			jumping=true;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			x=tileMap.getPlayerSpawnX();
			y=tileMap.getPlayerSpawnY();
		}
	}

	public void drawHitbox()
	{
		glClearColor(0,0,0,0);
		glColor4f(255,0,0,255);
		int tempX=(int)(((1280/2))+xSpd)+characterHitboxDiffs;
		int tempY=(int)(((720/2))+ySpd);
		int drawX=tempX;
		int drawY=tempY;
		int width=32-(characterHitboxDiffs)*2;
		int height=32;
		glBegin(GL_QUADS);
		{
			//lower left
			glVertex2f(drawX,drawY+height);

			//top left
			glVertex2f(drawX,drawY);

			//top right
			glVertex2f(drawX+width,drawY);

			//lower right
			glVertex2f(drawX+width,drawY+height);
		}
		glEnd();
	}

	private void initAnims()
	{
		stand= new Animation(32,"Resources/Textures/Players/Audiomancer/Stand/audiomancer_stand_right_", 2,1,1000,3000);
		walk= new Animation(32,"Resources/Textures/Players/Audiomancer/Walk/am_walk_", 5,-1,83,0);
		jump= new Animation(32,"Resources/Textures/Players/Audiomancer/Jump/audiomancer_jump_right_", 1,-1,83,0);
		fall= new Animation(32,"Resources/Textures/Players/Audiomancer/Jump_Fall/audiomancer_jumpfall_right_", 7,1,125,2000/3);
		shoot= new Animation(32,"Resources/Textures/Players/Audiomancer/Spell/audiomancer_spell_right_", 12,-1,320,0);
	}


	public float getX(){return x;}
	public float getY(){return y;}
	public float getWidth(){return width;}
	public float getHeight(){return height;}
}
