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

public class LinesParser{

	public void parser(String linesFile) throws JIPSyntaxErrorException, IOException {

        /*---------------Prolog Queries Section---------------*/
        JIPEngine traffic = new JIPEngine();
        traffic.consultFile("traffic.pl");
        /*----------End of Prolog Queries Section-------------*/

		PrintWriter writer = null;
	    try{
	        writer = new PrintWriter("lines.pl");

            /*---------------Prolog Queries Section---------------*/
            JIPTermParser trafficParser = traffic.getTermParser();
            JIPQuery jipQuery;
            JIPTerm term;
            /*----------End of Prolog Queries Section-------------*/

			BufferedReader reader = null;
            try{
                    reader = new BufferedReader(new FileReader(linesFile));
                    String line = null;
                    String trash = null;
                    try{
                    	trash = reader.readLine();
                        while ((line = reader.readLine()) != null){

                            /*LINE CANT BE ACCESSED - DISCARDED*/
                            String[] elements = line.split(",",-1);
                            if (!elements[7].equals("") || !elements[8].equals("") || !elements[9].equals("") || !elements[10].equals("") || !elements[11].equals("") || !elements[15].equals("")) continue;
                            if (elements[1].equals("steps") || elements[1].equals("pedestrian") || elements[1].equals("footway")) continue;

                            /*LINE CAN BE ACCESSED - USED*/
                            int lineID = Integer.parseInt(elements[0]);
                            writer.println("line(" + lineID + ").");
                            double heuristic = 1.3;

                            /*Line Characteristic 1: Highway*/
                            String highway = elements[1];
                            if (highway.equals("motorway_link")) heuristic -= 1.2;
                            else if (highway.equals("trunk")) heuristic -= 0.9;
                            else if (highway.equals("primary")) heuristic -= 0.7;
                            else if (highway.equals("secondary")) heuristic -= 0.5;
                            else if (highway.equals("tertiary")) heuristic += 0.3;
                            else if (highway.equals("residential")) heuristic += 0.5;
                            else if (highway.equals("residential")) heuristic += 0.7;

                            /*Line Characteristic 2: Oneway*/
                            String oneway = elements[3];
                            if (!oneway.equals("")) {
                                if (oneway.equals("yes")) writer.println("line_oneway(" + lineID + ").");
                                else if (oneway.equals("-1")) writer.println("line_oneway_back(" + lineID + ").");
                            }

                            /*Line Characteristic 3: Lit*/
                            String lit = elements[4];
                            if (lit.equals("yes")) writer.println("line_lit(" + lineID + "," + lit + ").");
                            else heuristic += 0.1;

                            /*Line Characteristic 4: Number of lanes*/
                            String lanesCheck = elements[5];
                            if (!lanesCheck.equals("")) {
                                String busway = elements[16];
                                int lanes =  Integer.parseInt(lanesCheck);
                                /*If there is a bus lane, decrease the available lines by 1*/
                                if(busway.equals("")) lanes--;
                                writer.println("line_lanes(" + lineID + "," + lanes + ").");
                                heuristic-= 0.1 * lanes;
                            }

                            /*Line Characteristic 5: Line Speed*/
                            String maxspeed = elements[6];
                            if (!maxspeed.equals(""))writer.println("line_speed(" + lineID + "," + Integer.parseInt(maxspeed) + ").");

                            /*Line Characteristic 6: Incline*/
                            String incline = elements[14];
                            if(incline.equals("up")) heuristic += 0.2;
                            else if(incline.equals("down")) heuristic -= 0.2;

                            /*Line Characteristic 7: Toll*/
                            String toll = elements[17];
                            if (!toll.equals("")) {
                                writer.println("line_tolls(" + lineID+ "," + toll + ").");
                                if(toll.equals("yes")) heuristic += 0.3;
                            }

                            /*Line Characteristic 8: Check if there is traffic*/
                            /*---------------Prolog Queries Section---------------*/
                            jipQuery = traffic.openSynchronousQuery(trafficParser.parseTerm("traffic(" + lineID + ",X)."));
                            term = jipQuery.nextSolution();
                            if(term!=null){
                                 String answer = term.getVariablesTable().get("X").toString();
                                 if(answer.equals("high")) heuristic+=2;
                                 else if(answer.equals("medium")) heuristic+=1;
                                 else  heuristic+=0.5;

                            }
                            /*----------End of Prolog Queries Section-------------*/

                            /*Finally each line is characterized by its customized heuristic value. Each characteristic,
                             *from the previous ones, if existed, led to the increasing, decreasing or the preservation
                             * of this value depending on whether it facilited or not taxi's route
                             */
                            if (heuristic < 0) heuristic = 0.0;
                            writer.println("line_heuristic(" + lineID + "," + heuristic + ").");
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
}
