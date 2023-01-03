/**
 * @author Niyonizigiye stephane
 * num Etudiant: 300242927
 * */

public class KDTree {
    public static class KDnode {
        public Point3D point;
        public int  axis;
        public double value;
        public KDnode left;
        public KDnode right;


        public KDnode (Point3D pt, int axis){
            this.point=pt;
            this.axis=axis;
            this.value=pt.get(axis);
            left=right=null;
        }
    }

    public KDnode root;
    //construct empty tree
    public KDTree(){
        root=null;
    }



    public KDnode insert(Point3D point, KDnode node, int axis){
        if(node ==null){
            node=new KDnode(point,axis);
        }
        else if (point.get(axis)<=node.value ){
            if (axis==0)
                node.left= insert(point,node.left,1);
            else
                node.left= insert(point,node.left,0);

        }
        else {
            if (axis == 0)
                node.right = insert(point, node.right, 1);
            else
                node.right = insert(point, node.right, 0);
        }
        return node;
    }
}

