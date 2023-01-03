
 		/**
		* @author Niyonizigiye stephane
		* num Etudiant: 300242927
		* */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;  

public class Exp1 {
  
  // reads a csv file of 3D points (rethrow exceptions!)
  public static List<Point3D> read(String filename) throws Exception {
	  
    List<Point3D> points= new ArrayList();
	double x,y,z;
	
	Scanner sc = new Scanner(new File(filename));  
	// sets the deli07miter pattern to be , or \n \r
	sc.useDelimiter(",|\n|\r");  

	// skipping the first line x y z
	sc.next(); sc.next(); sc.next();
	
	// read points
	while (sc.hasNext())  
	{  
		x= Double.parseDouble(sc.next());
		y= Double.parseDouble(sc.next());
		z= Double.parseDouble(sc.next());
		points.add(new Point3D(x,y,z));  
	}   
	
	sc.close();  //closes the scanner  
	
	return points;
  }
	/**
	 * Saves points with their rgb labels to a csv file
	 * @param filename The directory to save your csv file
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static void save(String filename, List<Point3D> points) throws FileNotFoundException, UnsupportedEncodingException{
		//initialize writer
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		//writing the list
		writer.println(points);
		writer.close();
	}
  
  public static void main(String[] args) throws Exception {
	  args = new String[6];
	  args[1] = "0.05";
	  args[2] = "src/Point_Cloud_1.csv";

	  double eps = Double.parseDouble(args[1]);
	  // reads the csv file
	  List<Point3D> points = Exp1.read(args[2]);

	  //adding  the points to search neighbors on
	  List<Point3D> pointReference = new ArrayList<>();
	  pointReference.add(new Point3D(-5.429850155, 0.807567048, -0.398216823));
	  pointReference.add(new Point3D(-12.97637373, 5.09061138, 0.762238889));
	  pointReference.add(new Point3D(-36.10818686, 14.2416184, 4.293473762));
	  pointReference.add(new Point3D(3.107437007, 0.032869335, 0.428397562));
	  pointReference.add(new Point3D(11.58047393, 2.990601868, 1.865463342));
	  pointReference.add(new Point3D(14.15982089, 4.680702457, -0.133791584));


	  for (int i = 0; i < pointReference.size(); i++) {

		  // creates the NearestNeighbor instance
		  NearestNeighbors nn = new NearestNeighbors(points);
		  List<Point3D> neighbors = nn.rangeQuery(eps,pointReference.get(i));

		  //create the nearestneighborsKD instance
		  NearestNeighborsKD n2 = new NearestNeighborsKD(points);
		  List<Point3D> neighborsKD = n2.rangeQuery(pointReference.get(i), eps);


		  //saving the 2 files
		  save("src/Exp1/pt" + (i+1) + "_lin.txt"+pointReference, neighbors);
		  save("src/Exp1/pt" + (i+1) + "_kd.txt", neighborsKD);

		  //printing the two lists for simularity check
		  System.out.println("number of neighbors= " + neighbors.size());
		  System.out.println(neighbors);
		  System.out.println("number of neighborsKD= " + neighborsKD.size());
		  System.out.println(neighborsKD);
	  }
  }
}
