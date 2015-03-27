package openGLTests.main;
import java.io.*;
import java.util.ArrayList;

public class TileMap
{
	private int x,y;
	private ArrayList<Tile> tiles;

	private int tileSize;
	private int[][] map;

	private int mapWidth;
	private int mapHeight;
	private int tileCount;
	private int playerSpawnX;
	private int playerSpawnY;
	
	private int maxTiles;
	private int xSpd, ySpd;
	private ArrayList<Integer> coordX, coordY;
	private ArrayList<Integer> fanX, fanY;
	private ArrayList<Integer> fanIDs;
	public TileMap (String s, int tileSize)
	{
		tiles = new ArrayList<Tile>();
		loadData("resources/testMap.txt");
		coordX = new ArrayList<Integer>();
		coordY = new ArrayList<Integer>();
		fanX = new ArrayList<Integer>();
		fanY = new ArrayList<Integer>();
		fanIDs = new ArrayList<Integer>();
		this.tileSize = tileSize;
		for(int i=0;i<tiles.size();i++)
		{
			if(tiles.get(i).getID()==99)
			{
				playerSpawnX= (int)tiles.get(i).getX();
				playerSpawnY= (int)tiles.get(i).getY();
				System.out.println("("+playerSpawnX+","+playerSpawnY+")");
			}
		}
	}

	public void loadData(String fileName)
	{
		System.out.println("loading");
		int x=0;
		int y=0;
		tiles = new ArrayList<Tile>();
		try 
		{
			BufferedReader s = new BufferedReader(new FileReader(fileName));
			mapWidth = Integer.parseInt(s.readLine());
			mapHeight = Integer.parseInt(s.readLine());
			tileCount = Integer.parseInt(s.readLine());
			maxTiles = (mapWidth*mapHeight)+1;
			String delimiters = " ";
			for(int i=0;i<mapHeight;i++)
			{
				for(int j=0;j<mapWidth;j++)
				{
					String raw = s.readLine();
					if(raw!=null)
					{
						String[] parsed = raw.split(delimiters);
						for(int k=0;k<parsed.length;k++)
						{
							int tileID=0;
							int backgroundTileID=-1;
							char[] chars = parsed[k].toCharArray();
							if(parsed[k].contains(","))
							{
								String[] newTileString = parsed[k].split(",");
								tileID=Integer.parseInt(newTileString[0]);
								backgroundTileID=Integer.parseInt(newTileString[1]);

								tiles.add(new Tile(x,y,"tile"+tileID,backgroundTileID));
								tiles.add(new Tile(x,y,"tile"+backgroundTileID,-1));
							}
							else if(chars.length>2 && Character.getNumericValue(chars[0])==1 && Character.getNumericValue(chars[1])==1)
							{
								Tile fan = new Tile(x,y,"tile"+11,backgroundTileID);
								String val = "";
								int index=3;
								for(int l=3,stop=chars.length;l<stop;l++,index++)
								{
									if(chars[l]=='-'){break;}
									else{val+=chars[l];}
								}
								fan.setVelocity(Integer.parseInt(val));
								val="";
								for(int l=index+1,stop=chars.length;l<stop;l++)
								{
									if(chars[l]=='}'){break;}
									else{val+=chars[l];}
								}
								fan.setFanHeight(Integer.parseInt(val));
								tiles.add(fan);
							}
							else
							{
								tileID=Integer.parseInt(parsed[k]);
								tiles.add(new Tile(x,y,"tile"+tileID,backgroundTileID));
							}
							x+=32;
						}
					}
					x=0;
					y+=32;
				}
			}
			s.close();
		} 
		catch (Exception e){e.printStackTrace();}
		System.out.println("loading complete");
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
		return(mapHeight*32)-tileSize*2;
	}
	public int getWidth(){return mapWidth;}
	public int getPlayerSpawnX(){return playerSpawnX;}
	public int getPlayerSpawnY(){return playerSpawnY;}

	public void setSpds(int xSpd, int ySpd)
	{
		this.xSpd=xSpd;
		this.ySpd=ySpd;
	}
	public Tile getTile(int id){return tiles.get(id);}
	public ArrayList<Integer> getCollidableXCoords(){return coordX;}
	public ArrayList<Integer> getCollidableYCoords(){return coordY;}
	
	public ArrayList<Integer> getFanIDs(){return fanIDs;}
	
	public ArrayList<Integer> getFanXCoords(){return fanX;}
	public ArrayList<Integer> getFanYCoords(){return fanY;}

	public void update(int x, int y)
	{
		coordX = new ArrayList<Integer>();
		coordY = new ArrayList<Integer>();
		fanX = new ArrayList<Integer>();
		fanY = new ArrayList<Integer>();
		fanIDs = new ArrayList<Integer>();
		int xDiffs=this.x;
		this.x=x;
		xDiffs-=this.x;
		
		int yDiffs=this.y;
		this.y=y;
		yDiffs-=this.y;
		
		for(int i=0,stop=tiles.size();i<stop;i++)
		{
			tiles.get(i).update(xDiffs,yDiffs);
			if(tiles.get(i).isCollidable())
			{
				coordX.add((int)tiles.get(i).getX()); 
				coordY.add((int)tiles.get(i).getY());
			}
			else if(tiles.get(i).isFan())
			{
				fanX.add((int) tiles.get(i).getX()); 
				fanY.add((int) tiles.get(i).getY()); 
				fanIDs.add(i);
			}
		}
	}
	public void render()
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0,stop=tiles.size();i<stop;i++)
		{
			if(tiles.get(i).getY()+32<720+32 && tiles.get(i).getX()+32<1280+32 && tiles.get(i).getY()>-32 && tiles.get(i).getX()>-32)
			{
				tiles.get(i).render();
				if(tiles.get(i).isOverlayable())
				{
					indexes.add(i);
				}
			}
		}
		for(int i=0;i<indexes.size();i++)
		{
			tiles.get(indexes.get(i)).render();
		}
	}
}
