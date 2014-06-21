package ProClasses;
import java.applet.AudioClip;
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
	private MainMenu mainMenu;
    private Audiomancer audiomancer;
    private Psychomancer psychomancer;
    private Timer timer;
    private TileMap tileMap;
    private static AudioClip stepSound;
    
    
    private Audiomancer_bolt[] bolts;
    private int maxBolts = 1000;
    private int currentBolts;
    
    private Button[] buttons;
    private int maxButtons=100;
    private int currentButtons;
    
    
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
    private boolean inMainMenu;
    private boolean inCharacterSelect;
    private boolean playAudiomancer;
    private boolean playPsychomancer;
    protected static int width, height;
    
    public Board()
    {
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        requestFocus();
        addMouseListener(new TAdapter());
        addKeyListener(new TAdapter2());
        
        inMainMenu=true;
        prevTimes = new long[animationCount];
        wait = new boolean[animationCount];
        current = new int[animationCount];
        
        for(int i=0;i<animationCount;i++)
        {
            prevTimes[i]=System.nanoTime();
            wait[i]=false;
            current[i]=0;
        }
        
        buttons = new Button[maxButtons];
        mainMenu = new MainMenu();
        bolts = new Audiomancer_bolt[maxBolts];
        tileMap = new TileMap("testmap.txt", 32);
        timer = new Timer(16, this);
        audiomancer = new Audiomancer(tileMap);
        audiomancer.load();
        
        
        width = 1280;
        height = 720;
        
        if(inMainMenu)
        {
        	currentButtons=2;
        	buttons[0]= new Button((width/2)-128,(height/2)-100);
        	buttons[1] = new Button((width/2)-128,(height/2)+100);
        }
    }
    
    public void destroyBolt(int target)
    {
    	bolts[target] = bolts[currentBolts-1];
    	currentBolts--;
    }
        
    private void update()
    {
    	 if(buttons[0].getHasPressed())
         {
         	if(inMainMenu)
         	{
         		inMainMenu=false;
         		playAudiomancer=true;
         	}
         	else if(inCharacterSelect)
         	{
         		audiomancer = new Audiomancer(tileMap);
                audiomancer.load();
         		psychomancer=null;
         		playPsychomancer=false;
         		inCharacterSelect=false;
         		playAudiomancer=true;
         	}
         }
         
         if(buttons[1].getHasPressed())
         {
         	//this means the "character select" button has been pressed
         	if(inMainMenu)
         	{
 	        	inMainMenu=false;
 	        	inCharacterSelect=true;
 	        	currentButtons=2;
 	        	buttons[0]= new Button((width/2)-buttons[0].getWidth()-20,(height/2)+64);
 	        	buttons[1] = new Button((width/2)+buttons[1].getWidth()/2-20,(height/2)+64);
         	}
         	//this means the "psychomancer" button has been pressed inside character select
         	else if(inCharacterSelect)
         	{
         		psychomancer = new Psychomancer(tileMap);
         		psychomancer.load();
         		audiomancer=null;
         		playAudiomancer=false;
         		inCharacterSelect=false;
         		playPsychomancer=true;
         	}
         }
         
         for(int i=0;i<currentBolts;i++)
         {
         	if(bolts[i].isColliding())
         	{
         		destroyBolt(i);
         	}
         }
         
         
         if(audiomancer!=null)
         {
        	 tileMap.setX(width/2 - audiomancer.getX(0));
             tileMap.setY(height/2 - audiomancer.getY(0));
         	if(audiomancer.getShootBolt())
             {
             	if(audiomancer.characterLeft())
             	{
             		if(currentBolts < maxBolts )
             		{
             			bolts[currentBolts] = new Audiomancer_bolt(tileMap, audiomancer.getX(0), audiomancer.boltY(), -10);
             			currentBolts++;
             			audiomancer.setShootBolt(false);
             		}
             	}
             	else if(audiomancer.characterRight())
             	{
             		if(currentBolts < maxBolts )
             		{
             			bolts[currentBolts] = new Audiomancer_bolt(tileMap, audiomancer.getX(0)+audiomancer.getWidth(0), audiomancer.boltY(), 10);
             			currentBolts++;
             			audiomancer.setShootBolt(false);
             		}
             	}
             }
         	audiomancer.update();
         }
         
         if(psychomancer!=null)
         {
        	 tileMap.setX(width/2 - psychomancer.getX(0));
             tileMap.setY(height/2 - psychomancer.getY(0));
         	if(psychomancer.getShootBolt())
             {
             	if(psychomancer.characterLeft())
             	{
             		if(currentBolts < maxBolts )
             		{
             			bolts[currentBolts] = new Audiomancer_bolt(tileMap, psychomancer.getX(0), psychomancer.boltY(), -10);
             			currentBolts++;
             			psychomancer.setShootBolt(false);
             		}
             	}
             	else if(psychomancer.characterRight())
             	{
             		if(currentBolts < maxBolts )
             		{
             			bolts[currentBolts] = new Audiomancer_bolt(tileMap, psychomancer.getX(0)+psychomancer.getWidth(0), psychomancer.boltY(), 10);
             			currentBolts++;
             			psychomancer.setShootBolt(false);
             		}
             	}
             }
         	psychomancer.update();
         }
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
        
        update();
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setFont(new Font("Italic", Font.PLAIN, 40));
        
        //draw the map
        tileMap.draw(g2d);
        
        if(playAudiomancer)
        {
	        //draw audiomancer animations
	        audiomancer.paint(g2d);
	        //draw audiomancer sub-animations
	        for(int i=0;i<currentBolts;i++)
	        {
	        	drawBolt(g, i);
	        }
        }
        
        if(playPsychomancer)
        {
	        //draw audiomancer animations
	        psychomancer.paint(g2d);
	        //draw audiomancer sub-animations
	        for(int i=0;i<currentBolts;i++)
	        {
	        	drawBolt(g, i);
	        }
        }
        
        g.drawString(""+fps, 35, 67);
        if(inMainMenu)
        {
        	mainMenu.draw(g);
        	for(int i=0;i<currentButtons;i++)
        	{
        		buttons[i].draw(g);
        	}
        	g.drawString("Play",buttons[0].getX()+(int)(buttons[0].getWidth()*0.375),buttons[0].getY()+(int)(buttons[0].getHeight()*0.75));
        	g.setFont(new Font("Italic", Font.PLAIN, 30));
        	g.drawString("Character Select",buttons[1].getX(),buttons[1].getY()+(int)(buttons[1].getHeight()*0.75));
        }
        if(inCharacterSelect)
        {
        	mainMenu.draw(g);
        	for(int i=0;i<currentButtons;i++)
        	{
        		buttons[i].draw(g);
        	}
        	g.drawString("Audiomancer",buttons[0].getX(),buttons[0].getY()+(int)(buttons[0].getHeight()*0.75));
        	g.drawString("Psychomancer",buttons[1].getX(),buttons[1].getY()+(int)(buttons[1].getHeight()*0.75));
        }
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
        
        if(current[animID]>=animFrames){current[animID]=0;}
        
        bolts[ID].draw(g, current[animID]);
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
        public void mousePressed(MouseEvent e)
        {
        	for(int i=0;i<currentButtons;i++)
        	{
        		buttons[i].mousePressed(e);
        	}
        }
        public void mouseReleased(MouseEvent e){}
        public void mouseClicked(MouseEvent e)
        {
        	for(int i=0;i<currentButtons;i++)
        	{
        		buttons[i].mouseClicked(e);
        	}
        }
    }
    private class TAdapter2 extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
        	if(audiomancer!=null){audiomancer.keyPressed(e);}
        	if(psychomancer!=null){psychomancer.keyPressed(e);}
        }
        public void keyReleased(KeyEvent e)
        {
        	if(audiomancer!=null){audiomancer.keyReleased(e);}
        	if(psychomancer!=null){psychomancer.keyReleased(e);}
        }
        public void keyTyped(KeyEvent e){}
    }
}

