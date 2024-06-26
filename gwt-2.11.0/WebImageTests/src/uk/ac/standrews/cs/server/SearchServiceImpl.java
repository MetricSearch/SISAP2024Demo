package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.*;
import com.github.jelmerk.knn.hnsw.HnswIndex;

import uk.ac.standrews.cs.client.SearchService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.ac.standrews.cs.shared.IndexSearchResult;
import uk.ac.standrews.cs.shared.IndexTypes;

import java.io.*;
import java.util.*;

public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {


    private static HnswIndex<Integer, MsedRep, MsedItem, Float> index;
    String dino2IndexPath = null;
    String dino2L2IndexPath = null;

    @Override
    public String initialise(IndexTypes.INDEX_TYPES indexType) {
//        // Load the path to initialise index
//        Properties properties = new Properties();
//
//        try (InputStream input = new FileInputStream("config.properties")) {
//            // Load the properties file
////            properties.load(input);
//
//            // Get the value of the property "index-path"
////             dino2IndexPath = properties.getProperty("index-path-dino2");
////             dino2L2IndexPath = properties.getProperty("index-path-dino2-l2");
//            dino2L2IndexPath = "/Volumes/Data/mf_dino_sm10_hnsw_1m_l2.obj";
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        dino2L2IndexPath = "/Volumes/Data/mf_dino_sm10_hnsw_1m_l2.obj";

        try {
            if (this.index == null) {
                this.index = getLoadedIndex(indexType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "ok";
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
        List<Float> dists = new ArrayList<>();

        long time = 0;

        if (imageIds.size() == 1) {
            long t0 = System.currentTimeMillis();
            List<SearchResult<MsedItem, Float>> queryResults = index.findNeighbors(qids.get(0), 100);
            time = System.currentTimeMillis() - t0;
            for (SearchResult<MsedItem, Float> r : queryResults) {
                res.add(r.item().id());
                dists.add(r.distance());
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
                dists.add(r.distance());
            }
        }
        IndexSearchResult isr = new IndexSearchResult();
        isr.result = res;
        isr.time = time;
        isr.distances = dists;
        return isr;
    }

    private HnswIndex<Integer, MsedRep, MsedItem, Float> getLoadedIndex(IndexTypes.INDEX_TYPES indexType) throws IOException {
        switch (indexType) {
            case DINO2_L2:
                return HnswIndex.load(new File(dino2L2IndexPath));
            case DINO2:
                return HnswIndex.load(new File(dino2IndexPath));
        }

        throw new RuntimeException("Unspecified index load!");
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
