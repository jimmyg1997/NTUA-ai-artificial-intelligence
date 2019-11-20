/*ClientLocation class: Reads Client's Coordinates from a .csv file by also
 catching the relevant exceptions if there are any (FileNotFound Exception,
 IO Exception). Use of FileReader() to read the proper .csv file, readLine()
 to separately parse in every line and split() to separate the two coordinates. */

import java.io.*;

public class ClientLocation{
  public double x;
  public double y;
  public Point endingNode;

  public double round (double value, int precision) {
      double scale =  Math.pow(10, precision);
      return  Math.round(value * scale) / scale;
  }


  public ClientLocation (String temp, NodeLocation nodes) {
    parser(temp);
    this.endingNode = findEndingNode(nodes);
  }

  public void parser (String clientCoordinates){
		String line = "";
		BufferedReader reader = null;
		String [] temp = new String[2];

    try{
	    reader = new BufferedReader(new FileReader(clientCoordinates));
    } catch(FileNotFoundException e){
      e.printStackTrace();
    }

    try{
    	line = reader.readLine();
  		while ((line = reader.readLine()) != null){
    		temp = line.split(",");
  			this.x = Double.parseDouble(temp[0]);
  			this.y = Double.parseDouble(temp[1]);
  		}
    } catch(IOException e){
      e.printStackTrace();
    }


    return;
	}

  public double distance(Point otherPoint) {
      final int R = 6371; // Radius of the earth
      double latDistance = Math.toRadians(otherPoint.x - this.x);
      double lonDistance = Math.toRadians(otherPoint.y - this.y);
      double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
          + Math.cos(Math.toRadians(this.x)) * Math.cos(Math.toRadians(otherPoint.x))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      double distance = R * c * 1000; // convert to meters
      return this.round(distance, 1);
  }


  public Point findEndingNode (NodeLocation nodes) {
    Point minPoint;
    double minDistance, distance;

    minPoint = nodes.hashMap.keySet().iterator().next();
    minDistance = this.distance(minPoint);

    for (Point x : nodes.hashMap.keySet()) {
      distance = this.distance(x);
      if (distance < minDistance) {
        minDistance = distance;
        minPoint = x;
      }
    }
    System.out.println("\n" + "distance between client and nearest node: " + minDistance);
    return minPoint;
  }


}
