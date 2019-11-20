/*Point class: A general class to store the coordinates, heuristic and real
distance of its objects. More specifically, heuristic distance is the distance
needed to reach the client's node and real distance is the distance covered so
far.
Note: Used an already implemented distance functions so that Earth's curvature is
taken into consideration in our simulation. The heuristic function is implemented
as the Euclidean Distance between current node and client's node.*/
import java.util.*;
public class Point {
	public double x;
	public double y;
	public int id;
	ArrayList<Point> previousPoint;
	double distanceCovered;
	double distanceNeeded;

	public double round (double value, int precision) {
	    double scale =  Math.pow(10, precision);
	    return  Math.round(value * scale) / scale;
	}

	public void reset(){
		this.previousPoint.clear();
		
        this.distanceNeeded = Double.POSITIVE_INFINITY;
        this.distanceCovered = Double.POSITIVE_INFINITY;
       
	}

	public Point (double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.previousPoint = new ArrayList<Point>();
        this.distanceNeeded = Double.POSITIVE_INFINITY;
        this.distanceCovered = Double.POSITIVE_INFINITY;
        
    }

	public double distance(Point otherPoint) {
	    final int R = 6371; // Radius of the earth
	    
	    double latDistance = Math.toRadians(otherPoint.x - this.x);
	    double lonDistance = Math.toRadians(otherPoint.y - this.y);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(this.x)) * Math.cos(Math.toRadians(otherPoint.x))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // Convert to meters.
	    return this.round(distance, 1);
	}

	public double heuristic(double client_x, double client_y) {
	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(client_x - this.x);
	    double lonDistance = Math.toRadians(client_y - this.y);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	    		+ Math.cos(Math.toRadians(this.x)) * Math.cos(Math.toRadians(client_x))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	   	return this.round(distance, 1);
	}


	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;

        Point other = (Point)o;
        if (this == other)
            return true;

        return (this.x == other.x && this.y == other.y);
    }

	@Override
	 public int hashCode() {
        return (int)(10000000*this.x + 10000000*this.y);
	}

}
