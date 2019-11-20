import java.util.*;
import java.io.*;
import java.lang.*;

public class Astar{
	double clientX;
  	double clientY;

	public ArrayList<AstarResult> AstarTaxis (TaxiLocation taxis, NodeLocation nodesMap, ClientLocation clientCoordinates) {
		this.clientX = clientCoordinates.x;
    	this.clientY = clientCoordinates.y;
		AstarSearch as = new AstarSearch();
		/*We find the closest tour our client node that this taxi can reach */
		int differentPaths = 0;
		Point start  = new Point (taxis.startingNode.x, taxis.startingNode.y,0);
		Point point = as.Astar(nodesMap, taxis, clientCoordinates);
		ArrayList<Point> nextPoint = new ArrayList<>();
		ArrayList<AstarResult> results = new ArrayList<AstarResult>();


		PrintWriter writer = null;
        try{
            
            writer = new PrintWriter("AllPossiblePath"+taxis.id + ".txt");
			differentPaths = this.findPaths(point,start,writer,nextPoint,0);
			writer.close();

			BufferedReader reader = null;
            try{
                    reader = new BufferedReader(new FileReader("AllPossiblePath"+taxis.id + ".txt"));
                    String line = null;
                    ArrayList <double[] > path = new ArrayList<>();
                    System.out.println("For the taxi with id " + taxis.id +" ,we have "+ differentPaths+ " different paths");

                    try{
                    	while ((line = reader.readLine()) != null){


	                    	if(line.equals("100000000")){

	                    	
	                    		System.out.println("id " + taxis.id + ", path size: "+path.size()+ ", distance: " +  point.distanceCovered + "m");
	                    		AstarResult result = new AstarResult (path, point.distanceCovered, taxis.id, taxis.x, taxis.y);

	                    		results.add(result);
	                    		path.clear();

	                    	}
	                    	else{
	                    		String[] coordinates = line.split(" ");
								
		                    	double x = Double.parseDouble(coordinates[0]);
		                    	double y = Double.parseDouble(coordinates[1]);
		                    	path.add(new double[] {x,y});

	                    	}
						
	                    }
	                    System.out.println(" ");
	                    reader.close();

                    }catch(IOException e){
                    	System.out.println("Input/Output");
                    }
            
		    } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } 
		} catch(FileNotFoundException e){

            e.printStackTrace();
        }
		return results;
	}
	
	public int findPaths (Point end, Point start,PrintWriter writer, ArrayList<Point> nextPoint,int differentPaths) {

		if(end.x == start.x && end.y == start.y){
			
			
			for (Point i : nextPoint){
                writer.println(i.x + " " + i.y);
            }
            writer.println("100000000");
            differentPaths = differentPaths + 1;
            return differentPaths;
			
			
		}
		else{
			ArrayList<Point> parents = end.previousPoint;
			while(!parents.isEmpty()){
				Point temp = parents.get(0);
				parents.remove(0);
				
				nextPoint.add(temp);
				differentPaths = findPaths(temp,start,writer,nextPoint,differentPaths);
				nextPoint.remove(temp);
				
			}
			return differentPaths;
		}
	}
}
