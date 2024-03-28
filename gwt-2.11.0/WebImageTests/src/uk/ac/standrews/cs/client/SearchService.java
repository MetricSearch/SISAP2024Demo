package uk.ac.standrews.cs.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import uk.ac.standrews.cs.shared.IndexTypes;
import uk.ac.standrews.cs.shared.IndexSearchResult;

import java.util.Set;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("search")
public interface SearchService extends RemoteService {
    String initialise(IndexTypes.INDEX_TYPES indexType);
    IndexSearchResult search(Set<Integer> imageIds);
}
