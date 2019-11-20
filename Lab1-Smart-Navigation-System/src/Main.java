import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
public class Main{
    private static Taxi taxis;
    private static ClientLocation client;
    private static NodeLocation nodes;
    private static Astar search;
    public static ArrayList<ArrayList<AstarResult>>  results;
    
    public static void main(String [] args){
        String nodesFile = "nodes.csv";
        nodes = new NodeLocation();
        nodes.parser(nodesFile);

        String clientFile = "client.csv";
        client = new ClientLocation(clientFile,nodes);

        String taxisFile = "taxis.csv";
        taxis = new Taxi();
        taxis.parser(taxisFile,nodes);


        search = new Astar();

        System.out.println("client's location: " + client.x + " " + client.y);
        System.out.println("client's nearest node: " + client.endingNode.x + " " + client.endingNode.y);

        
        results = new ArrayList<ArrayList<AstarResult>>();
        for (int i = 0; i < taxis.taxis.size() ; i++) {

            ArrayList<AstarResult> result = search.AstarTaxis(taxis.taxis.get(i), nodes, client);
            results.add(result);
            nodes.reset(nodes.hashMap);
            File file = new File("AllPossiblePath" + taxis.taxis.get(i).id + ".txt");
            file.delete();
     
        }
        
        Collections.sort(results, new Comparator<ArrayList<AstarResult>>(){
            public int compare(ArrayList<AstarResult> o1, ArrayList<AstarResult> o2){

                 if(o1.get(0).dist == o2.get(0).dist)
                    return 0;
                return o1.get(0).dist < o2.get(0).dist ? -1 : 1;
            }
        });


        kml("taxiFileInput2.kml");

        return;

    }


     public static void kml(String filename) {

            Random rand = new Random();
            Color green = Color.GREEN.darker(), color;
            PrintWriter writer = null;

            try {

                writer = new PrintWriter(filename);
  
                writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.println("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
                writer.println("<Document>");
                writer.println("<name>Taxi Routes</name>");
                writer.println("<Style id=\"green\">");
                writer.println("<LineStyle>");
                writer.println("<color>" + Integer.toHexString(green.getRGB()) + "</color>");
                writer.println("<width>4</width>");
                writer.println("</LineStyle>");
                writer.println("</Style>");
                int i = 1;
                /**
                 *  Make random colors for the
                 *  rest of the taxi routes.
                 *  Don't make it green.
                 */
                while (i < results.size()) {
                    int r = rand.nextInt(255);
                    int g = rand.nextInt(127);
                    int b = rand.nextInt(255);
                    color = new Color(r, g, b);

                    if (color.getRGB() == green.getRGB()) continue;

                    writer.println("<Style id=\"taxi" + i + "\">");
                    writer.println("<LineStyle>");
                    writer.println("<color>" + Integer.toHexString(color.getRGB()) + "</color>");
                    writer.println("<width>4</width>");
                    writer.println("</LineStyle>");
                    writer.println("</Style>");

                    

                    i++;

                }

                /**
                 *  This is to make the client point
                 */
                writer.println("<Placemark>");
                writer.println("<name>Client</name>");
                writer.println("<Point>");
                writer.println("<coordinates>");
                writer.println(client.x + "," + client.y);
                writer.println("</coordinates>");
                writer.println("</Point>");
                writer.println("</Placemark>");
                

                int j = 0;

                while( j < results.get(0).size()){
                    ArrayList<double []> current = results.get(0).get(j).route;
                    int id = results.get(0).get(j).id;

                    
                    /**
                     *  This is to make the first route
                     *  have green color.
                     */
                    writer.println("<Placemark>");
                    writer.println("<name>Taxi's ID " + id + "</name>");
                    writer.println("<styleUrl>#green</styleUrl>");
                    writer.println("<LineString>");
                    writer.println("<altitudeMode>relative</altitudeMode>");
                    writer.println("<coordinates>");
                    for (double[] co : current){

                        writer.println(co[0] + "," + co[1]);
                    }
                    writer.println("</coordinates>");
                    writer.println("</LineString>");
                    writer.println("</Placemark>");


                    j++;
                }
                results.remove(0);
                
                
                i = 1;
                /**
                 *  Do the same thing for all the rest
                 *  of the taxi routes.
                 */
                for(ArrayList<AstarResult> currentSet : results) {

                    j = 0;


                    while( j < currentSet.size()){

                        ArrayList<double []> currentRoute = currentSet.get(j).route;
                    
                        
                        writer.println("<Placemark>");
                        writer.println("<name>TaxiID " + currentSet.get(j).id + "</name>");
                        writer.println("<styleUrl>#taxi" + i + "</styleUrl>");
                        writer.println("<LineString>");
                        writer.println("<altitudeMode>relative</altitudeMode>");
                        writer.println("<coordinates>");
                        for (double[] co : currentRoute)
                            writer.println(co[0] + "," + co[1]);
                        writer.println("</coordinates>");
                        writer.println("</LineString>");
                        writer.println("</Placemark>");
                        j++;

                    }

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
}
