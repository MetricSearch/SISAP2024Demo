package uk.ac.standrews.cs.server;

import java.io.Serializable;
import java.util.List;

public class MsedRep implements Serializable {


    private int n;
    private float[] vecSum;
    private double compProduct;

    MsedRep(List<float[]> query) {
        this.n = query.size();
        this.vecSum = vector_sum(query);
        this.compProduct = 1;
        for( float[] fv : query){
            compProduct *= complexity(fv);
        }
    }

    MsedRep(float[] v) {
        this.n = 1;
        this.vecSum = v;
        this.compProduct = complexity(v);
    }

    public static float[] vector_sum(List<float[]> query) {
        float[] res = new float[query.get(0).length];
        for (float[] v : query) {
            for (int i = 0; i < v.length; i++) {
                res[i] = res[i] + v[i];
            }
        }
        return res;
    }

    public static double complexity(float[] v) {
        double acc = 0;
        for (float f : v) {
            acc -= f != 0? f * Math.log(f):0;
        }
        return Math.exp(acc);
    }

    public int getN() {
        return this.n;
    }

    public float[] getVecSum() {
        return this.vecSum;
    }

    public double getCompProduct() {
        return this.compProduct;
    }
}
