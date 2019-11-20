import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.regex.*;
import java.io.IOException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import java.util.*;

class NodesParser {

	public double endx, endy;
	public HashMap <Point, ArrayList<Point>> hm;

	public void parser (String nodesFile) throws JIPSyntaxErrorException, IOException {

		JIPEngine cl = new JIPEngine();
		JIPEngine roads = new JIPEngine();
		this.hm = new HashMap<>();
		Point previous, current;

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("nodes.pl");
			String line = "";
			BufferedReader br = null;
			String [] temp = new String[15];

			JIPTermParser parser_cl = cl.getTermParser();
			cl.consultFile("client.pl");

			JIPTermParser parser_roads = roads.getTermParser();
			roads.consultFile("prolog.pl");

			JIPQuery jipQuery;
			JIPTerm term;
			double x = 0, y = 0;
			double dist, min_dist;

			jipQuery = cl.openSynchronousQuery(parser_cl.parseTerm("client_coor(X,Y)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				x = Double.parseDouble(term.getVariablesTable().get("X").toString());
				y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
				term = jipQuery.nextSolution();
			}

			double previousx, previousy, currentx, currenty;

			int  previous_line, current_line;
			double previous_id, current_id;

			 try {
					br = new BufferedReader(new FileReader(nodesFile));
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}
		        try {
		        	line = br.readLine();
		        	line = br.readLine();
		        	temp = line.split(",");

		        	previousx = Double.parseDouble(temp[0]);
		        	previousy = Double.parseDouble(temp[1]);
		        	previous_line = Integer.parseInt(temp[2]);
		        	previous_id = Double.parseDouble(temp[3].trim())/1000;
		        	previous = new Point (previousx, previousy);
				   	this.hm.put(previous, new ArrayList<>());

		        	this.endx = previousx;
		        	this.endy = previousy;

		    		min_dist = distance(x, y, previousx, previousy);

		    		jipQuery = roads.openSynchronousQuery(parser_roads.parseTerm("road(" + previous_line + ")."));
					if (jipQuery.nextSolution() != null) {
						writer.println("nodes_coor(" + previous_id + "," + previousx + "," + previousy + ").");
			        	writer.println("nodes_line(" + previous_id + "," + previous_line + ").");
					}

					while ((line = br.readLine()) != null) {
						temp = line.split(",");
						currentx = Double.parseDouble(temp[0]);
			        	currenty = Double.parseDouble(temp[1]);
			        	current_line = Integer.parseInt(temp[2]);
			        	current_id = Double.parseDouble(temp[3].trim())/10000;
			        	current = new Point (currentx, currenty);

			        	jipQuery = roads.openSynchronousQuery(parser_roads.parseTerm("road(" + current_line + ")."));
						if (jipQuery.nextSolution() == null) {
							continue;
						}
			        	dist = distance(x, y, currentx, currenty);
						if (dist < min_dist) {
							min_dist = dist;
							this.endx = currentx;
							this.endy = currenty;
						}

						if (previous_line == current_line) {
							if (!this.hm.containsKey(current)) {
								this.hm.put(current, new ArrayList<>());
								writer.println("nodes_coor(" + current_id + "," + currentx + "," + currenty + ").");
					        	writer.println("nodes_line(" + current_id + "," + current_line + ").");
							}

							jipQuery = roads.openSynchronousQuery(parser_roads.parseTerm("oneway(" + current_line + ")."));
							if (jipQuery.nextSolution() != null) {
								writer.println("nodes_neighbors(" + previous_id + "," + current_id + ").");
								this.hm.get(previous).add(current);
							}
							else {
								jipQuery = roads.openSynchronousQuery(parser_roads.parseTerm("onewayBack(" + current_line + ")."));
								if (jipQuery.nextSolution() != null) {
									writer.println("nodes_neighbors(" + current_id + "," + previous_id + ").");
									this.hm.get(current).add(previous);
								}
								else {
									writer.println("nodes_neighbors(" + current_id + "," + previous_id + ").");
									writer.println("nodes_neighbors(" + previous_id + "," + current_id + ").");
									this.hm.get(previous).add(current);
									this.hm.get(current).add(previous);
								}
							}

						}
							/*if (!this.hm.containsKey(current)) {
								this.hm.put(current, new ArrayList<>());
							}

						}*/
						else {
							if (!this.hm.containsKey(current)) {
								this.hm.put(current, new ArrayList<>());
								writer.println("nodes_coor(" + current_id + "," + currentx + "," + currenty + ").");
					        	writer.println("nodes_line(" + current_id + "," + current_line + ").");
							}
						}

						previousx = currentx;
			        	previousy = currenty;
			        	previous_line = current_line;
						previous_id = current_id;
					}
				} catch (IOException e) {
					e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
            System.out.println("File not found");
        } finally {
            if (writer != null)
                writer.close();
        }
	    return;
	}

	private double distance(double x1, double y1, double x2, double y2) {
	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(x2 - x1);
	    double lonDistance = Math.toRadians(y2 - y1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	    		+ Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    return distance;
	}

}
