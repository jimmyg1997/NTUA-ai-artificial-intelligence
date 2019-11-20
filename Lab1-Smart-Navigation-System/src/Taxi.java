import java.util.*;
import java.io.*;

class Taxi {
	ArrayList  <TaxiLocation> taxis = new ArrayList<>();

	public void parser (String taxiCoordinates, NodeLocation nodes) {
		String line = "";
		BufferedReader reader = null;
		String [] temp = new String[3];

		try {
			reader = new BufferedReader(new FileReader(taxiCoordinates));
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}

    try {
    	line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				temp = line.split(",");
				TaxiLocation tempTaxiCoordinates =  new TaxiLocation(temp, nodes);
				this.taxis.add(tempTaxiCoordinates);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    
	  return;
	}

}
