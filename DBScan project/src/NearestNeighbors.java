/**
 /**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */


import java.util.*;
public class NearestNeighbors {

    Point3D[] pointset; // storage for 3D points
    public NearestNeighbors(List<Point3D> points){
        pointset = points.toArray(new Point3D[0]);
    }
    
    /**
	* Find all points in a neighborhood of P
    * @param eps The radius we consider to be 'inside' the neighborhood
    * @param P   The point we want to find neighbors of
    * @return A list of points in the neihborhood of P
	*/
    public List<Point3D> rangeQuery(double eps, Point3D P){
        
        ArrayList<Point3D> out = new ArrayList<Point3D>();

        // Loop through all points
        for(Point3D q: pointset){
            //Check distance to see if q counts as a neighbor
            if (P.distance(q)<=eps){
                out.add(q);
            }
        }
        return out;
    }
}
