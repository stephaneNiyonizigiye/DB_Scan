/**
 /**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */

import java.lang.Math;

public class Point3D{
    private double X;
    private double Y;
    private double Z;
    public boolean stacked=false;
    // -1 will correspond too unlabelled
    public int label = -1;

    public Point3D(double x, double y, double z){
        X=x;
        Y=y;
        Z=z;
    }
    /**
     * Getters       
     */
    public double getX(){return X;}
    public double getY(){return Y;}
    public double getZ(){return Z;}
    public String toString(){return "("+X+","+Y+","+Z+")";}

    /**
	* Function to get the euclidian distance between this point and pt
    * this is implemented to get the distance as fast as possible
    * @param pt The point we want the distance to (from this point)
	* @return
	* 	3D euclidian distance
	*/
    public double distance(Point3D pt){
        return Math.sqrt(L2(pt));
    }
    // gets coordinate x, y or z if axis 0, 1, or 2
    public double get(int axis) {

        switch(axis) {
            case 0: return getX();
            case 1: return getY();
            case 2: return getZ();
            default: return 0.0;
        }
    }

    /**
	* Conceptually same as distance but faster since you don't call Sqrt
    */
    public double L2(Point3D pt){
        //collect individual 1D distances
        double dx=(X-pt.X);
        double dy=(Y-pt.Y);
        double dz=(Z-pt.Z); 
        //calculate euclidian 3D distance squared
        return dx*dx+dy*dy+dz*dz;
    }


}
