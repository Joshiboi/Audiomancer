package ProClasses;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
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

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Psychomancer  extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 8184458597936033041L;
	private TileMap tileMap;
	private int animationCount;
	private int[] x,y;
	private int playerID=0;
	private int[] width, height;

	private Image[][] standingImages;
	private ImageIcon[][] standingImageIcons;
	private Image[][] walkingImages;
	private ImageIcon[][] walkingImageIcons;
	private Image jumpingImages[];
	private ImageIcon jumpingImageIcons[];
	private Image[][] fallingImages;
	private ImageIcon[][] fallingImageIcons;
	private Image[][] shootingImages;
	private ImageIcon[][] shootingImageIcons;

	private boolean left=true;
	private boolean right=false;

	private boolean standing=true;
	private boolean walk=false;

	private boolean shoot=false;
	private boolean shooting;
	private boolean walking;
	private boolean jump;

	private long[] prevTimes;
	private int[] current;
	private boolean[] wait;

	private boolean canFall;
	private boolean shootBolt=false;
	private boolean falling=false;
	private boolean jumping=false;
	private boolean inAir=false;
	private boolean attacking=false;
	private boolean landed=false;
	private boolean Apressed=false;
	private boolean Dpressed=false;
	private boolean[] colliding;
	private boolean floatCharacter=false;
	private boolean slow=false;
	private boolean canFloat=true;
	private boolean standingInDoor=false;
	private boolean bobbing;
	private boolean bobUp=false;
	private boolean bobDown=true;

	private double bobbingSpd=0;
	private double floatSpeed;
	private double defaultFloatSpeed=2;
	private double jumpSpeed;
	private int[] xSpd;
	private double[] ySpd;
	private int defaultXSpd=2;
	private int defaultYSpd=1;
	private int defaultJumpSpeed=5;
	private int characterHitboxDiffs = 8;
	private boolean loaded=false;
	private double gravity;

	private boolean topLeft, topRight, botLeft, botRight;
	private boolean topLeftLeft, botLeftLeft, topRightRight, botRightRight;
	private boolean topLeftF, topRightF, botLeftF, botRightF;
	private boolean topLeftD, topRightD, botLeftD, botRightD;
	private boolean topLeftB, topRightB, botLeftB, botRightB;

	public Psychomancer(TileMap tm)
	{
		tileMap = tm;
		animationCount=10;
		ySpd = new double[animationCount];
		xSpd = new int[animationCount];
		x = new int[animationCount];
		y = new int[animationCount];
		width = new int[animationCount];
		height = new int[animationCount];
		colliding = new boolean[animationCount];
		prevTimes = new long[animationCount];
		wait = new boolean[animationCount];
		current = new int[animationCount];
		jumpSpeed = defaultJumpSpeed;
		floatSpeed = 0;
		gravity = 0.3;


	}

	public void load()
	{
		Thread loadThread = new Thread()
		{
			public void run()
			{
				int walkingFrames=8;
				int standingFrames=2;
				int shootingFrames=13;
				int fallingFrames=6;

				for(int i=0;i<animationCount;i++)
				{
					current[i]=0;
					colliding[i]=false;
					x[i]=33;
					y[i]=tileMap.getHeight();
					xSpd[i]=defaultXSpd;
					ySpd[i] = defaultYSpd;
				}

				standingImages = new Image[2][standingFrames];
				standingImageIcons = new ImageIcon[2][standingFrames];

				walkingImages = new Image[2][walkingFrames];
				walkingImageIcons = new ImageIcon[2][walkingFrames];

				jumpingImages = new Image[2];
				jumpingImageIcons = new ImageIcon[2];

				fallingImages = new Image[2][fallingFrames];
				fallingImageIcons = new ImageIcon[2][fallingFrames];

				shootingImages = new Image[2][shootingFrames];
				shootingImageIcons = new ImageIcon[2][shootingFrames];
				for(int i=0;i<2;i++)
				{
					if(i==0)
					{
						jumpingImageIcons[i] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Jumping/Jump_Left/psychomancer_jump.png"));
					}
					else
					{
						jumpingImageIcons[i] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Jumping/Jump_Right/psychomancer_jump_right.png"));
					}

					jumpingImages[i] = jumpingImageIcons[i].getImage();

					for(int j=0;j<standingFrames;j++)
					{
						if(i==0)
						{
							standingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Standing/Still_Left/psychomancer_still_"+(j+1)+".png"));
							System.out.println("loaded standing image "+j+" at "+this.getClass().getResource("/resources/textures/psychomancer/Standing/Still_Left/psychomancer_still_"+(j+1)+".png"));
						}
						else
						{
							standingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Standing/Still_Right/psychomancer_still_right_"+(j+1)+".png"));
						}
						standingImages[i][j] = standingImageIcons[i][j].getImage();
					}

					for(int j=0;j<walkingFrames;j++)
					{
						if(i==0)
						{
							walkingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Walking/Walk_Left/psychomancer_walk_"+(j+1)+".png"));
						}
						else
						{
							walkingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Walking/Walk_Right/psychomancer_walk_right_"+(j+1)+".png"));
						}
						walkingImages[i][j] = walkingImageIcons[i][j].getImage();
					}

					for(int j=0;j<fallingFrames;j++)
					{
						if(i==0)
						{
							fallingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Jumping/Jumpfalltransit_Left/psychomancer_jumpfalltransit_"+j+".png"));
						}
						else
						{
							fallingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Jumping/Jumpfalltransit_Right/psychomancer_jumpfalltransit_right_"+j+".png"));
						}
						fallingImages[i][j] = fallingImageIcons[i][j].getImage();
					}

					for(int j=0;j<shootingFrames;j++)
					{
						if(i==0)
						{
							shootingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Rainbolt/Rainbolt_Left/psychomancer_spell_"+(j+1)+".png"));
						}
						else
						{
							shootingImageIcons[i][j] = new ImageIcon(this.getClass().getResource("/resources/textures/psychomancer/Rainbolt/Rainbolt_Right/psychomancer_spell_right_"+(j+1)+".png"));
						}
						shootingImages[i][j] = shootingImageIcons[i][j].getImage();
					}
				}
				loaded=true;
				return;
			}
		};
		try{loadThread.start();}
		catch(Exception e){}
	}

	public void keyPressed(KeyEvent e)
	{
		int _e = e.getKeyCode();

		if(KeyEvent.VK_E == _e)
		{
			x[playerID]=tileMap.getPlayerSpawnX();
			y[playerID]=tileMap.getPlayerSpawnY();
		}
		if(KeyEvent.VK_SHIFT == _e)
		{
			slow=true;
			//xSpd[playerID]=10;
		}

		if(KeyEvent.VK_D == _e)
		{
			Dpressed=true;
			if(!attacking)
			{
				walking=true;
				right=true;
				left=false;
			}
		}
		if(KeyEvent.VK_A == _e)
		{
			Apressed=true;
			if(!attacking)
			{
				walking=true;
				left=true;
				right=false;
			}

		}
		if(KeyEvent.VK_RIGHT == _e)
		{
			if(!inAir)
			{
				attacking=true;
				shooting=true;
				right=true;
				left=false;
			}

		}
		if(KeyEvent.VK_LEFT == _e)
		{
			if(!inAir)
			{
				attacking=true;
				shooting=true;
				left=true;
				right=false;
			}

		}
		if(KeyEvent.VK_SPACE == _e)
		{
			if(!inAir)
			{
				jump=true;
				landed=false;
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{
		int _e = e.getKeyCode();
		if(KeyEvent.VK_SHIFT == _e)
		{
			slow=false;
			//xSpd[playerID]=defaultXSpd;
		}
		if(KeyEvent.VK_D == _e)
		{
			Dpressed=false;
			if(!Apressed)
			{
				walking=false;
			}

		}
		if(KeyEvent.VK_A == _e)
		{
			Apressed=false;
			if(!Dpressed)
			{
				walking=false;
			}
		}
	}
	public void keyTyped(KeyEvent e){}
	public int checkLRCollisions (int animID, int tempx, int currCol) {
		int toxL = x[animID] - xSpd[animID];
		int toxR = x[animID] + xSpd[animID];
		if(left) 
		{
			calculateCorners(toxL, y[animID], animID);

			if (botLeftLeft || topLeftLeft)
			{
				tempx = ( ((currCol+1) * tileMap.getTileSize()))-characterHitboxDiffs;
				colliding[animID]=true;
			}

			else
			{
				tempx -=xSpd[animID];
				colliding[animID]=false;
			}
		}
		if (right) {
			calculateCorners(toxR, y[animID], animID);
			if (topRightRight || botRightRight)
			{
				tempx = ( ((currCol) * tileMap.getTileSize()))+characterHitboxDiffs;
				colliding[animID]=true;
			}
			else
			{
				tempx +=xSpd[animID];
				colliding[animID]=false;
			}
		}

		return tempx;
	}

	private void calculateCorners(int _x, int _y, int animID)
	{
		topLeftLeft = false;
		botLeftLeft = false;
		topRightRight = false;
		botRightRight = false;

		int leftTile = tileMap.getColTile((int) (_x)+characterHitboxDiffs+1);
		int rightTile = tileMap.getColTile((int) (_x +width[animID])-xSpd[animID]-characterHitboxDiffs+1);
		int topTile = tileMap.getRowTile((int) (_y));
		int bottomTile = tileMap.getRowTile((int) (_y +height[animID])-(int)(ySpd[animID]+0.75));

		topLeft = tileMap.getTile(topTile, leftTile) == 0 || tileMap.getTile(topTile, leftTile) == 2 || tileMap.getTile(topTile, leftTile) == 3 || tileMap.getTile(topTile, leftTile) == 4 || tileMap.getTile(topTile, leftTile) == 5;
		topRight = tileMap.getTile(topTile, rightTile) == 0 || tileMap.getTile(topTile, rightTile) == 2 || tileMap.getTile(topTile, rightTile) == 3 || tileMap.getTile(topTile, rightTile) == 4 || tileMap.getTile(topTile, rightTile) == 5;
		botLeft = tileMap.getTile(bottomTile, leftTile) == 0 || tileMap.getTile(bottomTile, leftTile) == 2 || tileMap.getTile(bottomTile, leftTile) == 3 || tileMap.getTile(bottomTile, leftTile) == 4 || tileMap.getTile(bottomTile, leftTile) == 5;
		botRight = tileMap.getTile(bottomTile, rightTile) == 0 || tileMap.getTile(bottomTile, rightTile) == 2 || tileMap.getTile(bottomTile, rightTile) == 3 || tileMap.getTile(bottomTile, rightTile) == 4 || tileMap.getTile(bottomTile, rightTile) == 5;





		if (topLeft && ( (leftTile * tileMap.getTileSize()) - (characterHitboxDiffs+1) < (tileMap.getColTile(x[animID]) * tileMap.getTileSize()) )) {
			topLeftLeft = true;
		} 


		if (botLeft && ( (leftTile * tileMap.getTileSize()) - (characterHitboxDiffs+1) < (tileMap.getColTile(x[animID]) * tileMap.getTileSize()) )) {
			botLeftLeft = true;
		} 


		if (topRight && ((rightTile * tileMap.getTileSize()) + (characterHitboxDiffs+1) > tileMap.getColTile(x[animID]) * tileMap.getTileSize())) {
			topRightRight = true;
		}

		if (botRight && ((rightTile * tileMap.getTileSize()) + (characterHitboxDiffs+1) > tileMap.getColTile(x[animID]) * tileMap.getTileSize())) {
			botRightRight = true;
		}


	}
	public void getFloatTile(int _x, int _y, int animID)
	{
		int leftTile = tileMap.getColTile((int) (_x+(width[animID]/2)));
		int rightTile = tileMap.getColTile((int) (_x+(width[animID]/2)));
		int topTile = tileMap.getRowTile((int) (_y));
		int bottomTile = tileMap.getRowTile((int) (_y +height[animID]));

		topLeftF = tileMap.getTile(topTile, leftTile) == 7 || tileMap.getTile(topTile, leftTile) == 6;
		topRightF = tileMap.getTile(topTile, rightTile) == 7 || tileMap.getTile(topTile, rightTile) == 6;
		botLeftF = tileMap.getTile(bottomTile, leftTile) == 7 || tileMap.getTile(bottomTile, leftTile) == 6;
		botRightF = tileMap.getTile(bottomTile, rightTile) == 7 || tileMap.getTile(bottomTile, rightTile) == 6;

	}
	public void getDoorTile(int _x, int _y, int animID)
	{
		int leftTile = tileMap.getColTile((int) (_x)+characterHitboxDiffs+1);
		int rightTile = tileMap.getColTile((int) (_x+9));
		int topTile = tileMap.getRowTile((int) (_y));
		int bottomTile = tileMap.getRowTile((int) (_y +height[animID]));

		topLeftD = tileMap.getTile(topTile, leftTile) == 8;
		topRightD = tileMap.getTile(topTile, rightTile) == 8;
		botLeftD = tileMap.getTile(bottomTile, leftTile) == 8;
		botRightD = tileMap.getTile(bottomTile, rightTile) == 8;
	}

	public void getBobbingTile(int _x, int _y, int animID)
	{
		int leftTile = tileMap.getColTile((int) (_x+(width[animID]/2)));
		int rightTile = tileMap.getColTile((int) (_x+(width[animID]/2)));
		int topTile = tileMap.getRowTile((int) (_y+(height[animID]/2)-2));
		int bottomTile = tileMap.getRowTile((int) (_y+(height[animID]/2)-2));

		topLeftB = tileMap.getTile(topTile, leftTile) == 9;
		topRightB = tileMap.getTile(topTile, rightTile) == 9;
		botLeftB = tileMap.getTile(bottomTile, leftTile) == 9;
		botRightB = tileMap.getTile(bottomTile, rightTile) == 9;

	}

	public void update()
	{
		if(loaded)
		{
			getDoorTile(x[playerID], y[playerID], playerID);
			if(topLeftD || topRightD || botLeftD || botRightD)
			{
				tileMap.setDoorState(true);
			}
			getFloatTile(x[playerID], y[playerID], playerID);
			if(topLeftF || topRightF || botLeftF || botRightF)
			{
				if(!bobbing)
				{
					floatCharacter=true;
				}
			}

			if(!inAir && walking && !attacking){walk=true;}
			else{walk=false;}

			if(!inAir && attacking)
			{
				if(shooting){shoot=true;}
			}
			if(!inAir && jump && !attacking)
			{
				jumping=true;
				inAir=true;
			}

			if (floatCharacter)
			{
				calcFloating();
			}
			else if(walk)
			{
				calcWalking();
			}
			else if(shoot)
			{
				calcShooting();
			}
			else if(falling)
			{
				calcFalling();
			}
			else if(jumping)
			{
				calcJumping();
			}
			else{calcStanding();}
		}
	}

	public void paint(Graphics g)
	{
		tileMap.setX(740 - x[playerID]);
		tileMap.setY(360 - y[playerID]);
		if (floatCharacter)
		{
			floating(g);
		}
		else if(walk)
		{
			walk(g);
		}
		else if(shoot)
		{
			character_shoot(g);
		}
		else if(falling)
		{
			falling(g);
		}
		else if(jumping)
		{
			jump(g);
		}
		else{stand(g);}
	}

	public void stand(Graphics g)
	{
		int xyID=playerID;
		int animID=1;
		Graphics2D g2d = (Graphics2D) g;

		if(left){g2d.drawImage(standingImages[0][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		if(right){g2d.drawImage(standingImages[1][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
	}

	public void calcStanding()
	{
		int xyID=playerID;
		int animID=1;
		int animFrames=2;
		int toyD = y[xyID] + (int)ySpd[xyID];
		long passedTime;

		width[animID] = standingImages[0][animFrames-1].getWidth(null);
		height[animID] = standingImages[0][animFrames-1].getHeight(null);

		passedTime = System.nanoTime() - prevTimes[animID];

		if(passedTime/1000000000 >= 1)
		{
			if(current[1]==1){wait[animID]=true;}

			if(wait[animID] && passedTime/1000000000 <3){}

			else
			{
				current[animID]++;
				wait[animID]=false;
				passedTime=0;
				prevTimes[animID]=System.nanoTime();
			}
		}

		if(current[animID]>=animFrames)
		{
			current[animID]=0;
		}
		calculateCorners(x[xyID], toyD, xyID);
		if(botRight || botLeft){}
		else
		{
			falling=true;
		}
	}

	public void walk(Graphics g)
	{
		int xyID=playerID;
		int animID=2;
		Graphics2D g2d = (Graphics2D) g;

		if(left){g2d.drawImage(walkingImages[0][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		if(right){g2d.drawImage(walkingImages[1][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
	}

	public void calcWalking()
	{
		if(Dpressed && !Apressed){right=true; left=false;}
		if(Apressed && !Dpressed){left=true; right=false;}

		int xyID=playerID;
		int animID=2;
		int animFrames=8;
		int toyD = y[xyID] + (int)ySpd[xyID];
		int currCol = tileMap.getColTile(x[xyID]);
		int tempx = x[xyID];
		long passedTime;

		width[xyID] = walkingImages[0][animFrames-1].getWidth(null);
		height[xyID] = walkingImages[0][animFrames-1].getHeight(null);

		passedTime = System.nanoTime() - prevTimes[animID];

		if(passedTime/83333333 >= 1)
		{
			current[animID]++;
			wait[animID]=false;
			passedTime=0;
			prevTimes[animID]=System.nanoTime();
		}
		if(current[animID]>=animFrames)
		{
			current[animID]=0;
		}
		calculateCorners(x[xyID]+xSpd[xyID], toyD, xyID);
		if(!botLeft)
		{
			calculateCorners(x[xyID]-xSpd[xyID], toyD, xyID);
			if(!botRight)
			{
				falling=true;
				inAir=true;
			}
		}

		tempx = checkLRCollisions(xyID, tempx, currCol);
		x[xyID]=tempx;
	}

	public void jump(Graphics g)
	{
		int xyID=playerID;
		int animID=3;
		Graphics2D g2d = (Graphics2D) g;

		if(left){g2d.drawImage(jumpingImages[0],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		if(right){g2d.drawImage(jumpingImages[1],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}  
	}

	public void calcJumping()
	{
		int xyID=playerID;
		int animID=3;
		int animFrames=1;

		int currRow = tileMap.getRowTile(y[xyID]);
		int currCol = tileMap.getColTile(x[xyID]);
		int tempx = x[xyID];
		int tempy = y[xyID];
		int toyU = y[xyID] - defaultJumpSpeed;
		long passedTime;
		if(Dpressed && !Apressed){right=true; left=false;}
		if(Apressed && !Dpressed){left=true; right=false;}

		width[xyID] = jumpingImages[animFrames-1].getWidth(null);
		height[xyID] = jumpingImages[animFrames-1].getHeight(null);

		passedTime = System.nanoTime() - prevTimes[animID];

		if(passedTime/125000000 >= 1)
		{
			current[animID]++;
			wait[animID]=false;
			passedTime=0;
			prevTimes[animID]=System.nanoTime();
		}

		if(current[animID]>=animFrames)
		{
			current[animID]=animFrames-1;
		}

		jumpSpeed-=0.2;
		if(jumpSpeed<=0)
		{
			jump=false;
			jumping=false;
			falling=true;
			canFall=false;
			prevTimes[7]=System.nanoTime();
		}

		if (walking) {tempx = checkLRCollisions(xyID, tempx, currCol);}
		x[xyID]=tempx;

		calculateCorners(x[xyID], toyU, xyID);
		if(topRight|| topLeft)
		{
			tempy = ((currRow * tileMap.getTileSize()));
			falling=true;
			jumping=false;
			jump=false;
			canFall=false;
			prevTimes[7]=System.nanoTime();
		}
		else{tempy-=jumpSpeed;}
		y[xyID]=tempy;
	}

	public void falling(Graphics g)
	{
		int xyID=playerID;
		int animID=4;

		Graphics2D g2d = (Graphics2D) g;

		try
		{
			getBobbingTile(x[xyID],y[xyID],xyID);
		}
		catch(Exception e){}
		if(topLeftB || topRightB || botLeftB || botRightB)
		{
			if(left){g2d.drawImage(jumpingImages[0],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
			if(right){g2d.drawImage(jumpingImages[1],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		}
		else
		{
			if(left){g2d.drawImage(fallingImages[0][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
			if(right){g2d.drawImage(fallingImages[1][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		}

	}

	public void calcFalling()
	{
		int xyID=playerID;
		int animID=4;
		int animFrames=6;
		int currRow = tileMap.getRowTile(y[xyID]);
		int currCol = tileMap.getColTile(x[xyID]);
		int tempx = x[xyID];
		int tempy = y[xyID];
		int toyD = y[xyID]+(int)ySpd[xyID];
		long passedTime;
		long passedTime1;
		inAir=true;
		landed=false;
		if(Dpressed && !Apressed){right=true; left=false;}
		if(Apressed && !Dpressed){left=true; right=false;}

		width[xyID] = fallingImages[0][animFrames-1].getWidth(null);
		height[xyID] = fallingImages[0][animFrames-1].getHeight(null);

		//begin float tile check
		try
		{
			getBobbingTile(x[xyID],y[xyID],xyID);
		}
		catch(Exception e){}
		if(topLeftB || topRightB || botLeftB || botRightB)
		{
			if(!bobbing)
			{
				//tempy-=2;
				bobbing=true;
			}
		}
		else
		{
			//System.out.println("currently falling "+x[xyID]);
			bobbing=false;
			passedTime = System.nanoTime() - prevTimes[animID];
			if(current[animID]==1){wait[animID]=true;}
			if(wait[animID] && passedTime/2000000000 <1){}
			else if(passedTime/125000000>=1)
			{
				current[animID]++;
				wait[animID]=false;
				passedTime=0;
				prevTimes[animID]=System.nanoTime();
			}

			if(current[animID]>=animFrames)
			{
				current[animID]=animFrames-1;
			}

			passedTime1 = System.nanoTime() - prevTimes[7];
			if(passedTime1/60000000>=1)
			{
				prevTimes[9]=System.nanoTime();
				canFall=true;
			}

			if(canFall)
			{
				if(ySpd[xyID]<10)
				{
					ySpd[xyID]+=0.25;
				}
				tempy+=ySpd[xyID];
			}

			calculateCorners(x[xyID], toyD, xyID);
			if(botLeft || botRight)
			{
				tempy = (currRow) * tileMap.getTileSize();
				playSound("audiomancer/falling/landed/step.au",false);
				landed=true;
			}
			y[xyID]=tempy;
			if (walking) {
				ySpd[2] = ySpd[xyID];
				ySpd[xyID]=9D;
				tempx = checkLRCollisions(xyID, tempx, currCol);
				ySpd[xyID]=ySpd[2];
			}
			x[xyID]=tempx;

		}
		//end float tile check

		if(bobbing)
		{
			if(bobUp){bobbingSpd-=0.25;}
			if(bobDown){bobbingSpd+=0.25;}
			if(bobbingSpd<=-2.25){bobbingSpd=0; bobDown=true; bobUp=false;}
			if(bobbingSpd>=3){bobbingSpd=0; bobUp=true; bobDown=false;}
			tempy+=bobbingSpd;
			y[xyID]=tempy;
		}

		if (walking && bobbing) {
			ySpd[2] = ySpd[xyID];
			ySpd[xyID]=9D;
			tempx = checkLRCollisions(xyID, tempx, currCol);
			ySpd[xyID]=ySpd[2];
			x[xyID]=tempx;
		}

		if(landed)
		{
			bobbing=false;
			canFloat=true;
			current[animID]=0;
			falling=false;
			inAir=false;
			ySpd[xyID]=defaultYSpd;
			jumpSpeed=defaultJumpSpeed;
		}
	}

	public void character_shoot(Graphics g)
	{
		int xyID=playerID;
		int animID=5;

		Graphics2D g2d = (Graphics2D) g;

		if(left){g2d.drawImage(shootingImages[0][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		if(right){g2d.drawImage(shootingImages[1][current[animID]],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
	}

	public void calcShooting()
	{
		int xyID=playerID;
		int animID=5;
		int animFrames=13;
		long passedTime;

		width[xyID] = shootingImages[0][animFrames-1].getWidth(null);
		height[xyID] = shootingImages[0][animFrames-1].getHeight(null);

		passedTime = System.nanoTime() - prevTimes[animID];

		if(passedTime/32000000 >= 1)
		{
			current[animID]++;
			wait[animID]=false;
			passedTime=0;
			prevTimes[animID]=System.nanoTime();
		}

		if(current[animID]>=animFrames)
		{
			shooting=false;
			shoot=false;
			attacking=false;
			current[animID]=0;
		}
		if(current[animID]==1)
		{
			current[animID]++;
			playSound("/audiomancer/attacks/bolt/attack.wav",false);
		}
		if(current[animID]==8)
		{
			current[animID]++;
			shootBolt=true;
		}
	}

	public void floating(Graphics g)
	{
		int xyID=playerID;
		int animID=6;
		Graphics2D g2d = (Graphics2D) g;

		if(left){g2d.drawImage(jumpingImages[0],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
		if(right){g2d.drawImage(jumpingImages[1],x[xyID]+tileMap.getX(),y[xyID]+tileMap.getY(),this);}
	}

	public void calcFloating()
	{
		int xyID=playerID;
		int animID=6;
		int animFrames=1;
		int currRow = tileMap.getRowTile(y[xyID]);
		int currCol = tileMap.getColTile(x[xyID]);
		int tempx = x[xyID];
		int tempy = y[xyID];
		int toyU = y[xyID] - defaultJumpSpeed;
		jump=false;
		jumping=false;
		canFall=false;
		inAir=true;
		if(Dpressed && !Apressed){right=true; left=false;}
		if(Apressed && !Dpressed){left=true; right=false;}

		width[xyID] = jumpingImages[animFrames-1].getWidth(null);
		height[xyID] = jumpingImages[animFrames-1].getHeight(null);

		getFloatTile(x[playerID], y[playerID], playerID);
		if(topLeftF || topRightF || botLeftF || botRightF){canFloat=true;}
		else{floatCharacter=false; canFloat=false;}


		if(floatSpeed<=defaultFloatSpeed)
		{
			floatSpeed+=0.2;
		}

		if (walking) {
			tempx = checkLRCollisions(xyID, tempx, currCol);
		}
		x[xyID]=tempx;

		calculateCorners(x[xyID], toyU, xyID);
		if(topRight || topLeft)
		{
			tempy = ((currRow * tileMap.getTileSize()));
		}
		else if(canFloat)
		{
			current[4]=0;
			ySpd[xyID]=defaultYSpd;
			tempy-=floatSpeed;
		}

		y[xyID]=tempy;
	}

	public void playSound(String filename, boolean loop)
	{
		Thread soundThread = new MakeSound(filename, loop);
		try
		{
			soundThread.start();
		}
		catch(Exception e){}

	}

	public Rectangle getBounds(int ID)
	{
		return new Rectangle((int)x[ID], (int)y[ID], width[ID], height[ID]);
	}

	public boolean isStandingInDoor(){return standingInDoor;}
	public boolean getShootBolt(){return shootBolt;}
	public boolean getSlow(){return slow;}
	public int getWidth(int ID){return width[ID];}
	public int getHeight(int ID){return height[ID];}
	public int getX(int ID){return x[ID];}
	public int getY(int ID){return y[ID];}
	public void setX(int i, int ID){x[ID]=i;}
	public void setY(int i, int ID){y[ID]=i;}
	public int boltY(){return(y[playerID] + (width[playerID]/2))-8;}
	public boolean getDPressed(){return Dpressed;}
	public boolean getAPressed(){return Apressed;}
	public boolean walking(){return(walk);}
	public boolean shooting(){return(shoot);}
	public boolean characterRight(){return(right);}
	public boolean characterLeft(){return(left);}
	public boolean characterStanding(){return(standing);}
	public boolean isFalling(){return falling;}
	public boolean isJumping(){return jumping;}
	public void setShooting(boolean i){shoot=i;}
	public void setWalking(boolean i){walk=i;}
	public void setLeft(boolean i){left=i;}
	public void setRight(boolean i){right=i;}
	public void setStanding(boolean i){standing=i;}
	public void setFalling(boolean i){falling = i;}
	public void setJumping(boolean i){jumping=i;}
	public void setShootBolt(boolean i){shootBolt = i;}
	public void setStandingInDoor(boolean i){standingInDoor=i;}

}
