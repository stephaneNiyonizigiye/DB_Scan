/**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */

import java.io.*;
import java.util.*;


public class DBScan {


    private Double epsilon = 1.2; // distance for each point to be in a neighbourhood
    private int minpts = 10; //minimum points in a neighborhood to be considered a point inside a cluster
    private List<Point3D> DB; // Set of points we want to cluster
    private NearestNeighbors NN;
    private NearestNeighborsKD N2;
    private int C; // Total number of clusters & current number of clusters during runtime
    private static int NOISE = 0;
    private static int UNDEFINED = -1;

    // set this to true if you want it to run faster
    public static boolean FAST = true;

    public DBScan(List<Point3D> DB) {
        this.DB = DB;

        // ternary operator to chooseif you want a NNF (fast) or NearestNeighbors(slow)
        NN = FAST ? new NNF(DB) : new NearestNeighbors(DB);
        N2 = new NearestNeighborsKD(DB);
    }

    /**
     * Getters and Setters
     */
    public void setEps(double eps) {
        epsilon = eps;
    }

    public double getEps() {
        return epsilon;
    }

    public void setMinPts(int minPts) {
        minpts = minPts;
    }

    public int getMinPts() {
        return minpts;
    }

    public int getNumberOfClusters() {
        return C;
    }

    public List<Point3D> getPoints() {
        return DB;
    }


    /**
     * Classifies each point in DB into a cluster, or as noise if
     * there are an insufficient number of nearby points
     * At the end of runtime, this also prints the size of each
     * cluster from largest to smallest
     */

    public void findClusters() {

        Stack<Point3D> S = new Stack<Point3D>();
        C = 0; /* Cluster counter */
        for (Point3D P : DB) {
            if (P.label != UNDEFINED) {
                continue;
            } /* Already processed */
            List<Point3D> N = NN.rangeQuery(epsilon, P); /* Find neighbors */
            if (N.size() < minpts) { /* Density check */
                P.label = NOISE; /* Label as Noise */
                continue;
            }

            C++; /* next cluster label */
            P.label = C; /* Label initial point */
            S.addAll(N); /* Push neighbors to stack */

            while (!S.empty()) {
                Point3D Q = S.pop(); /* Process point Q */
                if (Q.label == NOISE) {
                    Q.label = C; /* Noise becomes border pt */
                    continue; //not actually necessary since the next if causes a continue
                }
                if (Q.label != UNDEFINED) {
                    continue;
                } /* Previously processed */
                Q.label = C; /* Label neighbor */
                N = NN.rangeQuery(epsilon, Q); /* Find neighbors */
                if (N.size() >= minpts) { /* Density check */
                    S.addAll(N); /* Push new neighbors to stack */
                }
            }
        }
    }

    public void findClustersKD() {

        Stack<Point3D> S = new Stack<Point3D>();
        C = 0; /* Cluster counter */
        for (Point3D P : DB) {
            if (P.label != UNDEFINED) {
                continue;
            } /* Already processed */
            List<Point3D> N = N2.rangeQuery(P, epsilon); /* Find neighbors */
            if (N.size() < minpts) { /* Density check */
                P.label = NOISE; /* Label as Noise */
                continue;
            }

            C++; /* next cluster label */
            P.label = C; /* Label initial point */
            S.addAll(N); /* Push neighbors to stack */

            while (!S.empty()) {
                Point3D Q = S.pop(); /* Process point Q */
                if (Q.label == NOISE) {
                    Q.label = C; /* Noise becomes border pt */
                    continue; //not actually necessary since the next if causes a continue
                }
                if (Q.label != UNDEFINED) {
                    continue;
                } /* Previously processed */
                Q.label = C; /* Label neighbor */
                N = N2.rangeQuery(Q, epsilon); /* Find neighbors */
                if (N.size() >= minpts) { /* Density check */
                    S.addAll(N); /* Push new neighbors to stack */
                }
            }
        }
    }


