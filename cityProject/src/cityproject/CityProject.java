/* CityProject.java  -- project class file with the project's main method   
 *  
 * This software package creates a graph of cities in the Unitied States with
 * links between the cities. Each city is a vertex in the graph.
 * Each link between cities is an edge in the graph.   The data for the cities and
 * links are read into arrays from data files, which should be in the project folder.
 * The files are CSV files, which can be read and edited in Excel.
 *
 * The main class for the project is the CityProject class.   Other class include:
 * 
 *   Vertex - clas for each Vertex in a graph.
 *   City extends Vertex - Each City is a Vertex with added properties.  Each City
 *      has a unique name, and X and Y cooordinates for location on a 1500 by 900 Canvas.
 *   Edge - an edge in the graph, with a source, destination, and length.
 *   AjacencyNode - a node for a linked list of cities directly connected to each City.
 *      Each City has a linked list of adjacnt cities, created from the info in the 
 *      data files, with destination City and distance data in the node, and a 
 *      link to the next node. 
 *   CityMap - extends Canvas, a map of the graph on a 1500 by 900 GUI Canvas.
 *      A CityMap object in instantiated in the drawMap method in the CityProject class.
 * 
 * The main method in the CityProject class calls methods to reads City and Edge 
 * data from data files into arrays, set up the adjacency list in each instance 
 * of City, print a list of Vertex cities and their Edges, then draw a map of the graph.
 *
 * created for use by students in CSCI 211 at Community Colle of Philadelphia
 * copyright 2014 by C. herbert.  last edited Nov. 23, 2014 by C. Herbert

 *Other methods are done by David Seng
*/
package cityproject;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class CityProject {

    // main metod for the project
    public static void main(String[] args) {
        // create a scanner for user inptu
        Scanner keyboard = new Scanner(System.in);
        String CitySource; //source loctaion
        String CityDest; //destination

        City[] cities = new City[200]; //array of cities (Vertices) max = 200
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new City();
        }

        Edge[] links = new Edge[2000];// array of links  (Edges)  max = 2000
        for (int i = 0; i < links.length; i++) {
            links[i] = new Edge();
        }

        int cityCount; //    actual number of cities
        int linkCount; //    actual number of links

        // load cities into an array from a datafile
        cityCount = readCities(cities);

        // load links into an array from a datafile
        linkCount = readLinks(links, cities);

        // create the adjacency list for each city based on the link array
        createAdjacencyLists(cityCount, cities, linkCount, links);

        // print adjacency lists for all cities
        PrintAdjacencyLists(cityCount, cities);

        // draw a map of the cities and links
        drawMap(cityCount, cities, linkCount, links);

        // ask the user for the starting city and final city to be evaluated for shortest path
        System.out.println("\n Please enter the current city.\n"
                + "\nPlease follow these specific format in order to get the correct result: 'City' then 'State'\n(Note: dont forget to capitalize City name and cap State Abbrev.)\nFor instance: Boston MA\n");
        CitySource = keyboard.nextLine();

        System.out.println("Please enter your destination city.\n"
                + "Format: 'City' then 'State'\n(Note: dont forget to capitalize City name and cap State Abbrev.)\nFor instance: Boston MA\n");
        CityDest = keyboard.nextLine();

        //set source and destination cities to be null
        City source = null;
        City destination = null;

        int i;

        for (i = 0; i < cities.length; i++) {
            if (CitySource.equals(cities[i].getName())) {
                source = cities[i];// matches user unputted source to city in cities array is then stored as a True source 
            }//end if
           
            if (CityDest.equals(cities[i].getName())) {
                destination = cities[i];// matches user inputted destination to city in cities array is then stored as True destination
            }//end if
        }//end for
 
        //use shortPath() to return the most efficient path to take as a linked list
        LinkedList path = shortPath(source, destination, cities);

        //Print the shortest path back to the user.
        System.out.println("\nThe shortest path from " + source.getName()
                + " to " + destination.getName() + " is:\n"
                + path.toString());
    } // end main
    //************************************************************************
    /** this method follows the three properties of vertex in dijkstra's algorithm
    * the method is checking to see if the city has been visited
    *check for the best distance
    * an has an immediate predecessor
    */
        public static LinkedList shortPath(City source, City destination, City[] cities) {
        City current = source;
        current.setBestDistance(0);
        current.setImmediatePredecessor(null);

        //variable to hold adjacency list head
        AdjacencyNode adj;

        boolean finished = false;

        //while not finsihed
        while (finished == false) {
            //set the adjacency list head to be the one or the current city
            adj = current.getAdjacencyListHead();

            //while adjacent cities not visited
            while (!(adj == null)) {
                int dist = adj.getcDistance() + current.getBestDistance();

                if (dist < adj.getCity().getBestDistance()) {
                    adj.getCity().setBestDistance(dist);
                    adj.getCity().setImmediatePredecessor(current);
                }//end if

                adj = adj.getNext();
            }//end inner while
          
            current.setVisited(true);

            current = newCurrent(cities);

            if (current == null) //if newCurrent() is null then must be no unvisited cities)
            {
                finished = true;
            }//end if

        }//end While
       
        //set the current City to be the destination city
        current = destination;

        //Create a new Linked List, with the head node to be the destination city
        LinkedList path = new LinkedList(destination);

        while (current.getImmediatePredecessor() != null) {
            path.addFirst(current.getImmediatePredecessor());
            current = current.getImmediatePredecessor();
        }

        //return the completed linked list
        return path;
    }//end ShortPath

    /**
     * ***************************************************************************
     */

    // method to read city data into an array from a data file
    public static int readCities(City[] cities) {

        int count = 0; // number of cities[] elements with data

        String[][] cityData = new String[123][3]; // holds data from the city file
        String delimiter = ",";                   // the delimiter in a csv file
        String line;                              // a String to hold each line from the file

        String fileName = "cities.csv";           // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there is another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                cityData[count] = line.split(delimiter);

                // read data from the 2D array into an array of City objects
                cities[count].setName(cityData[count][0]);
                cities[count].setX(Integer.parseInt(cityData[count][1]));
                cities[count].setY(Integer.parseInt(cityData[count][2]));

                count++;
            }// end while

            infile.close();

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;

    } // end loadCities()
    //*************************************************************************

    // method to read link data into an array from a data file
    public static int readLinks(Edge[] links, City[] cities) {
        int count = 0; // number of links[] elements with data

        String[][] CityLinkArray = new String[695][3]; // holds data from the link file
        String delimiter = ",";                       // the delimiter in a csv file
        String line;				      // a String to hold each line from the file

        String fileName = "links.csv";                // the file to be opened  

        try {
            // Create a Scanner to read the input from a file
            Scanner infile = new Scanner(new File(fileName));

            /* This while loop reads lines of text into an array. it uses a Scanner class 
             * boolean function hasNextLine() to see if there another line in the file.
             */
            while (infile.hasNextLine()) {
                // read the line 
                line = infile.nextLine();

                // split the line into separate objects and store them in a row in the array
                CityLinkArray[count] = line.split(delimiter);

                // read link data from the 2D array into an array of Edge objects
                // set source to vertex with city name in source column
                links[count].setSource(findCity(cities, CityLinkArray[count][0]));
                // set destination to vertex with city name in destination column
                links[count].setDestination(findCity(cities, CityLinkArray[count][1]));
                //set length to integer valuein length column
                links[count].setLength(Integer.parseInt(CityLinkArray[count][2]));

                count++;

            }// end while

        } catch (IOException e) {
            // error message dialog box with custom title and the error icon
            JOptionPane.showMessageDialog(null, "File I/O error:" + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return count;
    } // end loadLinks()
    //*************************************************************************

    // emthod to find the City onject with the given city name
    public static City findCity(City[] cities, String n) {
        int index = 0;  // loop counter
        // go through the cities array until the name is found
        // the name will be in the list

        while (cities[index].getName().compareTo(n) != 0) {

            index++;
        }// end while()
        return cities[index];

    } // end  findCity()

// method to create an adjacency lists for each city
    public static void createAdjacencyLists(int cityCount, City[] cities, int linkCount, Edge[] links) {

        AdjacencyNode temp = new AdjacencyNode();

        // iterate city array
        for (int i = 0; i < cityCount; i++) {

            //iterate link array
            for (int j = 0; j < linkCount; j++) {
                // if the currentl link's source is the current city
                if (links[j].getSource() == cities[i]) {

                    /* create a node for the link and inseert it into the adjancency list
                     * as the new head of the list. 
                     */
                    // temporarily store the current value of the list's head
                    temp = cities[i].getAdjacencyListHead();
                    //create a new node
                    AdjacencyNode newNode = new AdjacencyNode();
                    // add city and distance data
                    newNode.setCity(links[j].getDestination());
                    newNode.setDistance(links[j].getLength());
                    // point newNode to the previous list head
                    newNode.setNext(temp);

                    // set the new head of the list to newNode
                    cities[i].setAdjacencyListHead(newNode);

                }  // end if
            } // end for j
        } // end for i

    } // end createAdjacencyLists()

    // method to print adjacency lists
    public static void PrintAdjacencyLists(int cityCount, City[] cities) {

        System.out.println("List of Edges in the Graph of Cities by Source City");
        // iterate array of cities
        for (int i = 0; i < cityCount; i++) {

            // set current to adjacency list for this city    
            AdjacencyNode current = cities[i].getAdjacencyListHead();

            // print city name
            System.out.println("\nFrom " + cities[i].getName());

            // iterate adjacency list and print each node's data
            while (current != null) {
                System.out.println("\t" + current.toString());
                current = current.getNext();
            } // end while (current != null) 

        }   // end for i 

    } // end PrintAdjacencyLists()

    // method to draw the graph (map of cities and links)
    static void drawMap(int cCount, City[] c, int lCount, Edge[] l) {
        CityMap canvas1 = new CityMap(cCount, c, lCount, l);

        int width = 1500; // width of the Canvas
        int height = 900; // heightof the Canvas 

        // set up a JFrame to hold the canvas
        JFrame frame = new JFrame();
        frame.setTitle("U.S. Cities");
        frame.setSize(width, height);
        frame.setLocation(10, 10);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the canvas to the frame as a content panel
        frame.getContentPane().add(canvas1);
        frame.setVisible(true);

    } // end drawMap() 

    /**
   * This method return the city to be used as the next current city in Dijkstra's algorithm.
   * It takes an array of cities, and see if it has been visited, and check the best distance out of all cities.
   * 
    */
 
    public static City newCurrent(City[] cities) {
        //set minimum distance to infinity
        int minimumDist = Integer.MAX_VALUE;

        //initialize the city with the lowest bestDistance to be null
        City best = null;

        //check every city in the array
        for (int i = 0; i < cities.length; i++) {
            //if the city has not been visited
            if (cities[i].getVisited() == false) {
                //...and the city's best distance is less than the minimum distance currently...
                if (cities[i].getBestDistance() <= minimumDist) {
                    //...change the minDist to be the current city's best distance
                    minimumDist = cities[i].getBestDistance();

                    //set the city with the best distance (so far) to be the current city
                    best = cities[i];
                }
            }
        }
        //return the city with the lowest best distance that has not yet been visited
        return best;
    }
    
     //* **************************************************************************//
    

} // end class cityProject
