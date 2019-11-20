public class Point {
	public double x;
	public double y;
	Point father;
	double distance_so_far;
	double fscore;

	public Point (double x, double y) {
        this.x = x;
        this.y = y;
        this.father = null;
        this.fscore = Double.POSITIVE_INFINITY;
        this.distance_so_far = Double.POSITIVE_INFINITY;
    }

	public double distance(double x2, double y2) {
	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(x2 - this.x);
	    double lonDistance = Math.toRadians(y2 - this.y);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	    		+ Math.cos(Math.toRadians(this.x)) * Math.cos(Math.toRadians(x2))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    return distance;
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
