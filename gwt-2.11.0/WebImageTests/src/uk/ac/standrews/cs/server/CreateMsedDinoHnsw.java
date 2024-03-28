package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.JavaObjectSerializer;
import com.github.jelmerk.knn.ObjectSerializer;
import com.github.jelmerk.knn.hnsw.HnswIndex;
import com.github.jelmerk.knn.DistanceFunction;
import com.github.jelmerk.knn.Item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateMsedDinoHnsw {

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

        System.out.println("ok");

        List<Item<Integer,float[]>> dinoLog = Dino2_data.getSoftmaxData(1000);
        List<MsedItem> ms = new ArrayList<>();
        for (int i = 0; i < dinoLog.size(); i++) {
            MsedItem msi = new MsedItem(dinoLog.get(i).vector(), dinoLog.get(i).id() + 1);
            ms.add(msi);
        }
        long t0 = System.currentTimeMillis();
        index.addAll(ms);
        System.out.println("build time: " + (System.currentTimeMillis() - t0));

        index.save(new File("/Volumes/Data/mf_dino_sm10_hnsw_1m.obj"));

    }
}
