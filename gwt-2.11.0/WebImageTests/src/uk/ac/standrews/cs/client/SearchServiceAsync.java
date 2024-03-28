package uk.ac.standrews.cs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import uk.ac.standrews.cs.shared.IndexTypes;
import uk.ac.standrews.cs.shared.IndexSearchResult;

import java.util.Set;

/**
 * The async counterpart of <code>SearchService</code>.
 */
public interface SearchServiceAsync {
    void initialise(IndexTypes.INDEX_TYPES indexType, AsyncCallback<String> callback)
            throws IllegalArgumentException;
    void search(Set<Integer> imageIds, AsyncCallback<IndexSearchResult> callback);


}
