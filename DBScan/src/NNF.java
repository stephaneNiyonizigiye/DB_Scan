
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NNF extends NearestNeighbors {
    public NNF(List<Point3D> var1) {
        super(var1);
        Arrays.sort(this.pointset, new P3DC());
    }

    private int last_leq(double var1) {
        int var3 = 0;
        int var4 = this.pointset.length;

        while(var3 < var4 - 1) {
            int var5 = (var3 + var4) / 2;
            if (this.pointset[var5].getX() < var1) {
                var3 = var5;
            } else {
                var4 = var5;
            }
        }

        return var3;
    }

    public List<Point3D> rangeQuery(double var1, Point3D var3) {
        int var4 = this.last_leq(var3.getX() - var1);
        double var5 = var3.getX() + var1;
        double var7 = var1 * var1;
        ArrayList var9 = new ArrayList();

        for(int var10 = var4; var10 < this.pointset.length && var5 >= this.pointset[var10].getX(); ++var10) {
            if (var3.L2(this.pointset[var10]) <= var7) {
                var9.add(this.pointset[var10]);
            }
        }

        return var9;
    }

    private class P3DC implements Comparator<Point3D> {
        private P3DC() {
        }

        public int compare(Point3D var1, Point3D var2) {
            int var3 = 0;
            var3 += var1.getX() > var2.getX() ? 9 : (var1.getX() < var2.getX() ? -9 : 0);
            var3 += var1.getY() > var2.getY() ? 3 : (var1.getY() < var2.getY() ? -3 : 0);
            var3 += var1.getZ() > var2.getZ() ? 1 : (var1.getZ() < var2.getZ() ? -1 : 0);
            return var3;
        }
    }
}
