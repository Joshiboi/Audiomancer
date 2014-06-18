 package ProClasses;


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
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;

public class Board extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1105451800805702002L;
	//class instance declarations
    private Audiomancer audiomancer;
    private Timer timer;
    private TileMap tileMap;
    
    private Audiomancer_bolt[] bolts;
    private int maxBolts = 1000;
    private int currentBolts;
    
    private Audiomancer_shockwave[] shockwaves;
    private int maxShockwaves = 1000;
    private int currentShockwaves;
    
    //framerate variables
    private static long prevTime=System.nanoTime();
    private static long passedTime;
    private static int fps=0;
    private static int frames=0;
    
    //animation variables
    private long[] prevTimes;
    private int animationCount=10;
    private int[] current;
    private boolean[] wait;
    
    protected static int width, height;
    
    public Board()
    {
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        requestFocus();
        addMouseListener(new TAdapter());
        addKeyListener(new TAdapter2());
        
        prevTimes = new long[animationCount];
        wait = new boolean[animationCount];
        current = new int[animationCount];
        
        for(int i=0;i<animationCount;i++)
        {
            prevTimes[i]=System.nanoTime();
            wait[i]=false;
            current[i]=0;
        }
        
        shockwaves = new Audiomancer_shockwave[maxShockwaves];
        bolts = new Audiomancer_bolt[maxBolts];
        tileMap = new TileMap("testmap.txt", 32);
        timer = new Timer(16, this);
        audiomancer = new Audiomancer(tileMap);
        
        width = 1280;
        height = 720;
    }
    
    public void destroyBolt(int target)
    {
    	bolts[target] = bolts[currentBolts-1];
    	currentBolts--;
    }
    
    public void destroyShockwave(int target)
    {
    	shockwaves[target] = shockwaves[currentShockwaves-1];
    	currentShockwaves--;
    }
        
    public void actionPerformed(ActionEvent e)
    {
        passedTime = System.nanoTime() - prevTime;
        if(passedTime/1000000000 >= 1)
        {
            //System.out.println("one second passed");
            fps=frames;
            prevTime = System.nanoTime();
            passedTime = 0;
            frames=0;
        }
        frames++;
        
        for(int i=0;i<currentBolts;i++)
        {
        	if(bolts[i].isColliding())
        	{
        		destroyBolt(i);
        	}
        }
        
        if(!audiomancer.isFalling())
        {
        	current[6]=0;
        }
        
        tileMap.setX(width/2 - audiomancer.getX(0));
        tileMap.setY(height/2 - audiomancer.getY(0));
        
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("Italic", Font.PLAIN, 40));
        
        //draw the map
        tileMap.draw(g2d);
        
        //draw audiomancer animations
        if(audiomancer.characterStanding())
        {
            audiomancer_stand(g);
        }
        if(audiomancer.walking())
        {
            audiomancer_walk(g);
        }
        if(audiomancer.characterShooting())
        {
            audiomancer_shoot(g);
        }
        if(audiomancer.performingShockwave())
        {
            audiomancer_shockwave(g);
        }
        if(audiomancer.isFalling())
        {
        	audiomancer_falling(g);
        }
        if(audiomancer.isJumping())
        {
        	audiomancer_jumping(g);
        }
        
        //draw audiomancer sub-animations
        for(int i=0;i<currentBolts;i++)
        {
        	drawBolt(g, i);
        }
        for(int i=0;i<currentShockwaves;i++)
        {
        	drawShockwave(g, i);
        }
        
        //audiomancer.drawBox(g);
        g.drawString(""+fps, 35, 67);
    }
    
    public void audiomancer_stand(Graphics g)
    {
        //this is animation 0
        int animID=0;
        int animFrames=2;
        long passedTime;
        passedTime = System.nanoTime() - prevTimes[animID];
        
        if(passedTime/1000000000 >= 1)
        {
            if(current[animID]==1){wait[animID]=true;}
            
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
        audiomancer.stand(g, current[animID]);
    }
    
    public void audiomancer_walk(Graphics g)
    {
        //this is animation 1
        int animID=1;
        int animFrames=8;
        long passedTime;
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
        audiomancer.walk(g, current[animID]);
    }
    
    public void audiomancer_shoot(Graphics g)
    {
        //this is animation 2
        int animID=2;
        int animFrames=11;
        long passedTime;
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
            current[animID]=0;
            audiomancer.setCharacterShooting(false);
            audiomancer.setCanMove(true);
            if(!audiomancer.getDPressed() && !audiomancer.getAPressed())
            {
            	audiomancer.setStanding(true);
            }
            else
            {
            	audiomancer.setWalking(true);
            }
            audiomancer.setCanPerformShockwave(true);
            audiomancer.setCanJump(true);
            audiomancer.setCanShoot(true);
            
        }
        if(current[animID]==8)
        {
        	current[animID]++;
        	if(audiomancer.characterLeft())
            {
            	if(currentBolts < maxBolts )
                {
                	bolts[currentBolts] = new Audiomancer_bolt(tileMap, audiomancer.getX(0), audiomancer.boltY(), -10);
                	currentBolts++;
                }
            }
            else if(audiomancer.characterRight())
            {
            	if(currentBolts < maxBolts )
                {
                	bolts[currentBolts] = new Audiomancer_bolt(tileMap, audiomancer.getX(0)+audiomancer.getWidth(0), audiomancer.boltY(), 10);
                	currentBolts++;
                }
            }
        }
        audiomancer.character_shoot(g, current[animID]);
    }
   
    public void audiomancer_shockwave(Graphics g)
    {
        //this is animation 4
        int animID=4;
        int animFrames=14;
        long passedTime;
        passedTime = System.nanoTime() - prevTimes[animID];
        
        if(passedTime/31250000 >= 1)
        {
            current[animID]++;
            wait[animID]=false;
            passedTime=0;
            prevTimes[animID]=System.nanoTime();
        }
        
        if(current[animID]>=animFrames)
        {
            current[animID]=0;
            audiomancer.setPerformShockwave(false);
            audiomancer.setCanMove(true);
            if(!audiomancer.getDPressed() && !audiomancer.getAPressed())
            {
            	audiomancer.setStanding(true);
            }
            else
            {
            	audiomancer.setWalking(true);
            }
            audiomancer.setCanShoot(true);
            audiomancer.setCanJump(true);
        }
        
        if(current[animID]==8)
        {
        	audiomancer.setShockwaveAnimation(true);
        	current[animID]++;
        	if(currentShockwaves < maxShockwaves)
        	{
        		shockwaves[currentShockwaves] = new Audiomancer_shockwave(tileMap, audiomancer.getX(0) - 48, audiomancer.getY(0));
        		currentShockwaves++;
        	}
        }
        
        audiomancer.character_shockwave(g, current[animID]);
    }
    
    
    public void audiomancer_falling(Graphics g)
    {
        //this is animation 6
        int animID=6;
        int animFrames=7;
        long passedTime;
        passedTime = System.nanoTime() - prevTimes[animID];
        if(current[animID]==1){wait[animID]=true;}
        if(wait[animID] && passedTime/500000000 <1){}
        
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
        audiomancer.falling(g, current[animID]);
    }
    
    public void audiomancer_jumping(Graphics g)
    {
        //this is animation 7
        int animID=7;
        int animFrames=1;
        long passedTime;
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
        audiomancer.jump(g, current[animID]);
    }
    
    public void drawBolt(Graphics g, int ID)
    {
    	//this is animation 3
    	int animID=3;
        int animFrames=2;
        long passedTime;
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
            current[animID]=0;
        }
        bolts[ID].draw(g, current[animID]);
    }
    
    public void drawShockwave(Graphics g, int ID)
    {
    	//this is animation 5
        int animID=5;
        int animFrames=8;
        long passedTime;
        passedTime = System.nanoTime() - prevTimes[animID];
        
        if(passedTime/25000000 >= 1)
        {
            current[animID]++;
            wait[animID]=false;
            passedTime=0;
            prevTimes[animID]=System.nanoTime();
        }
        
        if(current[animID]>=animFrames)
        {
            current[animID]=0;
            destroyShockwave(ID);
            audiomancer.setCanPerformShockwave(true);
            audiomancer.setShockwaveAnimation(false);
        }
        else{shockwaves[ID].draw(g, current[animID]);}
    }
    public static void main(String[] args)
    {
        JFrame f = new JFrame("Board");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Board window = new Board();
        f.add(window);
        f.setSize(width, height);
        f.setVisible(true);
        f.setResizable(true);
        window.timer.start();
    }

    private class TAdapter extends MouseAdapter
    {
        public void mousePressed(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
        public void mouseClicked(MouseEvent e){}
    }
    private class TAdapter2 extends KeyAdapter
    {
        public void keyPressed(KeyEvent e){audiomancer.keyPressed(e);}
        public void keyReleased(KeyEvent e){audiomancer.keyReleased(e);}
        public void keyTyped(KeyEvent e){}
    }
}

