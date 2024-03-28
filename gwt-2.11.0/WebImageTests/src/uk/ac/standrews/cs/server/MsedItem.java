package uk.ac.standrews.cs.server;

import com.github.jelmerk.knn.Item;

import java.io.Serializable;
import java.util.List;

public class MsedItem  implements Item<Integer,MsedRep>, Serializable {

    private int id;
    private MsedRep msed;

    MsedItem(List<float[]> vector, int id){
        this.id = id;
        this.msed = new MsedRep(vector);
    }
    MsedItem(float[] vector, int id){
        this.id = id;
        this.msed = new MsedRep(vector);
    }
    MsedItem(){

    }
    @Override
    public Integer id() {
        return this.id;
    }

    @Override
    public MsedRep vector() {
        return this.msed;
    }

    @Override
    public int dimensions() {
        return 0;
    }
}
