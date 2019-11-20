import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.regex.*;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import java.io.*;

class TaxisParser {
	public ArrayList  <Integer> taxis = new ArrayList<Integer>();

	public void parser(String taxisFile) throws JIPSyntaxErrorException, IOException{
		PrintWriter writer = null;
			try{
					writer = new PrintWriter("taxis.pl");
			BufferedReader reader = null;
				try{
					reader = new BufferedReader(new FileReader(taxisFile));
					String line = null;
					String trash = null;
					try{
						trash = reader.readLine();
						while ((line = reader.readLine()) != null){
							String[] elements = line.split(",");

							int id = Integer.parseInt(elements[2]);
							writer.println("taxis_coor("+id +","+ Double.parseDouble(elements[0]) + "," + Double.parseDouble(elements[1])+").");
							writer.println("taxis_available("+id +","+ elements[3] +").");
							writer.println("taxis_capacity("+id +","+ Integer.parseInt(elements[4].split("-")[1]) +")." );
							String[] languages = elements[5].split("\\|");

							for (String s: languages){
								writer.println("taxis_language("+id +","+ s +").");
							}
							writer.println("taxis_rating("+id +","+ Double.parseDouble(elements[6]) +").");
							writer.println("taxis_long_distance("+id +","+ elements[7] +").");

							String luggage = elements[8].split(Pattern.quote("("))[0];
							String luggageResult = luggage.substring(0, luggage.length() - 1);

							if(luggageResult.equals("subcompact")){

								writer.println("taxis_luggage("+id +",1" +").");
							}
							else if (luggageResult.equals("compact")){
						    	writer.println("taxis_luggage("+id +",2" +").");

							}
							else{
							 	writer.println("taxis_luggage("+id +",3" +").");
							}

							this.taxis.add(id);

						}
						reader.close();

					}catch(IOException e){
						System.out.println("Input/Output");
					}

				}catch(FileNotFoundException e){
				e.printStackTrace();
				}
			writer.close();

		}catch(FileNotFoundException e){
					e.printStackTrace();
			}

	}

	public void selectTaxis (boolean flag_dist) throws JIPSyntaxErrorException, IOException {
		JIPEngine jip = new JIPEngine();
        int persons = 0, luggage = 0;
        String language = "";
		jip.consultFile("client.pl");
		JIPTermParser parser = jip.getTermParser();

		JIPQuery jipQuery;
		JIPTerm term;

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("client_persons(X)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			persons = Integer.parseInt(term.getVariablesTable().get("X").toString());
		}

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("client_luggage(X)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			luggage = Integer.parseInt(term.getVariablesTable().get("X").toString());
		}

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("client_language(X)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			language = term.getVariablesTable().get("X").toString();
		}

		jip.consultFile("taxis.pl");
		String answer = "";
		int num = 0;

		Iterator<Integer> ite = this.taxis.iterator();
		while(ite.hasNext()) {
			int i = ite.next();
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_available(" + i + ",X)."));
			term = jipQuery.nextSolution();
			if (term != null) {
				answer = term.getVariablesTable().get("X").toString();
				if (!answer.equals("yes")) {
					System.out.println("Taxi " + i + " is not available.");
					ite.remove();
					continue;
				}
			}

			jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_capacity(" + i + ",X)."));
			term = jipQuery.nextSolution();
			if (term != null) {
				num = Integer.parseInt(term.getVariablesTable().get("X").toString());
				if (num < persons) {
					System.out.println("Taxi " + i + " does not have enough seats.");
					ite.remove();
					continue;
				}
			}

			boolean flag = false;
			jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_language(" + i + ",X)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				answer = term.getVariablesTable().get("X").toString();
				if (answer.equals(language)) {
					flag = true;
				}
				term = jipQuery.nextSolution();
			}
			if (!flag) {
				System.out.println("Taxi " + i + " 's driver does not speak the same language as the client.");
				ite.remove();
				continue;
			}

			if (flag_dist) {
				jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_long(" + i + ",X)."));
				term = jipQuery.nextSolution();
				if (term != null) {
					answer = term.getVariablesTable().get("X").toString();
					if (!answer.equals("yes")) {
						System.out.println("Taxi " + i + " is not suitable for long routes.");
						ite.remove();
						continue;
					}
				}
			}

			jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_luggage(" + i + ",X)."));
			term = jipQuery.nextSolution();
			if (term != null) {
				num = Integer.parseInt(term.getVariablesTable().get("X").toString());
				if (num < luggage) {
					System.out.println("Taxi " + i + " does not have enough space for luggages.");
					ite.remove();
					continue;
				}
			}
		}
	}

	public Point nearestNode (int id) throws JIPSyntaxErrorException, IOException {
		JIPEngine jip = new JIPEngine();
        double taxi_x = 0, taxi_y = 0;

		jip.consultFile("taxis.pl");
		JIPTermParser parser = jip.getTermParser();

		JIPQuery jipQuery;
		JIPTerm term;

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_coor(" + id +",X,Y)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			taxi_x = Double.parseDouble(term.getVariablesTable().get("X").toString());
			taxi_y = Double.parseDouble(term.getVariablesTable().get("Y").toString());
		}

		double x = 0, y = 0, min_dist = Double.POSITIVE_INFINITY, dist, min_x = 0, min_y = 0;
		jip.consultFile("nodes.pl");

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("nodes_coor(Z,X,Y)."));
		term = jipQuery.nextSolution();
		while(term != null) {
			x = Double.parseDouble(term.getVariablesTable().get("X").toString());
			y = Double.parseDouble(term.getVariablesTable().get("Y").toString());

			dist = distance(taxi_x, taxi_y, x, y);
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

	private double distance (double x1, double y1, double x2, double y2) {
	    final int R = 6371; // Radius of the earth
	    double latDistance = Math.toRadians(x2 - x1);
	    double lonDistance = Math.toRadians(y2 - y1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	    		+ Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2))* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    return distance;
	}

	public int isFar (int id) throws JIPSyntaxErrorException, IOException {
		JIPEngine jip = new JIPEngine();

		jip.consultFile("taxis.pl");
		JIPTermParser parser = jip.getTermParser();

		JIPQuery jipQuery;
		JIPTerm term;


		String answer = "";

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_long(" + id + ",X)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			answer = term.getVariablesTable().get("X").toString();
			if (answer.equals("yes")) {
				return 0;
			}
		}

		return 1;
	}
}
