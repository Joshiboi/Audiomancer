package ProClasses;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Button extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1824049778630639968L;
	private Image unpressed;
	private Image pressed;
	
	private int x;
	private int y;
	private int width;
	private int height;
	private int mouseX;
	private int mouseY;
	private long prevTime;
	private boolean depress;
	private boolean hasPressed;
	
	public Button(int _x, int _y)
	{
		ImageIcon ii_unpressed = new ImageIcon(this.getClass().getResource("/resources/textures/menus/buttons/Button.png"));
		ImageIcon ii_pressed = new ImageIcon(this.getClass().getResource("/resources/textures/menus/buttons/Button_depresses.png"));
		unpressed = ii_unpressed.getImage();
		pressed = ii_pressed.getImage();
		
		x=_x;
		y=_y;
		
		width=unpressed.getWidth(null);
		height=unpressed.getHeight(null);
	}
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if(!depress)
		{
			g2d.drawImage(unpressed,x,y,this);
		}
		else{pressButton(g);}
	}
	public void pressButton(Graphics g)
	{
		depress=true;
		boolean unpress = false;
		long passedTime;
		Graphics2D g2d = (Graphics2D) g;
		passedTime = System.nanoTime() - prevTime;
        if(passedTime/250000000 >= 1)
        {
        	System.out.println("one second passed");
        	prevTime = System.nanoTime();
        	unpress=true;
        	depress=false;
        	hasPressed=true;
        }
		if(!unpress){g2d.drawImage(pressed,x,y,this);}
		else{g2d.drawImage(unpressed,x,y,this);}
	}
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		depress = getMouseColliding(mouseX, mouseY);
		prevTime = System.nanoTime();
	}
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		depress = getMouseColliding(mouseX, mouseY);
		prevTime = System.nanoTime();
	}
	
	public boolean getMouseColliding(int _x, int _y)
	{
		System.out.println("checking collisions "+x+" "+mouseX+" "+(x+width));
		if(mouseX>x && mouseX<x+width)
		{
			System.out.println("mouse X within boundaries");
			if(mouseY>y && mouseY<y+height)
			{
				System.out.println("mouseY within boundaries");
				return true;
			}
		}
		return false;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getMouseX(){return mouseX;}
	public int getMouseY(){return mouseY;}
	public boolean getHasPressed(){return hasPressed;}
	public boolean getDepressed(){return depress;}
	
	public void setDepressed(boolean i){depress=i;}
	public void setX(int i){x=i;}
	public void setY(int i){y=i;}
	public void setMouseX(int i){mouseX=i;}
	public void setMouseY(int i){mouseY=i;}

}
