package uk.ac.standrews.cs.server;


import com.github.jelmerk.knn.Item;

public class ListItem implements Item<Integer, float[]> {
    private final int id;
    private final float[] vec;

    public ListItem(int id, float[] vec) {
        this.id = id;
        this.vec = vec;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public float[] vector() {
        return vec;
    }

    @Override
    public int dimensions() {
        return vec.length;
    }

    @Override
    public long version(){
        return 0;
    }
}
