import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AstarSearch {
    public double round (double value, int precision) {
        double scale = (int) Math.pow(10, precision);
        return  Math.round(value * scale) / scale;
    }


	public Point Astar (NodeLocation nodesMap, TaxiLocation taxi, ClientLocation client) {

        /*Necessary initiliazations*/
		ArrayList <Point> frontier = new ArrayList<>();
		ArrayList <Point> close = new ArrayList<Point>();
        ArrayList <Point> children = new ArrayList<>();
        Point current, end, start, clientCoordinates;
        int maxSizeNeeded = 0;

        /*Finding ending and starting nodes that are the closest to the client and the examining taxi respectively*/
        end = new Point (client.endingNode.x, client.endingNode.y,0);
        start = new Point (taxi.startingNode.x, taxi.startingNode.y,0);
        clientCoordinates = new Point (client.x, client.y,0);
        
        start.distanceCovered = 0;
        start.distanceNeeded = this.round(start.distance(clientCoordinates),1); //Math.floor(start.distance(clientCoordinates));
        current = start;

       
        frontier.clear();
        close.clear();
        frontier.add(start);
        int counter = 0;

        while (!frontier.isEmpty()) {
        	current = frontier.get(0);
            current.distanceCovered = this.round(current.distanceCovered ,1); 

            /*If the node examined is the client's node, stop*/
        	if (current.x == end.x && current.y == end.y) break;
            

            /*Else remove the node from the frontier set and move it to the close set*/
            /*and start examining all its children*/
        	close.add(current);
        	frontier.remove(0);
            
        	children = nodesMap.hashMap.get(current);
        	for (Point i : children) {
                
                i.distanceCovered = this.round(i.distanceCovered ,1);

                /*If the child is in close set we don't examine it*/
        		if (close.contains(i)) continue;
                else{

                    /*If it isn't in the frontier set, we currently added in the frontier set*/
                    if (!frontier.contains(i)) {
                        
                        i.previousPoint.add(current);
                        i.distanceCovered = current.distanceCovered + i.distance(current);
                        i.distanceNeeded = i.distanceCovered + i.heuristic(client.x,client.y);
                        frontier.add(i);
                        if (frontier.size() > maxSizeNeeded) maxSizeNeeded = frontier.size();
                        
                    }
                    else{
                        /*If it is in the frontier set , examine if the new sum is less than sum stored 
                        and in that occasion update the heuristic and real distance variables*/
                        if ( current.distanceCovered + i.distance(current) < i.distanceCovered ){
                            i.previousPoint.clear();
                            i.previousPoint.add(current);
                            i.distanceCovered = current.distanceCovered + i.distance(current);
                            i.distanceNeeded = i.distanceCovered + i.heuristic(client.x,client.y);
                           
                        }
                        else if ( current.distanceCovered + i.distance(current) == i.distanceCovered ){
                            counter = counter + 1;
                            i.previousPoint.add(current);

                        }
                    }
                }
        	}

            /*Sorting the frontier so that we will pick the node with least distance Needed in the next loop
            Key Note:Sorting each time is not an expensive operation since the frontier will maximely 
            include 1000 nodes*/
        	Collections.sort(frontier, new Comparator<Point>(){
        	   public int compare(Point point1, Point point2){
        	         if((point1.distanceCovered +point1.distanceNeeded) == (point2.distanceCovered+point2.distanceNeeded ))
        	             return 0;
        	         return (point1.distanceCovered+point1.distanceNeeded ) < (point2.distanceCovered+point2.distanceNeeded)? -1 : 1;
        	     }
        	});
            

        }
        System.out.println("counter " + counter);
        System.out.println("Max Size of frontier set that reached sometime:  " + maxSizeNeeded);
        return current;
    }
}
