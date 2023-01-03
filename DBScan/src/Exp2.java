/**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */


import java.util.List;

public class Exp2 {
    public static void main(String[] args) throws Exception {
        args = new String[5];
        args[1] = "0.05";
        args[2] = "src/Point_Cloud_1.csv";
        args[3]="src/Point_Cloud_2.csv";
        args[4]="src/Point_Cloud_3.csv";
        //setting eps
        double eps = Double.parseDouble(args[1]);
        //initializing time
        long time=0;
        long timeKD=0;

        for (int i=2;i<5;i++){
        // reads the csv file
        List<Point3D> points = Exp1.read(args[i]);
        int j =0;
        while (j<points.size()){

            // linear execution time
            NearestNeighbors nn = new NearestNeighbors(points);
            long startTime = System.nanoTime();
            nn.rangeQuery(eps,points.get(j));
            long endTime = System.nanoTime();
            time +=  (endTime - startTime) ;// in milliseconds.


            //create the nearestneighborsKD instance
            NearestNeighborsKD n2 = new NearestNeighborsKD(points);
            long startTimeKd = System.nanoTime();
            n2.rangeQuery(points.get(j), eps);
            long endTimeKd = System.nanoTime();
            timeKD += (endTimeKd - startTimeKd);// in milliseconds.


            j+=10;// incrementation
        }
        //calculating time and printout
        time=time/1000000;
        timeKD=timeKD/1000000;
        System.out.println("execution time for the file "+args[i]);
        System.out.println("linear Time : "+time);
        System.out.println("Time KD tree : "+timeKD);
        time=0;
        timeKD=0;

     }
    }
}
