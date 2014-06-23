package ProClasses;

import java.awt.*;
import javax.swing.*;
public class DoorTile {
	private Image[] images;
	private ImageIcon[] imageIcons;
	private int x;
	private int y;
	private int animFrames;
	private long passedTime;
	private int current;
	private long prevTime;
	private boolean open;
	private boolean opening;
	public DoorTile(int _x, int _y, int _current, boolean _opening) 
	{
		opening = _opening;
		current=_current;
		animFrames = 12;
		images = new Image[animFrames];
		imageIcons = new ImageIcon[animFrames];
		for(int i=0;i<animFrames;i++)
		{
			imageIcons[i] = new ImageIcon(this.getClass().getResource("/resources/textures/environment/tiles/Door/door_"+i+".png"));
			images[i] = imageIcons[i].getImage();
		}
		if(current==11){open=true;}
		else{open=false;}
		x=_x;
		y=_y;
	}
	
	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if(opening){g2d.drawImage(images[current], x,y,null);}
		
		else if(open){g2d.drawImage(images[11], x,y,null);}
		
		else{g2d.drawImage(images[0], x,y,null);}
	}
	public boolean isOpen(){return open;}
	public void setOpen(boolean i){open=i;}
	public void setOpening(boolean i){opening=i;}
}
