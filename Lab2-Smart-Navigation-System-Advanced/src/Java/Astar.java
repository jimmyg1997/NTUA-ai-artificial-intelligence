import java.io.IOException;
import java.util.ArrayList;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;

public class Astar {
	int max_size;

	public AstarResult AstarTaxis (double taxi_x, double taxi_y, int taxi_id, NodesParser nodes_map) throws JIPSyntaxErrorException, IOException {
		this.max_size = 100;
		AstarSearch as = new AstarSearch();
		Point a = as.Astar(nodes_map, taxi_id, taxi_x, taxi_y, this.max_size);
		ArrayList<Location> path = new ArrayList<Location>();
		path = this.find_path(a);
		AstarResult temp = new AstarResult (path, a.distance_so_far, taxi_id, taxi_x, taxi_y);
		return temp;
	}

	public ArrayList <Location> find_path (Point end) {
		ArrayList <Location> path = new ArrayList<>();
        Location temp;
		while (end != null) {
			temp = new Location (end.x, end.y);
			path.add(0, temp);
			end = end.father;
		}
		return path;
	}
}
