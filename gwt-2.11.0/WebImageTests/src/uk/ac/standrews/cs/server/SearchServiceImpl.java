package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.*;
import com.github.jelmerk.knn.hnsw.HnswIndex;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.ac.standrews.cs.client.SearchService;
import uk.ac.standrews.cs.shared.IndexSearchResult;
import uk.ac.standrews.cs.shared.IndexTypes;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {
    private static HnswIndex<Integer, MsedRep, MsedItem, Float> index;

    @Override
    public String initialise(IndexTypes.INDEX_TYPES indexType) {
        switch (indexType) {

            case MSED:
                try {
                    if (this.index == null) {
                        this.index = getLoadedIndex();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return "ok";
            default:
                return "index type not known";
        }
    }

    public List<Integer> search(int imageId) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            res.add(100);
        }
        return res;

    }

    @Override
    public IndexSearchResult search(Set<Integer> imageIds) {
        List<Integer> qids = new ArrayList<>();
        qids.addAll(imageIds);
        List<Integer> res = new ArrayList<>();
        long time = 0;

        if (imageIds.size() == 1) {
            long t0 = System.currentTimeMillis();
            List<SearchResult<MsedItem, Float>> queryResults = index.findNeighbors(qids.get(0), 100);
            time = System.currentTimeMillis() - t0;
            for (SearchResult<MsedItem, Float> r : queryResults) {
                res.add(r.item().id());
            }
        } else {
            List<float[]> reps = new ArrayList<>();
            for (int i : imageIds) {
                Optional<MsedItem> x = index.get(i);
                reps.add(x.get().vector().getVecSum());
            }
            MsedRep msr = new MsedRep(reps);
            long t0 = System.currentTimeMillis();
            List<SearchResult<MsedItem, Float>> queryResults = index.findNearest(msr, 100);
            time = System.currentTimeMillis() - t0;
            for (SearchResult<MsedItem, Float> r : queryResults) {
                res.add(r.item().id());
            }
        }
        IndexSearchResult isr = new IndexSearchResult();
        isr.result = res;
        isr.time = time;
        return isr;
    }

    private HnswIndex<Integer, MsedRep, MsedItem, Float> getLoadedIndex() throws IOException {
        // HnswIndex<Integer, MsedRep, MsedItem, Float> res = HnswIndex.load(new File("/Volumes/Data/mf_dino_sm10_hnsw_1m.obj"));
        HnswIndex<Integer, MsedRep, MsedItem, Float> res = HnswIndex.load(new File("/Volumes/Data/mf_dino_sm10_hnsw_1m.obj"));

//        HnswIndex<Integer, MsedRep, MsedItem, Float> res = HnswIndex.load(new File("/Volumes/Data/mf_pl_sm_hnsw_1m.obj"));
        return res;
    }

    private <Tid, Tvector, Tdistance extends Comparable<Tdistance>, Titem extends Item<Tid, Tvector>>
    HnswIndex<Tid, Tvector, Titem, Tdistance> getIndex(DistanceFunction<Tvector, Tdistance> dist) throws
            IOException {

        ObjectSerializer<Tid> itemIdSerializer = new JavaObjectSerializer<>();
        ObjectSerializer<Titem> itemSerializer = new JavaObjectSerializer<>();

        HnswIndex.Builder<Tvector, Tdistance> builder = HnswIndex
                .newBuilder(384, dist, 10000);

        HnswIndex<Tid, Tvector, Titem, Tdistance> index = builder
                .withCustomSerializers(itemIdSerializer, itemSerializer)
                .withM(5)
                .withEfConstruction(100)
                .withEf(100)
                .build();

        return index;
    }
}
