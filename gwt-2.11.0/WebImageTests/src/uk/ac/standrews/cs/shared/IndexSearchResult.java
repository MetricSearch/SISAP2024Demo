package uk.ac.standrews.cs.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class IndexSearchResult implements IsSerializable {
    public long time;
    public List<Integer> result;
}
