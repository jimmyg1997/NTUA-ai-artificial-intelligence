public class TaxiLocation {
	public double x;
	public double y;
	public int id;
	public Point startingNode;

	public double round (double value, int precision) {
	    double scale = (int) Math.pow(10, precision);
	    return  Math.round(value * scale) / scale;
	}


	public TaxiLocation (String [] temp, NodeLocation nodes) {
		this.x = Double.parseDouble(temp[0]);
		this.y = Double.parseDouble(temp[1]);
		this.id = Integer.parseInt(temp[2]);
		this.startingNode = findStartingNode(nodes);
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

	public Point findStartingNode (NodeLocation nodes) {
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
		System.out.println("distance between a taxi and its nearest node: " + minDistance);
		return minPoint;
	}
}
