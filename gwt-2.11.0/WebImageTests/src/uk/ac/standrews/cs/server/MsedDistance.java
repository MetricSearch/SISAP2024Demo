package uk.ac.standrews.cs.server;

import java.util.ArrayList;
import java.util.List;
import com.github.jelmerk.knn.DistanceFunction;

public class MsedDistance implements DistanceFunction<MsedRep, Float> {
    @Override
    public Float distance(MsedRep u, MsedRep v) {
        int nSum = u.getN() + v.getN();
        float[] v1 = vector_mean(u.getVecSum(), v.getVecSum(), nSum);
//        printVec(v1);
        double comp = MsedRep.complexity(v1);
//        System.out.println("C" + comp);

        double prod = u.getCompProduct() * v.getCompProduct();
        double root = Math.pow(prod, 1.0 / nSum);
        return (float) ((comp / root - 1)) / (nSum - 1);

//
//        System.out.println("R" + root);
//        System.out.println("P" + prod);
//        System.out.println("N" + nSum);
    }

    private float[] vector_mean(float[] v, float[] w, int nSum) {
        float[] res = new float[v.length];
        for (int i = 0; i < v.length; i++) {
            res[i] = (v[i] + w[i]) / nSum;
        }
        return res;
    }

    public static void main(String a[]) {
        System.out.println("ok");
        float[] v1 = {1f, 0, 0, 0};
        float[] v2 = {0, 0, 0, 1f};
        float[] v3 = {0.35f, 0.25f, 0.25f, 0.15f};
        List<float[]> l1 = new ArrayList<>();
        l1.add(v1);
        List<float[]> l2 = new ArrayList<>();
        l2.add(v2);
        l2.add(v3);

        MsedRep m1 = new MsedRep(l1);
        MsedRep m2 = new MsedRep(l2);

        MsedDistance md = new MsedDistance();
        System.out.println(md.distance(m1, m2));
    }

    private static void printVec(float[] fs) {
        for (float f : fs) {
            System.out.print(f + " ");
        }
        System.out.println();
    }
}
