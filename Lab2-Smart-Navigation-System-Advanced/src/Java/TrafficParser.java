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

class TrafficParser {

	public void parser (String traffic_file) throws JIPSyntaxErrorException, IOException {
		JIPEngine cl = new JIPEngine();

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("traffic.pl");
			String line = "";
			BufferedReader br = null;
			String [] temp = new String[15];
			int id, time = 0;
			String [] temp2 = new String[15];

			JIPTermParser parser_cl = cl.getTermParser();
			cl.consultFile("client.pl");

			JIPQuery jipQuery;
			JIPTerm term;

			jipQuery = cl.openSynchronousQuery(parser_cl.parseTerm("client_time(X)."));
			term = jipQuery.nextSolution();
			while (term != null) {
				String t = term.getVariablesTable().get("X").toString();
				temp = t.split(",");
				temp = temp[0].split("\\(");
				time = Integer.parseInt(temp[1]);
				term = jipQuery.nextSolution();
			}

			 try {
					br = new BufferedReader(new FileReader(traffic_file));
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}
		        try {
		        	line = br.readLine();
		        	line = br.readLine();
		        	temp = line.split(",",-1);
		        	id = Integer.parseInt(temp[0]);

					temp2 = temp[2].split("\\|");
					if (time >= 9 && time < 11) {
						temp2 = temp2[0].split("=");
						writer.println("traffic(" + id + "," + temp2[1] + ").");
					}
					else if (time >= 13 && time < 15) {
						temp2 = temp2[1].split("=");
						writer.println("traffic(" + id + "," + temp2[1] + ").");
					}
					else if (time >= 17 && time < 19) {
						temp2 = temp2[2].split("=");
						writer.println("traffic(" + id + "," + temp2[1] + ").");
					}

					while ((line = br.readLine()) != null) {
						temp = line.split(",");
						if (temp.length != 3) continue;
			        	id = Integer.parseInt(temp[0]);
			        	if (temp[1].equals("")) {
			        		continue;
			        	}
			        	if (temp[2].equals("")) {
			        		continue;
			        	}

						temp2 = temp[2].split("\\|");
						if (time >= 9 && time < 11) {
							temp2 = temp2[0].split("=");
							writer.println("traffic(" + id + "," + temp2[1] + ").");
						}
						else if (time >= 13 && time < 15) {

							temp2 = temp2[1].split("=");
							writer.println("traffic(" + id + "," + temp2[1] + ").");
						}
						else if (time >= 17 && time < 19) {
							temp2 = temp2[2].split("=");
							writer.println("traffic(" + id + "," + temp2[1] + ").");
						}
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
}
