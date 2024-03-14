package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.*;
import com.github.jelmerk.knn.hnsw.HnswIndex;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.ac.standrews.cs.client.SearchService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.DoubleStream;

public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {
    @Override
    public String searchServer(String name) {
        try {
            HnswIndex<Integer, float[], Item<Integer, float[]>, Float> index = getIndex(DistanceFunctions.FLOAT_COSINE_DISTANCE);
            List<Item<Integer, float[]>> data = Dino2_data.getData(1);
//            for( int i = 0; i < 10; i++){
//                index.add(data.get(i));
//            }
            index.addAll(data);
            int s = index.size();

            return "hello from server after accessing JerkMerk with size " + s;
        } catch (Exception e) {
            return "exception: " + e;
        }
    }

    private <Tid, Tvector, Tdistance extends Comparable<Tdistance>, Titem extends Item<Tid, Tvector>> HnswIndex<Tid, Tvector, Titem, Tdistance> getIndex(DistanceFunction<Tvector, Tdistance> dist) {

        ObjectSerializer<Tid> itemIdSerializer = new JavaObjectSerializer<>();
        ObjectSerializer<Titem> itemSerializer = new JavaObjectSerializer<>();

        HnswIndex.Builder<Tvector, Tdistance> builder = HnswIndex
                .newBuilder(384, dist, 10000);

        HnswIndex<Tid, Tvector, Titem, Tdistance> index = builder
                .withCustomSerializers(itemIdSerializer, itemSerializer)
                .withM(5)
                .withEfConstruction(100)
                .withEf(100)
                .withRemoveEnabled()
                .build();

        return index;
    }
}
