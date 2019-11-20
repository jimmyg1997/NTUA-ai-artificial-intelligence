import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;

public class AstarSearch {
	public Point Astar (NodesParser nodes_map, int taxi_id, double taxi_x, double taxi_y, int size) throws JIPSyntaxErrorException, IOException{
		ArrayList <Point> queue = new ArrayList<>();
		ArrayList <Point> close = new ArrayList <Point> ();

        int max_size_needed = 0;
        JIPEngine jip = new JIPEngine();
        JIPEngine cl = new JIPEngine();
        JIPEngine jip3 = new JIPEngine();

		jip.consultFile("nodes.pl");
		JIPTermParser parser = jip.getTermParser();
		JIPTermParser parser_cl = cl.getTermParser();
		JIPTermParser parser3 = cl.getTermParser();

		jip3.consultFile("lines.pl");

		cl.consultFile("client.pl");
		JIPQuery jipQuery, jipQuery2, jipQuery3;
		JIPTerm term, term2, term3;

		double client_x = 0, client_y = 0;
		double x1 = 0 , y1 = 0;
        double id = 0, id2 = 0;

        Point end = new Point (nodes_map.endx, nodes_map.endy);
        Point start = new Point (taxi_x, taxi_y);

        jipQuery = cl.openSynchronousQuery(parser_cl.parseTerm("client_coor(X,Y)."));
		term = jipQuery.nextSolution();
		while (term != null) {
			client_x = Double.parseDouble(term.getVariablesTable().get("X").toString());
			client_y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
			term = jipQuery.nextSolution();
		}

        start.distance_so_far = 0;
        if (taxi_id == 0) {
        	start.x = end.x;
        	start.y = end.y;
        	client_x = taxi_x;
        	client_y = taxi_y;
        	end.x = client_x;
        	end.y = client_y;

        }

        start.fscore = start.distance(client_x, client_y);
        Point current = start;

        queue.clear();
        close.clear();
        queue.add(start);
        int count = 0;
        while (!queue.isEmpty()) {
        	count++;
        	current = queue.get(0);
        	if (current.x == end.x && current.y == end.y) break;
        	close.add(current);
        	queue.remove(0);

        	jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes_coor(X," + current.x + "," +current.y + ")."));
    		term = jipQuery.nextSolution();
    		if (term != null) {
    			id = Double.parseDouble(term.getVariablesTable().get("X").toString());
    		}

    		int line_id = 0;
    		jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes_line(" + id + ",X)."));
    		term = jipQuery.nextSolution();
    		if (term != null) {
    			line_id = Integer.parseInt(term.getVariablesTable().get("X").toString());
    		}

    		jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes_neighbors(" + id + ",X)."));
    		term = jipQuery.nextSolution();
    		while (term != null) {
    			id2 = Double.parseDouble(term.getVariablesTable().get("X").toString());
    			jipQuery2 = jip.openSynchronousQuery(parser.parseTerm("nodes_coor(" + id2 + ",X,Y)."));
    			term2 = jipQuery2.nextSolution();
    			if (term2 != null) {
    				x1 = Double.parseDouble(term2.getVariablesTable().get("X").toString());
    				y1 = Double.parseDouble(term2.getVariablesTable().get("Y").toString());
    			}

    			Point i = new Point(x1, y1);
        		if (close.contains(i)) {
        			term = jipQuery.nextSolution();
        			continue;
        		}
        		double h = 1;

        		jipQuery3 = jip3.openSynchronousQuery(parser3.parseTerm("line_heuristic(" + line_id + ",X)."));
        		term3 = jipQuery3.nextSolution();
        		if (term3 != null) {
        			h = Double.parseDouble(term3.getVariablesTable().get("X").toString());
        		}

            	if (!queue.contains(i)) {
            	  	i = new Point (i.x, i.y);
            		queue.add(i);
            		if (queue.size() > max_size_needed) max_size_needed = queue.size();
            		if (queue.size() > size) queue.remove(queue.size() - 1);

            	}
            	else if (current.distance_so_far + h * i.distance(current.x, current.y) >= i.distance_so_far) {
            		term = jipQuery.nextSolution();
            		continue;
            	}

            	i.father = current;
            	i.distance_so_far = current.distance_so_far + i.distance(current.x, current.y);
            	i.fscore = i.distance_so_far + h * i.distance(client_x, client_y);
            	term = jipQuery.nextSolution();
        	}

        	Collections.sort(queue, new Comparator<Point>(){
   		     public int compare(Point o1, Point o2){
   		         if(o1.fscore == o2.fscore)
   		             return 0;
   		         return o1.fscore < o2.fscore ? -1 : 1;
   		     }
        	});
		}

        System.out.println("ID: " + taxi_id + ", max size: " + (max_size_needed-1) + ", steps: " + count);
        return current;
	}
}
