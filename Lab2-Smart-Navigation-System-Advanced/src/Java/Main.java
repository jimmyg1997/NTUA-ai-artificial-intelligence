import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class Main {
	private static TaxisParser taxis;
	private static ClientParser client;
	private static NodesParser nodes;
	private static LinesParser lines;
	private static TrafficParser traffic;
	private static Astar search;
	public static ArrayList<AstarResult> result;

	public static void main(String[] args) throws FileNotFoundException,IOException {
		String taxisFile = "taxis.csv";
		String nodesFile = "nodes.csv";
		String clientFile = "client.csv";
		String linesFile = "lines.csv";
		String trafficFile = "traffic.csv";

		lines = new LinesParser();
		client = new ClientParser();
		taxis = new TaxisParser();
		nodes = new NodesParser();
		traffic = new TrafficParser();
		search = new Astar();

		client.parser(clientFile);
		taxis.parser(taxisFile);
		traffic.parser(trafficFile);
		lines.parser(linesFile);
		nodes.parser(nodesFile);

		boolean longDist = false;
		if (client.estimated > 10000) longDist = true;

		taxis.selectTaxis(longDist);

		System.out.println();

		result = new ArrayList<AstarResult> ();

		Iterator<Integer> ite =  taxis.taxis.iterator();

		while(ite.hasNext()) {
			int i = ite.next();

			Point a = taxis.nearestNode(i);
			if (distance(a.x, a.y, nodes.endx, nodes.endy) > 10000) {
				if (taxis.isFar(i) == 1) {
					System.out.println("Taxi " + i + " is far away.");
					ite.remove();
					continue;
				}
			}
			AstarResult temp = search.AstarTaxis(a.x, a.y, i, nodes);
			result.add(temp);
		}

		Collections.sort(result, new Comparator<AstarResult>(){
		     public int compare(AstarResult o1, AstarResult o2){
		         if(o1.dist == o2.dist)
		             return 0;
		         return o1.dist < o2.dist ? -1 : 1;
		     }
		});

		if (result.size() > 5) {
			int count = result.size() - 5;
			for (int i = 1; i <= count; i++) {
				result.remove(result.size()-1);
			}
		}

		System.out.println("\n"+ "Taxis sorted by distance:");

		int count = 0;
		for (AstarResult i : result) {
			count++;
			System.out.println(count + ". Taxi " + i.id );
		}

		System.out.println();

		Collections.sort(result, new Comparator<AstarResult>(){
		     public int compare(AstarResult o1, AstarResult o2){
		         if(o1.rating == o2.rating)
		             return 0;
		         return o1.rating < o2.rating ? -1 : 1;
		     }
		});

		System.out.println("Taxis sorted by distance and rating:");

		count = 0;
		for (AstarResult i : result) {
			count++;
			System.out.println(count + ". Taxi " + i.id );
		}

		System.out.println();

		Point point_temp = nearestNode (client.destx, client.desty);
		AstarResult temp = search.AstarTaxis(point_temp.x, point_temp.y, 0, nodes);
		result.add(0, temp);

		kml("taxis.kml");

		return;
	}

	 public static void kml(String filename) {

	        Random rand = new Random();
	        Color green = Color.GREEN.darker(), color, red = new Color(200,150,10);
	        PrintWriter writer = null;

	        try {

	            writer = new PrintWriter(filename);
	            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	            writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
	            writer.println("<Document>");
	            writer.println("<name>Taxis</name>");
	            writer.println("<Style id=\"red\">");
	            writer.println("<LineStyle>");
	            writer.println("<color>" + Integer.toHexString(red.getRGB()) + "</color>");
	            writer.println("<width>4</width>");
	            writer.println("</LineStyle>");
	            writer.println("</Style>");
	            writer.println("<Style id=\"green\">");
	            writer.println("<LineStyle>");
	            writer.println("<color>" + Integer.toHexString(green.getRGB()) + "</color>");
	            writer.println("<width>4</width>");
	            writer.println("</LineStyle>");
	            writer.println("</Style>");
	            int i = 2;

	            while (i < result.size()) {

	                int r = rand.nextInt(255);
	                int g = rand.nextInt(127);
	                int b = rand.nextInt(255);
	                color = new Color(r, g, b);

	                if (color.getRGB() == green.getRGB())
	                    continue;

	                if (color.getRGB() == red.getRGB())
	                    continue;

	                writer.println("<Style id=\"taxi" + i + "\">");
	                writer.println("<LineStyle>");
	                writer.println("<color>" + Integer.toHexString(color.getRGB()) + "</color>");
	                writer.println("<width>4</width>");
	                writer.println("</LineStyle>");
	                writer.println("</Style>");

	                i++;

	            }
	            ArrayList<Location> current = result.get(0).route;
	            int id = result.get(0).id;
	            result.remove(0);

	            writer.println("<Placemark>");
	            writer.println("<name>Client</name>");
	            writer.println("<Point>");
	            writer.println("<coordinates>");
	            writer.println(client.x + "," + client.y);
	            writer.println("</coordinates>");
	            writer.println("</Point>");
	            writer.println("</Placemark>");
	            writer.println("<Placemark>");
	            writer.println("<name>destination " + "</name>");
	            writer.println("<styleUrl>#red</styleUrl>");
	            writer.println("<LineString>");
	            writer.println("<altitudeMode>relative</altitudeMode>");
	            writer.println("<coordinates>");
	            for (Location co : current)
	                writer.println(co.x + "," + co.y);
	            writer.println("</coordinates>");
	            writer.println("</LineString>");
	            writer.println("</Placemark>");

	            current = result.get(0).route;
	            id = result.get(0).id;
	            result.remove(0);

	            writer.println("<Placemark>");
	            writer.println("<name>Taxi's ID " + id + "</name>");
	            writer.println("<styleUrl>#green</styleUrl>");
	            writer.println("<LineString>");
	            writer.println("<altitudeMode>relative</altitudeMode>");
	            writer.println("<coordinates>");
	            for (Location co : current)
	                writer.println(co.x + "," + co.y);
	            writer.println("</coordinates>");
	            writer.println("</LineString>");
	            writer.println("</Placemark>");
	            i = 2;

	            for(AstarResult currentSet : result) {

	                ArrayList<Location> currentRoute = currentSet.route;
	                currentRoute.remove(0);
	                writer.println("<Placemark>");
	                writer.println("<name>TaxiID " + currentSet.id + "</name>");
	                writer.println("<styleUrl>#taxi" + i + "</styleUrl>");
	                writer.println("<LineString>");
	                writer.println("<altitudeMode>relative</altitudeMode>");
	                writer.println("<coordinates>");
	                for (Location co : currentRoute)
	                    writer.println(co.x + "," + co.y);
	                writer.println("</coordinates>");
	                writer.println("</LineString>");
	                writer.println("</Placemark>");

	                i++;

	            }

	            writer.println("</Document>");
	            writer.println("</kml>");

	        } catch (FileNotFoundException e) {
	            System.out.println("File not found");
	        } finally {
	            if (writer != null)
	                writer.close();
	        }
	    }

	 private static Point nearestNode (double x_dest, double y_dest) throws JIPSyntaxErrorException, IOException {
			JIPEngine jip = new JIPEngine();

			JIPTermParser parser = jip.getTermParser();

			JIPQuery jipQuery;
			JIPTerm term;

			double x = 0, y = 0, min_dist = Double.POSITIVE_INFINITY, dist, min_x = 0, min_y = 0;
			jip.consultFile("nodes.pl");

			jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes_coor(Z,X,Y)."));
			term = jipQuery.nextSolution();
			while(term != null) {
				x = Double.parseDouble(term.getVariablesTable().get("X").toString());
				y = Double.parseDouble(term.getVariablesTable().get("Y").toString());

				dist = distance(x_dest, y_dest, x, y);
				if (dist < min_dist) {
					min_dist = dist;
					min_x = x;
					min_y = y;
				}

				term = jipQuery.nextSolution();
			}

			Point a = new Point (min_x, min_y);
			return a;
		}

	 private static double distance(double x1, double y1, double x2, double y2) {
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
