package openGLTests.main.editor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
public class Scrollbar implements MouseMotionListener, KeyListener
{
	private double x,y;
	private double diffs;
	private int mouseX, mouseY;
	private int width,height;
	private boolean selected;
	private int windowHeight;
	private int windowWidth;
	private double tileHeight;
	private double tileWidth;
	private double pixelsToBottom;
	private boolean type;
	private boolean atTop;
	private boolean atBottom;
	private boolean atLeft;
	private boolean atRight;
	private Image image;
	private ImageIcon icon;
	private double prevY, prevX;
	private boolean keyVert, keyHoriz;
	private int ySpd, xSpd;
	private long initTime;
	private boolean initTimer;
	private boolean keyLeft, keyRight, keyUp, keyDown;
	private int overlayWidth, overlayHeight;
	private double modifier;
	private double extraPos=0.0;
	private double extraNeg=0.0;
	public Scrollbar(double _tileWidth, double _tileHeight, int _windowWidth, int _windowHeight, int tileSize, boolean _type)
	{
		type=_type;
		if(type)
		{
			initType1(_tileHeight, _windowWidth, _windowHeight, tileSize);
		}
		else
		{
			initType2(_tileWidth, _windowWidth, _windowHeight, tileSize);
		}
	}
	public void update()
	{	
		if(type)
		{
			if(selected)
			{
				double tempY = y;
				y=mouseY-height/2;
				if(y<32){y=32; atTop = true;}
				else{atTop = false;}
				if(y+height>windowHeight){diffs+=extraPos; y=windowHeight-height; atBottom = true;}
				else{atBottom = false;}
				if(y!=tempY && initTimer){initTimer=false;}
				
			}
			if((System.nanoTime()/1000000000) - initTime>=10 && initTimer)
			{
				initTimer = false;
				selected = false;
			}
			diffs = y-prevY;
			
			diffs*=modifier;
			if(diffs>0)
			{
				extraPos+=(diffs-(int)diffs);
				if(extraPos>1)
				{
					diffs+=(int)extraPos;
					extraPos-=(int)extraPos;
				}
			}
			else if(diffs<0)
			{
				extraNeg+=(diffs-(int)diffs);
				if(extraNeg<-1)
				{
					diffs+=extraNeg;
					extraNeg-=(int)extraNeg;
				}
			}
			
			//System.out.println(extraPos+"    |    "+atRight);
			diffs=(int)diffs;
			prevY = y;
		}
		else
		{
			if(selected)
			{
				x=mouseX-width/2;
				if(x<32){x=32; atLeft = true;}
				else{atLeft = false;}
				if(x+width>windowWidth)
				{
					diffs+=extraPos;
					x=windowWidth-width; 
					atRight = true;
				}
				else{atRight = false;}
			}
			diffs = x-prevX;
			diffs*=modifier;
			if(diffs>0)
			{
				extraPos+=(diffs-(int)diffs);
				if(extraPos>1)
				{
					diffs+=(int)extraPos;
					extraPos-=(int)extraPos;
				}
			}
			else if(diffs<0)
			{
				extraNeg+=(diffs-(int)diffs);
				if(extraNeg<-1)
				{
					diffs+=extraNeg;
					extraNeg-=(int)extraNeg;
				}
			}
			//System.out.println(diffs);
			//System.out.println(extraPos+"    |    "+atRight);
			diffs=(int)diffs;
			prevX=x;
		}
	}
	public void render(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.drawImage(image,(int)x,(int)y,null);
	}
	public void initType1(double _tileHeight, int _windowWidth, int _windowHeight, int tileSize)
	{
		System.out.println(this.getClass().getResource(""));
		icon = new ImageIcon(this.getClass().getResource("resources/Editor/textures/editor/scrollbar/vertBar.png"));
		//icon = new ImageIcon(this.getClass().getResource("resources/textures/editor/scrollbar/vertBar.png"));
		image = icon.getImage();
		width = image.getWidth(null);
		height = image.getHeight(null)+32;
		
		windowHeight=_windowHeight-32;
		tileHeight=_tileHeight*tileSize;
		
		
		pixelsToBottom=windowHeight-height;
		y=32;
		overlayHeight=32;
		
		modifier = Math.abs(tileHeight - (windowHeight-overlayHeight))/pixelsToBottom;
		
		x=_windowWidth-width;
		ySpd = (int) (10/(Math.abs(tileHeight - windowHeight)/pixelsToBottom));
		if(ySpd<2.5){ySpd=5*(int)(Math.round(1280.0/720.0));}
	}
	
