package uk.ac.standrews.cs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import uk.ac.standrews.cs.client.SearchService;

public class SearchServiceImpl extends RemoteServiceServlet implements
        SearchService {
    @Override
    public String searchServer(String name) throws IllegalArgumentException {
        return "hello from server";
    }
}
