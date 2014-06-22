package ProClasses;

import java.awt.*;
import javax.swing.*;
public class FanTile {
	private Image[] images;
	private ImageIcon[] imageIcons;
	private int x;
	private int y;
	private int animFrames;
	private long passedTime;
	private int current;
	private long prevTime;
	public FanTile(int _x, int _y, int _current) 
	{
		current=_current;
		animFrames = 8;
		images = new Image[animFrames];
		imageIcons = new ImageIcon[animFrames];
		for(int i=0;i<animFrames;i++)
		{
			imageIcons[i] = new ImageIcon(this.getClass().getResource("/resources/textures/environment/tiles/Fan/fan_"+(i+1)+".png"));
			images[i] = imageIcons[i].getImage();
		}
		x=_x;
		y=_y;
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		passedTime = System.nanoTime() - prevTime;
		if(passedTime/83333333 >= 1)
        {
            current++;
            passedTime=0;
            prevTime=System.nanoTime();
        }
		if(current>=animFrames)
		{
			current=0;
		}
		g2d.drawImage(images[current], x,y,null);
	}

}
