package openGLTests.main.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
public class Overlay implements MouseMotionListener
{
	private Image image;
	private ImageIcon icon;
	private int x,y;
	private int width,height;
	private ArrayList<OverlayTile> tiles;
	private int availTiles;
	private int curID;
	private int tileX, tileY;
	public Overlay()
	{
		System.out.println(this.getClass().getResource(""));
		//icon = new ImageIcon(this.getClass().getResource("resources/textures/editor/frame/frame.png"));

		icon = new ImageIcon(this.getClass().getResource("resources/Editor/textures/editor/frame/frame.png"));
		image = icon.getImage();
		x=0;
		y=0;
		curID=-1;
		width=image.getWidth(null);
		height=image.getHeight(null);
		tiles = new ArrayList<OverlayTile>();
		availTiles=12;
		int index=0;
		for(int i=0;i<availTiles;i++)
		{
			tiles.add(new OverlayTile(14,(44*i)+78,"tile_"+(i)+".png"));
			index++;
		}
		tiles.add(new OverlayTile(14,(44*index)+78,"tile_99.png") );
	}
	public void update()
	{
		for(int i=0,stop=tiles.size();i<stop;i++)
		{
			if(tiles.get(i).isSelected())
			{
				curID=tiles.get(i).getID();
				tileX = tiles.get(i).getX();
				tileY = tiles.get(i).getY();
				break;
			}
		}
	}
	public void render(Graphics2D g)
	{
		g.drawImage(image,x,y,null);
		for(int i=0,stop=tiles.size();i<stop;i++)
		{
			tiles.get(i).render(g);
		}
	}
	
	public void resetSelection()
	{
		for(int i=0,stop=tiles.size();i<stop;i++)
		{
			tiles.get(i).setSelected(false);
		}
		curID=-1;
	}
	
	public int getTileX(){return tileX;}
	public int getTileY(){return tileY;}
	public int getSelectedID(){return curID;}
	public ArrayList<OverlayTile> getTiles(){return tiles;}
	
	
	public void mouseMoved(MouseEvent e)
	{
		
	}
	public void mouseDragged(MouseEvent e)
	{
		
	}
}
