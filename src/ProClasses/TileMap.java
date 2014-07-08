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
   private int playerSpawnX;
   private int playerSpawnY;
   private int renderXLow, renderXHigh, renderYLow, renderYHigh;
   
   //fan tile variables
   private FanTile fanTile;
   private long prevTimeFan=System.nanoTime();
	private long passedTimeFan;
	private int currentFan;
	private int animFramesFan=8;
	
	//door tile variables
	private DoorTile doorTile;
	private long prevTimeDoor = System.nanoTime();
	private long passedTimeDoor;
	private int currentDoor;
	private boolean doorState;
	private int animFramesDoor=11;
	
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
   public int getPlayerSpawnX(){return playerSpawnX;}
   public int getPlayerSpawnY(){return playerSpawnY;}
   public int getHeight()
   {
   	heightTile = mapHeight-1;
   	return((heightTile*tileSize)-tileSize);
   }
   public int getWidth(){return mapWidth;}
   public void setDoorState(boolean i){doorState = i;}
   public void setRenderBounds(int aX, int aY, int w, int h)
   {
	   int tempx = x-(w/2)+aX;
	   int tempy = y-(h/2)+aY;
	   
	   renderXLow = tempx-(w/2);
	   renderXHigh = tempx+(w);
	   renderYLow = tempy-(h/2);
	   renderYHigh = tempy+(h);
   }
   public void update()
   {
   	passedTimeFan = System.nanoTime() - prevTimeFan;
   	if(passedTimeFan/41666666 >= 1)
   	{
   		currentFan++;
   		passedTimeFan=0;
   		prevTimeFan=System.nanoTime();
   	}
   	if(currentFan>=animFramesFan)
   	{
   		currentFan=0;
   	}
   	if(doorState)
   	{
   		passedTimeDoor = System.nanoTime() - prevTimeDoor;
   		if(passedTimeDoor/10000000 >=1)
   		{
   			currentDoor++;
   			passedTimeDoor=0;
   			prevTimeDoor=System.nanoTime();
   		}
   	}

   	if(currentDoor>=animFramesDoor)
   	{
   		currentDoor=11;
   	}
   }
   
   public void draw(final Graphics2D g)
   {
       for(int row=0;row<mapHeight;row++)
       {
           for(int col=0;col<mapWidth;col++)
           {
        	   int rc = map[row][col];
        	   int tileXL = (x+col*tileSize)-tileSize;
        	   int tileXR = x+col*tileSize;
        	   int tileYU = (y+row*tileSize)-tileSize;
        	   int tileYD = y+row*tileSize;
        	   
        	   if(rc==99)
               {
               	playerSpawnX = col*tileSize;
               	playerSpawnY = row*tileSize;
               }
        	   
        	   if(tileXL<renderXLow || tileXR>renderXHigh || tileYU<renderYLow || tileYD>renderYHigh){}
        	   else
        	   {
	               if(rc==4 || rc==3)
	               {
	               	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
	               	g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
	               }
	               else if(rc==6)
	               {
	               	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
	               	fanTile = new FanTile(x + col * tileSize, (y + row * tileSize),currentFan);
	               	fanTile.draw(g);
	               }
	               else if(rc==8)
	               {
	               	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
	               	doorTile = new DoorTile(x + col * tileSize, (y + row * tileSize),currentDoor,doorState);
	               	doorTile.draw(g);
	               }
	               else if(rc==99)
	               {
	               	g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);
	               }
	               else 
	               {
	                   try
	                   {
	                       g.drawImage(tiles[rc], x + col * tileSize, (y + row * tileSize),null);
	                   }
	                   catch(Exception e){g.drawImage(tiles[1], x + col * tileSize, (y + row * tileSize),null);}
	               }
        	   }
           }
       }
   }
}