	public void initType2(double _tileWidth, int _windowWidth, int _windowHeight, int tileSize)
	{
		icon = new ImageIcon(this.getClass().getResource("resources/Editor/textures/editor/scrollbar/horizBar.png"));
		//icon = new ImageIcon(this.getClass().getResource("resources/textures/editor/scrollbar/horizBar.png"));
		image = icon.getImage();
		height = image.getHeight(null);
		
		width = image.getWidth(null)+32;
		windowWidth=_windowWidth-32;
		
		windowHeight=_windowHeight;
		tileWidth = _tileWidth*tileSize;
		
		pixelsToBottom=windowWidth-width;
		x=32;
		overlayWidth=192;
		
		modifier = Math.abs(tileWidth - (windowWidth-overlayWidth))/pixelsToBottom;
		
		y=windowHeight-height;
		xSpd = (int)(10/(Math.abs(tileWidth - windowWidth)/pixelsToBottom));
		if(xSpd<2.5){xSpd=5*(int)(Math.round(1280.0/720.0));}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void mouseDragged(MouseEvent e) 
	{
		if(e.getX()>x && e.getX()<x+width)
		{
			if(e.getY()>y && e.getY()<y+height && !keyVert)
			{
				selected=true;
				if(!initTimer)
				{
					initTime = System.nanoTime()/1000000000;
					initTimer=true;
				}
			}
		}
		if(selected)
		{
			//System.out.println("selected");
			mouseX=e.getX();
			mouseY=e.getY();
		}
	}
	public void mouseMoved(MouseEvent e) 
	{
		selected=false;
	}
	public boolean isSelected(){return selected;}
	public void setSelected(boolean i){selected=i;}
	public double getDiffs(){return diffs;}
	public double getY(){return y;}
	public boolean isAtBottom(){return atBottom;}
	public boolean isAtTop(){return atTop;}
	public boolean isAtRight(){return atRight;}
	public boolean isAtLeft(){return atLeft;}
	public boolean isKeyVert(){return keyVert;}
	public boolean isKeyHoriz(){return keyHoriz;}
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP && type)
		{
			keyVert = true;
			y-=ySpd;
			if(y<32){y=32; atTop=true;}
			else{atTop=false;}
			keyUp = true;
			keyDown = false;
			atBottom=false;
		}
		if(key == KeyEvent.VK_DOWN && type || keyDown)
		{
			y+=ySpd;
			if(y+height>=windowHeight){y = windowHeight-height; atBottom=true;}
			else{atBottom = false;}
			atTop=false;
			keyDown = true;
			keyUp = false;
			keyVert = true;
		}
		if(key == KeyEvent.VK_LEFT && !type || keyLeft)
		{
			x-=xSpd;
			if(x<32){x=32; atLeft=true;}
			else{atLeft=false;}
			atRight=false;
			keyLeft = true;
			keyRight = false;
			keyHoriz=true;
		}
		if(key == KeyEvent.VK_RIGHT && !type || keyRight)
		{
			x+=xSpd;
			if(x+width>=windowWidth){x = windowWidth-width; atRight=true;}
			else{atRight=false;}
			atLeft=false;
			keyRight = true;
			keyLeft = false;
			keyHoriz=true;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_UP){keyUp = false;}
		if(key == KeyEvent.VK_DOWN){keyDown = false;}
		if(!keyUp && !keyDown){keyVert = false;}
		if(key == KeyEvent.VK_RIGHT){keyRight = false;}
		if(key == KeyEvent.VK_LEFT){keyLeft = false;}
		if(!keyLeft && !keyRight){keyHoriz = false;}
	}
	public void keyTyped(KeyEvent e) {
		
	}
}
