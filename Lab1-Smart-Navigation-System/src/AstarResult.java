import java.util.ArrayList;
import javafx.util.Pair;

public class AstarResult {
	public ArrayList<double []> route;
	public int id;
	public double dist;
	public double x;
	public double y;

	public AstarResult (ArrayList<double []> path, double d, int num, double x, double y) {
		this.route = new ArrayList<>(path);
		this.id = num;
		this.dist = d;
		this.x = x;
		this.y = y;
	}
}
