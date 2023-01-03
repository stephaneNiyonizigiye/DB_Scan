/**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */

import java.util.ArrayList;
import java.util.List;

public class NearestNeighborsKD {

        KDTree KDTree ;

        // construct with list of points
        public NearestNeighborsKD(List<Point3D> points) {

            KDTree = new KDTree();
            for (Point3D point : points) {

                if (KDTree.root==null) {
                    KDTree.root = new KDTree.KDnode(point, 0);
                    continue;
                }

                KDTree.insert(point, KDTree.root, 0);
            }
        }

        // gets the neighbors of p (at a distance less than eps)
        public List<Point3D> rangeQuery(Point3D p, double eps) {

            // empty list to contain the neighbors
            List<Point3D> neighbors = new ArrayList<Point3D>();

            //neighbors recursive search
            rangeQuery(p,eps,neighbors,this.KDTree.root);

            return neighbors;

        }
        public void  rangeQuery(Point3D p, double eps, List<Point3D> N, KDTree.KDnode node){
            if (node==null){
                return;
            }
            if (p.distance(node.point)<eps){
                N.add(node.point);
            }
            if(p.get(node.axis)-eps<=node.value){
                rangeQuery(p,eps,N,node.left);
            }
            if (p.get(node.axis)+eps>node.value){
                rangeQuery(p,eps,N,node.right);
            }
        }
    }

