package Final;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Console;
import java.util.*;

import javax.xml.soap.Node;

public class BadGuy {
	
	Image myImage;
	int my_x=0,my_y=0;
	boolean hasPath=false;
	node nodes[][] = new node[40][40];
	Stack path = null;
	LinkedList openList = null;

	public BadGuy( Image i ) {
		myImage=i;
		my_x = 30;
		my_y = 10;
		path = new Stack();
		openList = new LinkedList();
		for(int x=0; x<40; x++){
			for(int y=0; y<40; y++){
				nodes[x][y] = new node();
				nodes[x][y].x = x;
				nodes[x][y].y = y;
			}
		}
	}
	
	public int manhattanDist(int x1, int y1, int x2, int y2){
		return Math.abs(x1-x2)+Math.abs(y1-y2);
	}
	
	public void reCalcPath(boolean map[][],int targx, int targy) {
		System.out.print("Pathfinding from "+my_x+","+my_y+" to "+targx+","+targy+"\n");
		
		for(int x=0; x<40; x++){
			for(int y=0; y<40; y++){
				nodes[x][y].isOpen = false;
				nodes[x][y].isClosed = map[x][y];
			}
		}
		
		openList.clear();
		path.clear();
		
		nodes[my_x][my_y].isOpen = true;
		nodes[my_x][my_y].g = 0;
		nodes[my_x][my_y].h = manhattanDist(my_x,my_y,targx,targy);
		nodes[my_x][my_y].f = nodes[my_x][my_y].g+nodes[my_x][my_y].h;
		openList.add(nodes[my_x][my_y]);
		
		boolean finished = false, givenUp = false;
		
		while(!finished && !givenUp){
			node best_node = null;
			Iterator i = openList.iterator();
			while(i.hasNext()){
				node n = (node)i.next();
				if (best_node==null || best_node.f>n.f)
					best_node = n;
			}
			
			if(best_node!=null){
				testneighbours:
					for(int xx=-1;xx<=1;xx++){
						for(int yy=-1;yy<=1;yy++){
							int xxx=best_node.x+xx, yyy=best_node.y+yy;
							if(xxx>=0 && xxx<40 && yyy>=0 && yyy<40){
								if(!nodes[xxx][yyy].isClosed && !nodes[xxx][yyy].isOpen){
									nodes[xxx][yyy].isOpen=true;
									openList.add(nodes[xxx][yyy]);
									nodes[xxx][yyy].parentx = best_node.x;
									nodes[xxx][yyy].parenty = best_node.y;
									nodes[xxx][yyy].g = 1 + best_node.g;
									nodes[xxx][yyy].h = manhattanDist(xxx,yyy,targx,targy);
									nodes[xxx][yyy].f = nodes[xxx][yyy].g + nodes[xxx][yyy].h;
									if(xxx==targx && yyy==targy){
										finished=true;
										break testneighbours;
									}
								}
							}
						}
					}
			
			best_node.isOpen = false;
			best_node.isClosed = true;
			openList.remove(best_node);
			}
			else{givenUp = true;
			System.out.print("No path found!\n");
				
			}
		}
		
		if(finished){
			System.out.print("Nowat "+my_x+","+my_y+" ..push path: ");
			int x = targx, y = targy;
			while(x!=my_x || y!=my_y){
				path.push(nodes[x][y]);
				int parentx = nodes[x][y].parentx;
				int parenty = nodes[x][y].parenty;
				System.out.print(x+","+y+" ");
				x = parentx;
				y = parenty;
			}
			System.out.print("\n");
			hasPath = true;
		}
		else
			hasPath = false;
		
	}	
	
	public void move(boolean map[][],int targx, int targy) {
		if (hasPath) {
			node nextNode = (node)path.pop();
			targx = nextNode.x;
			targy = nextNode.y;
			if(path.size()==0)
				hasPath = false;
			System.out.print("Popped "+targx+","+targy+"\n");
			my_x = targx;
			my_y = targy;			
		}
		else {
			// no path known, so just do a dumb 'run towards' behaviour
			int newx=my_x, newy=my_y;
			if (targx<my_x)
				newx--;
			else if (targx>my_x)
				newx++;
			if (targy<my_y)
				newy--;
			else if (targy>my_y)
				newy++;
			if (!map[newx][newy]) {
				my_x=newx;
				my_y=newy;
			}
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, my_x*20, my_y*20, null);
	}
	
}

