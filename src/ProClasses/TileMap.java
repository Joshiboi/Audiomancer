package ProClasses;

import java.io.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class TileMap
{
    private int x,y;
    private Image[] tiles;
    private ImageIcon[] tileImages;
    private int tileSize;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private int tileCount=7;
    
    public TileMap(String s, int tileSize)
    {
        this.tileSize = tileSize;
        
        tiles = new Image[tileCount];
        tileImages = new ImageIcon[tileCount];
        for(int i=0;i<tileCount;i++)
        {
        	tileImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/environment/tiles/tile"+(i)+".png"));
        	tiles[i] = tileImages[i].getImage();
        }
        
        try
        {
			BufferedReader br = new BufferedReader(new FileReader(s));
            
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            map = new int[mapHeight][mapWidth];
            
            String delimiters = " ";
            for(int row=0;row<mapHeight;row++)
            {
                String line = br.readLine();
                String[] tokens = line.split(delimiters);
                for(int col=0;col<mapWidth;col++)
                {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
            br.close();
        }
        catch(Exception e){System.out.println("exception caught: "+e);}
    }
    
    public int getColTile(int x)
    {
        return x / tileSize;
    }
    public int getRowTile(int y)
    {
        return y / tileSize;
    }
    public int getTile(int row, int col)
    {
        return map[row][col];
    }
    
    public int getTileSize()
    {
        return tileSize;
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
    public void setY(int i){y=i;}
    public void setX(int i){x=i;}
    
    public void draw(Graphics2D g)
    {
        for(int row=0;row<mapHeight;row++)
        {
            for(int col=0;col<mapWidth;col++)
            {
                int rc = map[row][col];
                
                if(rc==0)
                {
                    g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                if(rc==1)
                {
                	g.drawImage(tiles[rc],x + col * tileSize, y + row * tileSize,null);
                }
                if(rc==2)
                {
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                if(rc==3)
                {
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                if(rc==4)
                {
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                if(rc==5)
                {
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                if(rc==6)
                {
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
            }
        }
    }
}
