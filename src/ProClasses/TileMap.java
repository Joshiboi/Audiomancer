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
    private int tileCount;
    private int heightTile;
    private int widthTile;
    
    //fan tile variables
    private FanTile fanTile;
    private long prevTime= System.nanoTime();
	private long passedTime;
	private int current;
	private int animFrames=8;
    public TileMap(String s, int tileSize)
    {
        this.tileSize = tileSize;
        
        try
        {
			BufferedReader br = new BufferedReader(new FileReader(s));
            
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            tileCount = Integer.parseInt(br.readLine());
            map = new int[mapHeight][mapWidth];
            
            tiles = new Image[tileCount];
            tileImages = new ImageIcon[tileCount];
            for(int i=0;i<tileCount;i++)
            {
            	tileImages[i] = new ImageIcon(this.getClass().getResource("/resources/textures/environment/tiles/tile"+(i)+".png"));
            	tiles[i] = tileImages[i].getImage();
            }
            
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
    
    public int getHeight()
    {
    	heightTile = mapHeight-1;
    	return((heightTile*tileSize)-tileSize);
    }
    public int getWidth(){return mapWidth;}
    
    public void draw(Graphics2D g)
    {
        for(int row=0;row<mapHeight;row++)
        {
            for(int col=0;col<mapWidth;col++)
            {
            	passedTime = System.nanoTime() - prevTime;
        		if(passedTime/41666666 >= 1)
                {
                    current++;
                    passedTime=0;
                    prevTime=System.nanoTime();
                }
        		if(current>=animFrames)
        		{
        			current=0;
        		}
                int rc = map[row][col];
                if(rc==4 || rc==3)
                {
                	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
                	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
                }
                else if(rc==6)
                {
                	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
                	fanTile = new FanTile(x + col * tileSize, (y + row * tileSize),current);
                	fanTile.draw(g);
                }
                else {g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);}
            }
        }
    }
}
