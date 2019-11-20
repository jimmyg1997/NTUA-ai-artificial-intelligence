/*NodeLocation Class: For each point of the street we create
an arraylist with its neighbours(points). We also find the
closest point to the client from which the taxi will pick
him up.  */
import java.util.*;
import java.io.*;


public class NodeLocation{
	ArrayList  <Point> points = new ArrayList<>();
	HashMap <Point, ArrayList<Point>> hashMap = new HashMap<>(); //For each Point we store its neighbours with same ID.

	public void reset (HashMap<Point, ArrayList<Point>> map) {

		for ( Point key :  map.keySet()) {
    		key.reset();
		}
		
	}

	public void parser (String nodesFile) {
		String line = "";
		BufferedReader reader = null;
		String [] temp = new String[3];
		Point previous, current, node;

		int previousID, currentID;

		try{
			reader = new BufferedReader(new FileReader(nodesFile));
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		}

	    try{
	    	line = reader.readLine(); //Ignore first line of the .csv (title-label line)
	    	line = reader.readLine(); //Reading coordinates/ID/name of the first node.
	    	temp = line.split(",");
	  		node = new Point(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), Integer.parseInt(temp[2]));
	      	this.points.add(node);

      		previous = node;
    		previousID = node.id;

      		this.hashMap.put(previous, new ArrayList<>());

			while ((line = reader.readLine()) != null) {
				temp = line.split(",");
				node = new Point(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), Integer.parseInt(temp[2]));
	      		this.points.add(node);

	     		current = node;
	      		currentID = node.id;

				if (previousID == currentID) {
					this.hashMap.get(previous).add(current);
					if (!this.hashMap.containsKey(current)){
            			this.hashMap.put(current, new ArrayList<>());
          			}
					this.hashMap.get(current).add(previous);
				}
				else {
					if (!this.hashMap.containsKey(current)){
            			this.hashMap.put(current, new ArrayList<>());
          			}
				}
				previous = current;
				previousID = currentID;
			}

			System.out.println ("number of nodes: " + this.points.size());
			System.out.println("size of hashmap: " + this.hashMap.size());
			System.out.println ("number of crossings: " + (this.points.size() - this.hashMap.size()));
		} catch (IOException e){
			e.printStackTrace();
	  }
	  return;
	}

}
