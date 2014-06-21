package ProClasses;

import java.awt.*;
import javax.swing.*;

public class MainMenu extends JPanel {
	
	private static final long serialVersionUID = 276265534354651264L;
	
	private int x;
	private int y;
	private int width;
	private int height;
	private Image image;
	private ImageIcon ii;

	public MainMenu() 
	{
		ii = new ImageIcon(this.getClass().getResource("/resources/textures/menus/mainMenu.png"));
		image = ii.getImage();
		x=0;
		y=0;
		width = image.getWidth(null);
		height = image.getHeight(null);
		
	}
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x,y,this);
	}
	
	
	
	public int getX(){return x;}
	public int getY(){return y;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public void setX(int i){x=i;}
	public void setY(int i){y=i;}
	

}