    /**
     * Function to display cluster sizes from largest to smallest
     */
    public void displayClusters() {
        //counting the size of each cluster
        int[] cs = new int[C];
        int noise = 0, sum_ = 0;
        for (Point3D P : DB) {
            // funny one liner to increment each cluster properly
            noise += (P.label == 0) ? 1 : (cs[P.label - 1]++) * 0;
        }
        // sort & print sizes
        Arrays.sort(cs);
        sum_ = noise;
        System.out.print("Cluster sizes: [");
        for (int i = C - 1; i >= 0; i--) {
            System.out.print(cs[i] + ",");
            sum_ += cs[i];
        }
        System.out.println("]\nNoise: " + noise + "\nNumber of Points: " + sum_);
    }

    /**
     * Function to get unique rgb values from cluster labels
     *
     * @param label Integer cluster label
     * @return String containing r,g,b values separated by commas
     */
    private String rgb(int label) {
        if (label == 0) {
            return "0.0,0.0,0.0";
        }

        double val = (0.5 * label) / C + 0.5;
        int id = (label % 7) + 1;
        /*  Bit shift shenanigans to make 7 colors
         *   colors are then scaled up with val
         *   to make them unique for each cluster
         */
        double r = (id >> 2) * val;
        double g = ((id % 4) >> 1) * val;
        double b = (id % 2) * val;
        String s = r + "," + g + ',' + b;
        return s;
    }


    /**
     * Saves points with their rgb labels to a csv file
     *
     * @param filename The directory to save your csv file
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public void save(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        //initialize writer and set header
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.println("x,y,z,C,R,G,B");
        //write each point with its corresponding color
        for (Point3D p : DB) {
            String xyz = p.getX() + "," + p.getY() + "," + p.getZ() + ",";
            writer.println(xyz + p.label + "," + rgb(p.label));
        }
        writer.close();
    }

    /**
     * Saves points with their rgb labels to a csv file
     *
     * @param filename The directory to save your csv file
     * @return A list of 3D points
     * @throws FileNotFoundException
     */
    public static List<Point3D> read(String filename) throws FileNotFoundException {

        List<Point3D> points = new ArrayList<Point3D>();
        File myObj = new File(filename);
        //java forces a try catch since scanner throws file not found errors
        Scanner reader = new Scanner(myObj);
        //first line should have column names
        reader.nextLine();
        //scan through file
        while (reader.hasNextLine()) {
            //get next line and split into x,y,z
            String[] tokens = reader.nextLine().split(",");
            //make into point
            Point3D p = new Point3D(
                    Double.parseDouble(tokens[0]),
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2])
            );
            //add point to list
            points.add(p);
        }
        reader.close();
        return points;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        args= new String[5];
        String[] params = new String[]{"src/Point_cloud_1.csv", "src/Point_cloud_2.csv","src/Point_cloud_3.csv", "1.2", "10"};
        for (int i=0;i<params.length;i++){
            args[i]=params[i];
        }

        long startTime = System.currentTimeMillis();
        for (int k = 0; k <3; k++) {
            List<Point3D> DB = read(args[k]);
            DBScan dbs = new DBScan(DB);
            //setting epsilon
            dbs.setEps(Double.parseDouble(args[3]));
            //setting minpts
            dbs.setMinPts(Integer.parseInt(args[4]));
            //get the clusters
            dbs.findClusters();
            dbs.displayClusters();
            //save the output
            dbs.save(args[k].split("\\.")[0] + "_clusters_" + dbs.getEps() + "_" + dbs.getMinPts() + "_" + dbs.getNumberOfClusters() + ".csv");
            long endTime = System.currentTimeMillis();
            System.out.println(args[k]);
            System.out.println("execution time is " + (endTime - startTime));
        }

        //KD tree process recording
    long startTimeKD = System.currentTimeMillis();


        for (int j = 0; j < 3; j++) {
        List<Point3D> DBkd = read(args[j]);
        DBScan dbs = new DBScan(DBkd);
        //setting epsilon
        dbs.setEps(Double.parseDouble(args[3]));
        //setting minpts
        dbs.setMinPts(Integer.parseInt(args[4]));
        //get the clusters
        dbs.findClustersKD();
        dbs.displayClusters();
        //save the output
        dbs.save(args[j].split("\\.")[0] + "_clusters_" + dbs.getEps() + "_" + dbs.getMinPts() + "_" + dbs.getNumberOfClusters() + ".csv");
        long endTimeKD = System.currentTimeMillis();
        System.out.println(args[0]+ " KD tree");
        System.out.println("execution time is " + (endTimeKD - startTimeKD));
        System.out.println("");
    }
}
}
