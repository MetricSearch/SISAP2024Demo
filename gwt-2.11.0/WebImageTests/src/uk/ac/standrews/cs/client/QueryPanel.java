package uk.ac.standrews.cs.client;

import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class QueryPanel extends HorizontalPanel {

    private Set<Integer> queryIds;

    QueryPanel() {
        queryIds = new TreeSet<>();
        Random r = new Random();
        int queryId = 0;//r.nextInt(1_000_000);
        queryIds.add(queryId);
        this.add(new ThumbImageButton(queryId, queryIds, true));
    }

    public void updateImageIds(Set<Integer> queryIds) {
        this.queryIds = queryIds;
        this.clear();
        for (int queryId : queryIds) {
            this.add(new ThumbImageButton(queryId, queryIds, true));
        }
    }

    public Set<Integer> getQueryIds() {
        return queryIds;
    }

}
