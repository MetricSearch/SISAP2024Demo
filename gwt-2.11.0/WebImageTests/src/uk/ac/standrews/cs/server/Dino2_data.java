package uk.ac.standrews.cs.server;


import com.github.jelmerk.knn.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Dino2_data {


    private static final int dimensions = 384;
    private static String pathroot = "/Volumes/Data/mf_dino2/mf_dino2_text/";
    private static String javaPathRoot = "/Volumes/Data/mf_dino2/mf_dino2_java/";
    private static String javaLogisticPathRoot = "/Volumes/Data/mf_dino2/mf_dino2_logisticL1_java/";

    public static List<Item<Integer, float[]>> getLogisticL1Data(int noOfFiles) throws IOException {
        List<Item<Integer, float[]>> res = new ArrayList<>();
        for (int fileNumber = 0; fileNumber < noOfFiles; fileNumber++) {
            res.addAll(getLogisticL1DataFile(fileNumber));
        }
        return res;
    }

    public static List<Item<Integer, float[]>> getData(int noOfFiles) throws IOException {
        List<Item<Integer, float[]>> res = new ArrayList<>();
        for (int fileNumber = 1; fileNumber <= noOfFiles; fileNumber++) {
            res.addAll(getDataFile(fileNumber));
        }
        return res;
    }

    private static List<Item<Integer, float[]>> getLogisticL1DataFile(int fileNumber) throws IOException {
        try {
            // if it's already here, just read it as an object
            return getJavaFile(javaLogisticPathRoot, fileNumber);
        } catch (Exception e) {
            //otherwise, read the raw data, normalise/logistic it, and write the java file
            List<Item<Integer, float[]>> rawData = getDataFile(fileNumber);
            List<Item<Integer, float[]>> res = new ArrayList<>();
            for (Item<Integer, float[]> entry : rawData) {
                float[] vec = entry.vector();
                vec = logistic(vec);
                vec = l1_norm(vec);

                res.add(new ListItem(entry.id(), vec));
            }


            File f = new File(javaLogisticPathRoot + fileNumber + ".obj");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(res);
            oos.close();

            return res;
        }
    }

    private static float[] logistic(float[] vec) {
        // used this one in matlab seemed to work!
        // =1/(1+EXP(-0.5*D7))
        float[] fs = new float[vec.length];
        int i = 0;
        for (float f : vec) {
            double term = 1 / (1 + Math.exp(-f/2));
            fs[i++] = (float) term;
        }
        return fs;
    }


    /**
     * when reading the text files, this code performs RELU...
     * the text files are already L_2 normed from the original
     * not obvious why RELU is appplied in fact; I suspect this is an error!!
     * <p>
     * so, sorting this guff...
     * 1. rewriting all text files to use raw Dino data, i.e. cosine distance, not normalised
     * 2. deleted all cached data so need to re-read
     * 3. change this code to remove the RELU
     * 4. should we l_2-norm it? I think not, maybe in a special version
     *
     * @param fileNumber
     * @return
     * @throws IOException
     */
    private static List<Item<Integer, float[]>> getDataFile(int fileNumber) throws IOException {
        try {
            return getJavaFile(javaPathRoot, fileNumber);
        } catch (Exception e) {
            System.out.println("caught " + fileNumber);
            List<Item<Integer, float[]>> res = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(pathroot + fileNumber + ".txt"));
            for (int line = 0; line < 1000; line++) {
                String newLine = br.readLine();
                String[] data = newLine.split("\\s+");
                final float[] dat = new float[dimensions];
                for (int x = 0; x < dimensions; x++) {
                    float f = Float.parseFloat(data[x]);
                    dat[x] = f;
                }
                res.add(new ListItem(fileNumber * 1000 + line, dat));
            }
            br.close();

            File f = new File(javaPathRoot + fileNumber + ".obj");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(res);
            oos.close();

            return res;
        }
    }

    protected static List<Item<Integer, float[]>> getJavaFile(String java_path_root, int fileNumber) throws IOException, ClassNotFoundException {
        File f = new File(java_path_root + fileNumber + ".obj");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        List<Item<Integer, float[]>> res = (List<Item<Integer, float[]>>) ois.readObject();
        ois.close();
        return res;
    }


    public static List<Item<Integer, float[]>> getQueries(int noOfQueries) throws IOException {
        return getDataFile(0);
    }

//    private static float[] l1_norm(float[] f) {
//        float[] res = new float[f.length];
//        float acc = 0;
//        for (float ff : f) {
//            acc += ff;
//        }
//        for (int i = 0; i < f.length; i++) {
//            res[i] = f[i] / acc;
//        }
//        return res;
//    }


    public static void main(String[] a) throws IOException {
//        List<Item<Integer, float[]>> d = Dino2_data.getData(600);
//        List<Item<Integer, float[]>> q = Dino2_data.getQueries(100);
        List<Item<Integer, float[]>> d = Dino2_data.getLogisticL1Data(1000);

        System.out.println(d.size());
        float[] d1 = d.get(999999).vector();
//        float[] q1 = q.get(0).vector();
        float acc = 0;
        for (float f : d1) {
            System.out.println(f);
            acc += f;
        }
        System.out.println(acc);
        System.out.println(d1.length);
//        for (float f : q1) {
//            acc += f * f;
//        }
//        System.out.println(acc);

//
//        DistanceFunction<float[], Float> euc = DistanceFunctions.FLOAT_EUCLIDEAN_DISTANCE;
//        float[] smGirl = q.get(0).vector();
//        List<Float> smDists = new ArrayList<>();
//        for (int i = 0; i < d.size(); i++) {
//            smDists.add(euc.distance(smGirl, d.get(i).vector()));
//        }
//        smDists.sort(new Comparator<Float>() {
//            @Override
//            public int compare(Float o1, Float o2) {
//                return Float.compare(o1, o2);
//            }
//        });
//        for (int i = 0; i < 100; i++) {
//            System.out.println(smDists.get(i));
//        }

//        DistanceFunction<float[], Float> tri = DistanceFunctions.FLOAT_EUCLIDEAN_DISTANCE;
//        float tt1 = tri.distance(q.get(0).vector(), d.get(585585 - 1000).vector());
//        float tt2 = tri.distance(q.get(0).vector(), d.get(585585 - 999).vector());
//
//        System.out.println(tt1 + "; " + tt2);

    }

    private static float[] l1_norm(float[] f) {
        float[] res = new float[f.length];
        float acc = 0;
        for (float ff : f) {
            acc += ff;
        }
        for (int i = 0; i < f.length; i++) {
            res[i] = f[i] / acc;
        }
        return res;
    }
}
