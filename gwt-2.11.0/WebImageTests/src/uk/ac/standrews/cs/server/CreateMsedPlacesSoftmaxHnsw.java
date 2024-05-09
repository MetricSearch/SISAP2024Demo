package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.DistanceFunction;
import com.github.jelmerk.knn.Item;
import com.github.jelmerk.knn.JavaObjectSerializer;
import com.github.jelmerk.knn.ObjectSerializer;
import com.github.jelmerk.knn.hnsw.HnswIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CreateMsedPlacesSoftmaxHnsw {

    public static class Softmax_data_places {


        private static String pathroot = "/Volumes/Data/mf_sm_places/mf_alex_pl_softmax_text/";
        private static String javaPathRoot = "/Volumes/Data/mf_sm_places/mf_alex_pl_softmax_java/";

        private static int dimensions = 365;

        /**
         * @param noOfFiles
         * @return list if items; the id for each item is the integer id from the 1M data set, ie numbering from 0 for smoking girl
         * @throws IOException
         */
        public static List<Item<Integer, float[]>> getData(int noOfFiles) throws IOException {
            List<Item<Integer, float[]>> res = new ArrayList<>();
            for (int fileNumber = 1; fileNumber <= noOfFiles; fileNumber++) {
                res.addAll(getDataFile(fileNumber));
            }
            return res;
        }

        private static List<Item<Integer, float[]>> getDataFile(int fileNumber) throws IOException {
            try {
                return getJavaFile(fileNumber);
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
                    res.add(new ListItem(fileNumber * 1000 + line, l1_norm(dat)));
                }
                br.close();

                File f = new File(javaPathRoot + fileNumber + ".obj");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(res);
                oos.close();

                return res;
            }
        }

        private static List<Item<Integer, float[]>> getJavaFile(int fileNumber) throws IOException, ClassNotFoundException {
            File f = new File(javaPathRoot + fileNumber + ".obj");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            List<Item<Integer, float[]>> res = (List<Item<Integer, float[]>>) ois.readObject();
            ois.close();
            return res;
        }


        public static List<Item<Integer, float[]>> getQueries() throws IOException {
            return getDataFile(0);
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

    private static List<MsedItem> getPlacesSoftmaxData() throws IOException {

        List<Item<Integer, float[]>> qs = Softmax_data_places.getQueries();
        List<Item<Integer, float[]>> data = Softmax_data_places.getData(999);

        List<MsedItem> res = new ArrayList<>();
        for( Item<Integer, float[]> q : qs){
            res.add(new MsedItem(q.vector(),q.id()));
        }
        for( Item<Integer, float[]> d : data){
            res.add(new MsedItem(d.vector(),d.id()));
        }

        return res;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ObjectSerializer<Integer> itemIdSerializer = new JavaObjectSerializer<>();
        ObjectSerializer<MsedItem> itemSerializer = new JavaObjectSerializer<>();

        DistanceFunction<MsedRep, Float> dist = new MsedDistance();
        HnswIndex.Builder<MsedRep, Float> builder = HnswIndex
                .newBuilder(0, dist, 1000000);

        HnswIndex<Integer, MsedRep, MsedItem, Float> index = builder
                .withCustomSerializers(itemIdSerializer, itemSerializer)
                .withM(5)
                .withEfConstruction(100)
                .withEf(100)
                .build();


        List<MsedItem> ms = getPlacesSoftmaxData();

        long t0 = System.currentTimeMillis();
        index.addAll(ms);
        System.out.println("build time: " + (System.currentTimeMillis() - t0));

        index.save(new File("/Volumes/Data/mf_pl_sm_hnsw_1m.obj"));

    }
}
