import java.util.*;
import java.io.*;
import java.lang.*;

public class ClientParser {
		public double x, y, destx, desty;
		public double estimated;

		public void parser (String clientFile ) throws FileNotFoundException {
		PrintWriter writer = null;
		try{
			writer = new PrintWriter("client.pl");
		BufferedReader reader = null;
					try{
						reader = new BufferedReader(new FileReader(clientFile));
						String line = null;
						String trash = null;
						try{
							trash = reader.readLine();
							line = reader.readLine();

							String[] elements = line.split(",");
							writer.println("client_coor(" + Double.parseDouble(elements[0]) + "," + Double.parseDouble(elements[1])+").");
							writer.println("client_dest(" + Double.parseDouble(elements[2]) + "," + Double.parseDouble(elements[3])+").");
							int first = Integer.parseInt(elements[4].split(":")[0]);
							int second = Integer.parseInt(elements[4].split(":")[1]);
							if(first < 10 && second < 10){
								writer.println("client_time(" +"0"+first + ":" + "0"+second+").");

							}
							else if(first < 10){
								writer.println("client_time(" +"0"+first + ":" +second+").");
							}
							else if( second < 10){
								writer.println("client_time(" +first + ":" + "0"+second+").");
							}
							else{
								writer.println("client_time(" +first + ":" +second+").");
							}
							writer.println("client_persons(" + Integer.parseInt(elements[5])+").");
							writer.println("client_language(" + elements[6]+").");
							writer.println("client_luggage(" + Integer.parseInt(elements[7])+").");

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
