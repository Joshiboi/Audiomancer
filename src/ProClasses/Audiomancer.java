package Audiomancer;

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

public class Audiomancer extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 8184458597936033041L;

	private TileMap tileMap;
	
	protected int animationCount;
    protected int[] x,y;
    protected int playerID=0;
    protected int boltID=1;
    protected int shockwaveID=2;
    protected int[] width, height;
    
    protected boolean left=true;
    protected boolean right=false;
    
    protected boolean standing=true;
    
    protected boolean walk=false;
    
    protected boolean shoot=false;
    protected boolean characterShoot=false;
    protected boolean shootLeft;
    protected boolean shootRight;
    protected int boltCount=0;
    
    protected boolean shockwave_animation=false;
    protected boolean performShockwave;
    
    protected boolean canPerformShockwave=true;
    protected boolean canMove=true;
    protected boolean canShoot=true;
    protected boolean canJump=true;
    protected boolean falling=false;
    protected boolean jumping=false;
    protected boolean inAir=false;
    protected boolean landed=false;
    protected boolean Apressed=false;
    protected boolean Dpressed=false;
    protected boolean inAirLeft=false;
    protected boolean inAirRight=false;
    protected boolean speed=false;
    
    private double jumpSpeed=5;
    
    protected int xSpd = 3;
    protected double ySpd = 1;
    protected int boltSpd=10;
    
    private boolean topLeft, topRight, botLeft, botRight;
    private boolean topLeftLeft, topLeftTop, botLeftLeft, botLeftBot, topRightRight, topRightTop, botRightRight, botRightBot;
    
    public Audiomancer(TileMap tm)
    {
    	tileMap = tm;
    	
    	animationCount=10;
        x = new int[animationCount];
        y = new int[animationCount];
        width = new int[animationCount];
        height = new int[animationCount];
        
        for(int i=0;i<animationCount;i++)
        {
	        x[i]=640;
	        y[i]=(720/2);
        }
        
        width[playerID]=32;
        height[playerID]=32;
    }
    
    public void keyPressed(KeyEvent e)
    {
        int _e = e.getKeyCode();
        if(KeyEvent.VK_SHIFT == _e)
        {
        	xSpd=10;
        	speed=true;
        }
        
        if(KeyEvent.VK_D == _e)
        {
        	Dpressed=true;
        	if(falling || jumping){inAir=true; right=true; left=false;}
            if(canMove)
            {
                standing=false;
                walk=true;
                right = true;
                left=false;
            }
        }
        if(KeyEvent.VK_A == _e)
        {
        	Apressed=true;
        	if(falling || jumping){inAir=true; left=true; right=false;}
            if(canMove)
            {
                standing=false;
                walk=true;
                left=true;
                right=false;
            }
        }
        if(KeyEvent.VK_RIGHT == _e)
        {
            if(canShoot)
            {
            	canShoot=false;
                standing=false;
                canPerformShockwave=false;
                
                right=true;
                left=false;
                
                if(walk){walk=false;}
                
                if(!shootLeft)
                {
                    shootRight=true;
                }
                canMove=false;
                characterShoot=true;
                
                if(!shoot)
                {
                    x[boltID]=x[playerID]+9;
                    y[boltID]=y[playerID]+4;
                }
            }
        }
        if(KeyEvent.VK_LEFT == _e)
        {
            if(canShoot)
            {
            	canShoot=false;
                canPerformShockwave=false;
                standing=false;
                
                right=false;
                left=true;
                
                if(walk){walk=false;}
                if(!shootRight)
                {
                    shootLeft=true;
                }
                canMove=false;
                characterShoot=true;
                
                if(!shoot)
                {
                	x[boltID]=x[playerID]-9;
                    y[boltID]=y[playerID]+4;
                }
            }
        }
        if(KeyEvent.VK_SPACE == _e)
        {
        	if(canJump)
        	{
        		if(walk)
        		{
        			inAir=true;
        		}
        		if(characterShoot){characterShoot=false;}
	        	jumping=true;
	        	canShoot=false;
	        	canJump=false;
	        	standing=false;
	        	walk=false;
	        	canMove=false;
	        	canPerformShockwave=false;
	        	landed=false;
        	}
        	
        }
        if(KeyEvent.VK_CONTROL == _e)
        {
        	if(canPerformShockwave)
            {
            	canPerformShockwave=false;
                performShockwave=true;
                canMove=false;
                canShoot=false;
                standing=false;
                walk=false;
                x[shockwaveID]=x[playerID]-46;
                y[shockwaveID]=y[playerID];
            }
        }
    }
    public void keyReleased(KeyEvent e)
    {
        int _e = e.getKeyCode();
        if(KeyEvent.VK_SHIFT == _e)
        {
        	xSpd=3;
        	speed=false;
        }
        if(KeyEvent.VK_D == _e)
        {
        	Dpressed=false;
        	if(!Apressed)
        	{
        		if(falling || jumping){inAir=false;}
        	}
            if(canMove)
            {
            	if(!Apressed)
            	{
	                walk=false;
	                standing=true;
            	}
            }
        }
        if(KeyEvent.VK_A == _e)
        {
        	Apressed=false;
        	if(!Dpressed)
        	{
        		if(falling || jumping){inAir=false;}
        	}
            if(canMove)
            {
            	if(!Dpressed)
            	{
	                walk=false;
	                standing=true;
            	}
            }
        }
    }
    public void keyTyped(KeyEvent e){}
    public int checkLRCollisions (int animID, int tempx, int currCol) {
    	int toxL = x[animID] - xSpd;
        int toxR = x[animID] + xSpd;
        
    	/*try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    	
    	if(left) {
    		calculateCorners(toxL, y[animID]);
        	
        	// System.out.println("funcL TLL: " + topLeftLeft + ", BLL: " + botLeftLeft + ", TLT: " + topLeftTop + ", BLB: " + botLeftBot);
        	
            if (botLeftLeft || topLeftLeft) // if(topLeft || botLeft)
            {
            	// tempx = ( ((currCol+1) * tileMap.getTileSize()) - width[animID]/2+6);
            //	tempx = ( ((currCol) * tileMap.getTileSize()) - (width[animID]/2) + 6);
            	tempx = ( ((currCol) * tileMap.getTileSize()));
            }
            else
            {
            	tempx -=xSpd;
            }
        }
    	if (right) {
        	calculateCorners(toxR, y[animID]);
        	
        	// System.out.println("funcR TLL: " + topLeftLeft + ", BLL: " + botLeftLeft + ", TLT: " + topLeftTop + ", BLB: " + botLeftBot);
        	
            if (topRightRight || botRightRight) // if(topLeft || botLeft)
            {
            	// tempx = ( ((currCol+1) * tileMap.getTileSize()) - width[animID]/2+6);
            	// tempx = ( ((currCol) * tileMap.getTileSize()) + (width[animID]/2)-6);
            	tempx = ( ((currCol) * tileMap.getTileSize()));
            }
            else
            {
            	tempx +=xSpd;
            }
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
        int rightTile = tileMap.getColTile((int) (_x +width[playerID])-xSpd);
        int topTile = tileMap.getRowTile((int) (_y));
        int bottomTile = tileMap.getRowTile((int) (_y +height[playerID])-(int)ySpd);
        
        topLeft = tileMap.getTile(topTile, leftTile) == 0;
        topRight = tileMap.getTile(topTile, rightTile) == 0;
        botLeft = tileMap.getTile(bottomTile, leftTile) == 0;
        botRight = tileMap.getTile(bottomTile, rightTile) == 0;
        
        
        
        
        
        if (topLeft && (leftTile * tileMap.getTileSize() < tileMap.getColTile(x[playerID]) * tileMap.getTileSize())) {
        	topLeftLeft = true;
        } else if (topLeft){
        	topLeftTop = true;
        }
        
        if (botLeft && (leftTile * tileMap.getTileSize() < tileMap.getColTile(x[playerID]) * tileMap.getTileSize())) {
        	botLeftLeft = true;
        } else if (botLeft){
        	botLeftBot = true;
        }
        
        if (topRight && (rightTile * tileMap.getTileSize() > tileMap.getColTile(x[playerID]) * tileMap.getTileSize())) {
        	topRightRight = true;
        } else if (topLeft){
        	topRightTop = true;
        }
        
        if (botRight && (rightTile * tileMap.getTileSize() > tileMap.getColTile(x[playerID]) * tileMap.getTileSize())) {
        	botRightRight = true;
        } else if (botLeft){
        	botRightBot = true;
        }
        
        
    }
    
    public void stand(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=2;
        int toyD = y[animID] + (int)ySpd;
        
        Image[] standingRight = new Image[animFrames];
        ImageIcon[] standingRightImages = new ImageIcon[animFrames];
        
        Image[] standingLeft = new Image[animFrames];
        ImageIcon[] standingLeftImages = new ImageIcon[animFrames];
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            standingRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Stand/audiomancer_stand_right_"+(i+1)+".png"));
            standingRight[i] = standingRightImages[i].getImage();
            
            standingLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Stand/audiomancer_stand_left_"+(i+1)+".png"));
            standingLeft[i] = standingLeftImages[i].getImage();
        }
        
        calculateCorners(x[animID]+xSpd, toyD);
    	if(!botLeft)
        {
    		calculateCorners(x[animID]-xSpd, toyD);
    		if(!botRight)
    		{
    			falling=true;
            	landed=false;
    		}
        }
        if(left){g2d.drawImage(standingLeft[current],x[animID],y[animID],this);}
        if(right){g2d.drawImage(standingRight[current],x[animID],y[animID],this);}
    }
    
    public void walk(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=8;
        int toyD = y[animID] + (int)ySpd;
        
        // int toxL = x[animID] - xSpd;
        // int toxR = x[animID] + xSpd;
        
        int currCol = tileMap.getColTile(x[animID]);
        
       // System.out.println("walkin" + currCol);
        
        int tempx = x[animID];
        
        Image[] walkingRight = new Image[animFrames];
        ImageIcon[] walkingRightImages = new ImageIcon[animFrames];
        
        Image[] walkingLeft = new Image[animFrames];
        ImageIcon[] walkingLeftImages = new ImageIcon[animFrames];
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            walkingLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Walk/audiomancer_walk_left_"+(i+1)+".png"));
            walkingLeft[i] = walkingLeftImages[i].getImage();
            
            walkingRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Walk/audiomancer_walk_right_"+(i+1)+".png"));
            walkingRight[i] = walkingRightImages[i].getImage();
        }
        
        calculateCorners(x[animID]+xSpd, toyD);
    	if(!botLeft)
        {
    		calculateCorners(x[animID]-xSpd, toyD);
    		if(!botRight)
    		{
    			falling=true;
            	landed=false;
            	inAir=true;
    		}
        }
      
    	
    	
        tempx = checkLRCollisions(animID, tempx, currCol);
        x[animID]=tempx;
        if(right){g2d.drawImage(walkingRight[current],x[animID],y[animID],this);}
        if(left){g2d.drawImage(walkingLeft[current],x[animID],y[animID],this);}
    }
    
    public void character_shoot(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=12;
        
        Image[] shootingLeft = new Image[animFrames];
        ImageIcon[] shootingLeftImages = new ImageIcon[animFrames];
        
        Image[] shootingRight = new Image[animFrames];
        ImageIcon[] shootingRightImages = new ImageIcon[animFrames];
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            shootingLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Spell/audiomancer_spell_left_"+(i+1)+".png"));
            shootingLeft[i] = shootingLeftImages[i].getImage();
            
            shootingRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Spell/audiomancer_spell_right_"+(i+1)+".png"));
            shootingRight[i] = shootingRightImages[i].getImage();
        }
        
        if(current==8)
        {
            shoot=true;
        }
            
        if(left)
        {
            g2d.drawImage(shootingLeft[current],x[animID],y[animID],this);
        }
        if(right)
        {
            g2d.drawImage(shootingRight[current],x[animID],y[animID],this);
        }
    }
    
    public void bolt(Graphics g, int current)
    {
    	int animID=boltID;
        int animFrames=2;
        Image[] bolt = new Image[animFrames];
        ImageIcon[] boltImages = new ImageIcon[animFrames];
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            boltImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Spell Effect/bolt_"+(i+1)+".png"));
            bolt[i] = boltImages[i].getImage();
        }
        
        
        if(shootLeft)
        {
            x[animID]-=boltSpd;
            if(x[animID]<=5)
            {
                shoot=false;
                shootLeft=false;
                canShoot=true;
                System.out.println(+x[animID]+" "+shoot);
            }
            System.out.println("drawing at: "+x[animID]+", "+y[animID]);
            g2d.drawImage(bolt[current],x[animID],y[animID],this);
        }
        
        if(shootRight)
        {
            x[animID]+=boltSpd;
            if(x[animID]>=1200)
            {
                shoot=false;
                shootRight=false;
                canShoot=true;
                System.out.println(+x[animID]+" "+shoot);
            }
            System.out.println("drawing at: "+x[animID]+", "+y[animID]);
            g2d.drawImage(bolt[current],x[animID],y[animID],this);
        }
    }
    
    public void character_shockwave(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=14;
        
        Image[] shockwaveRight = new Image[animFrames];
        ImageIcon[] shockwaveRightImages = new ImageIcon[animFrames];
        
        Image[] shockwaveLeft = new Image[animFrames];
        ImageIcon[] shockwaveLeftImages = new ImageIcon[animFrames];
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            shockwaveRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Slam/audiomancer_slam_right_"+(i+1)+".png"));
            shockwaveRight[i] = shockwaveRightImages[i].getImage();
            
            shockwaveLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Slam/audiomancer_slam_left_"+(i+1)+".png"));
            shockwaveLeft[i] = shockwaveLeftImages[i].getImage();
        }
        
        if(right){g2d.drawImage(shockwaveRight[current],x[animID],y[animID],this);}
        if(left){g2d.drawImage(shockwaveLeft[current],x[animID],y[animID],this);}
    }
    
    public void shockwave(Graphics g, int current)
    {
    	int animID=shockwaveID;
        int animFrames=8; 
        
        Image[] shockwave = new Image[animFrames];
        ImageIcon[] shockwaveImages = new ImageIcon[animFrames];
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
            shockwaveImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/Slam Effect/shockwave_"+(i+1)+".png"));
            shockwave[i] = shockwaveImages[i].getImage();
        }
        
        g2d.drawImage(shockwave[current],x[animID],y[animID],this);
    }
    
    public void falling(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=6;
        
    	int currRow = tileMap.getRowTile(y[animID]);
    	int currCol = tileMap.getColTile(x[animID]);
        int tempx = x[animID];
    	int tempy = y[animID];
    	int toyD = y[animID] + (int)ySpd;
    	
    	characterShoot=false;
		walk=false;
		canMove=false;
		canPerformShockwave=false;
		canShoot=false;
		canJump=false;
		standing=false;
        
        Image[] fallingRight = new Image[animFrames];
        ImageIcon[] fallingRightImages = new ImageIcon[animFrames];
        
        Image[] fallingLeft = new Image[animFrames];
        ImageIcon[] fallingLeftImages = new ImageIcon[animFrames];
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
        	fallingRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/jumping/Jumpfall Transit/audiomancer_jumpfall_right_"+(i+1)+".png"));
            fallingRight[i] = fallingRightImages[i].getImage();
            
            fallingLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/jumping/Jumpfall Transit/audiomancer_jumpfall_left_"+(i+1)+".png"));
            fallingLeft[i] = fallingLeftImages[i].getImage();
        }
		y[animID]+=ySpd;
		ySpd+=0.25;
		
		
		
		
        calculateCorners(x[animID], toyD);
    	if(botLeft || botRight)
    	{
    		tempy = (currRow) * tileMap.getTileSize();
    		y[animID]=tempy;
    		landed=true;
    	}
    	else {
    		if (inAir) {
    			tempx = checkLRCollisions(animID, tempx, currCol);
    		}
    		x[animID]=tempx;
    	}
        
        if(right){g2d.drawImage(fallingRight[current],x[animID],y[animID],this);}
        if(left){g2d.drawImage(fallingLeft[current],x[animID],y[animID],this);}
        
        if(landed)
    	{
    		canShoot=true;
    		standing=false;
    		falling=false;
    		canMove=true;
    		canPerformShockwave=true;
    		canJump=true;
    		if(inAir && left)
    		{
    			walk=true;
    		}
    		if(inAir && right)
    		{
    			walk=true;
    		}
    		if (!inAir && !walk)
    		{
    			standing=true;
    		}
    		inAir=false;
    		ySpd=1;
    		jumpSpeed=5;
    	}
    
    }
    
    public void jump(Graphics g, int current)
    {
    	int animID=playerID;
        int animFrames=1;
        
        int currRow = tileMap.getRowTile(y[animID]);
    	int currCol = tileMap.getColTile(x[animID]);
        int tempx = x[animID];
    	int tempy = y[animID];
    	int toyU = y[animID] - (int)jumpSpeed;
        
        Image[] jumpingRight = new Image[animFrames];
        ImageIcon[] jumpingRightImages = new ImageIcon[animFrames];
        
        Image[] jumpingLeft = new Image[animFrames];
        ImageIcon[] jumpingLeftImages = new ImageIcon[animFrames];
        
        Graphics2D g2d = (Graphics2D) g;
        
        for(int i=0;i<animFrames;i++)
        {
        	jumpingRightImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/jumping/audiomancer_jump_right.png"));
        	jumpingRight[i] = jumpingRightImages[i].getImage();
            
        	jumpingLeftImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/audiomancer/jumping/audiomancer_jump_left.png"));
        	jumpingLeft[i] = jumpingLeftImages[i].getImage();
        }	
        
        jumpSpeed-=0.2;
		if(jumpSpeed<=0)
		{
			jumping=false;
			falling=true;
		}
		
		if (inAir) {
			tempx = checkLRCollisions(animID, tempx, currCol);
		}
		x[animID]=tempx;
		
		calculateCorners(x[animID], toyU);
		if(topRightTop || topLeftTop)
		{
			
			tempy = ((currRow * tileMap.getTileSize()));
        }
        else
        {
            tempy-=jumpSpeed;
        }
		
		if(right)
		{
			calculateCorners(x[animID], toyU);
			if(topLeftTop || topRightTop)
			{
				tempy = ((currRow) * tileMap.getTileSize());
				falling=true;
				jumping=false;
			}
			else
			{
				tempy-=jumpSpeed;
			}
		}
		if(left)
		{
			calculateCorners(x[animID], toyU);
			if(topLeftTop || topRightTop)
			{
				tempy = ((currRow) * tileMap.getTileSize());
				falling=true;
				jumping=false;
			}
			else
			{
				tempy-=jumpSpeed;
			}
		}
		
	      
	    y[animID]=tempy;
	
        if(right){g2d.drawImage(jumpingRight[current],x[animID],y[animID],this);}
        if(left){g2d.drawImage(jumpingLeft[current],x[animID],y[animID],this);}
    
    }
    
    public void drawBox(Graphics g)
    {
        
    	g.setColor(Color.GREEN);
    	g.drawRect(x[playerID], y[playerID], 31, 31);
    	g.drawRect(x[playerID]-xSpd, y[playerID], 31+xSpd, 31);
    	g.setColor(Color.RED);
    	g.drawRect(x[playerID], y[playerID]-(int)jumpSpeed, 31 , 31+(int)jumpSpeed);
    	//g.drawRect(x[playerID]-xSpd-(width[playerID]/2), y[playerID], 32, 32);
    }
    
   /*public Rectangle getBounds(int ID)
    {
        return new Rectangle((int)x, (int)y, width, height);
    }*/
    public int getX(int ID){return x[ID];}
    public int getY(int ID){return y[ID];}
    public void setX(int i, int ID){x[ID]=i;}
    public void setY(int i, int ID){y[ID]=i;}
    public boolean walking(){return(walk);}
    public boolean characterShooting(){return(characterShoot);}
    public boolean shooting(){return(shoot);}
    public boolean animatingShockwave(){return(shockwave_animation);}
    public boolean performingShockwave(){return(performShockwave);}
    public boolean characterRight(){return(right);}
    public boolean characterLeft(){return(left);}
    public boolean characterStanding(){return(standing);}
    public boolean isFalling(){return falling;}
    public boolean isJumping(){return jumping;}
    public void setCanShoot(boolean i){canShoot=i;}
    public void setShooting(boolean i){shoot=i;}
    public void setCharacterShooting(boolean i){characterShoot=i;}
    public void setWalking(boolean i){walk=i;}
    public void setLeft(boolean i){left=i;}
    public void setRight(boolean i){right=i;}
    public void setCanMove(boolean i){canMove=i;}
    public void setShockwaveAnimation(boolean i){shockwave_animation=i;}
    public void setPerformShockwave(boolean i){performShockwave=i;}
    public void setCanPerformShockwave(boolean i){canPerformShockwave=i;}
    public void setStanding(boolean i){standing=i;}
    public void setFalling(boolean i){falling = i;}
    public void setJumping(boolean i){jumping=i;}
}
