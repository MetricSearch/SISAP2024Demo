package uk.ac.standrews.cs.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import uk.ac.standrews.cs.shared.IndexTypes;
import uk.ac.standrews.cs.shared.IndexSearchResult;

import java.util.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebImageTest implements EntryPoint {

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service proxy to talk to the server-side Greeting service.
     */
    private final SearchServiceAsync searchService = GWT.create(SearchService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        Panel buttonPanel = new HorizontalPanel();
        RootPanel.get("buttonPanelContainer").add(buttonPanel);
        Panel imagePanel = new HorizontalPanel();
        RootPanel.get("imagePanelContainer").add(imagePanel);

        Panel queryPanelContainer = new HorizontalPanel();
        QueryPanel queryPanel = new QueryPanel();
        queryPanelContainer.add(queryPanel);
        RootPanel.get("queryPanelContainer").add(queryPanelContainer);

        Button searchButton = getSearchButton(queryPanel, imagePanel);
//        addInitialiseButton(searchButton, buttonPanel);

        Button updateQuery = new Button("update query");
        updateQuery.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Set<Integer> newIds = queryPanel.getQueryIds();
                queryPanel.updateImageIds(newIds);
            }
        });

        Button randomImages = new Button("random query");
        randomImages.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Random rand = new Random();
                Set<Integer> rs = new TreeSet<>();
                for (int i = 0; i < 5; i++) {
                    rs.add(rand.nextInt(1_000_000));
                }
                queryPanel.updateImageIds(rs);
            }
        });

        buttonPanel.add(new Label("initialising, one moment please..."));
        //first, load the dino2/msed hnsw onto the server
        try {
            searchService.initialise(IndexTypes.INDEX_TYPES.MSED, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable e) {
                    Window.alert("oh dear can't initialise: " + e);
                }

                @Override
                public void onSuccess(String s) {
                    buttonPanel.clear();
                    ;
                    buttonPanel.add(searchButton);
                    searchButton.setEnabled(true);
                    buttonPanel.add(updateQuery);
                    buttonPanel.add(randomImages);
                }
            });
        } catch (Exception e) {
            Window.alert("oh dear: " + e);
        }

    }

    private Button getSearchButton(QueryPanel queryPanel, Panel imagePanel) {
        Button searchButton = new Button("search");
        searchButton.setEnabled(false);

        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {

                imagePanel.clear();
                Set<Integer> ids = queryPanel.getQueryIds();
//                Set<Integer> ids = new HashSet<>();
//                ids.add(0);

                searchService.search(ids, new AsyncCallback<IndexSearchResult>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Window.alert("oh dear search failed");
                    }

                    @Override
                    public void onSuccess(IndexSearchResult sr) {
                        try {
                            List<Integer> resultIds = sr.result;
                            imagePanel.add(new ThumbnailPanel(resultIds, queryPanel.getQueryIds()));
                            RootPanel.get("errorLabelContainer").add(new Label("time: " + sr.time));
                        } catch (Exception e) {
                            Window.alert("" + e);
                        }
                    }
                });
            }
        });
        return searchButton;
    }

    private void addInitialiseButton(final Button searchButton, Panel buttonPanel) {
        final Button button = new Button("initialise");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                try {
                    searchService.initialise(IndexTypes.INDEX_TYPES.MSED, new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable e) {
                            Window.alert("oh dear can't initialise: " + e);
                        }

                        @Override
                        public void onSuccess(String s) {
                            button.setEnabled(false);
                            searchButton.setEnabled(true);
                        }
                    });
                } catch (Exception e) {
                    Window.alert("oh dear: " + e);
                }
            }
        });
        buttonPanel.add(button);
    }
}
