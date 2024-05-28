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
    final IndexTypes.INDEX_TYPES indexType = IndexTypes.INDEX_TYPES.DINO2_L2;
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

        TextBox queryBox = new TextBox();

        Button queryIDButton = new Button("add mf query id", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                try {
                    Set<Integer> newIds = queryPanel.getQueryIds();
                    int id = Integer.parseInt(queryBox.getValue());

                    if (id < 0 || id > 999999) {
                        throw new NumberFormatException();
                    }

                    newIds.add(id);
                    queryPanel.updateImageIds(newIds);
                } catch (NumberFormatException e) {
                    Window.alert("Please enter a valid number!");
                }
            }
        });

        Button updateQuery = new Button("update query");
        updateQuery.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Set<Integer> newIds = queryPanel.getQueryIds();
                queryPanel.updateImageIds(newIds);
            }
        });

        Button clear = new Button("clear queries");
        clear.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                queryPanel.clear();
                RootPanel.get("errorLabelContainer").clear();
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

        buttonPanel.add(new Label("initialising index " + indexType + " , one moment please..."));
        //first, load the dino2/msed hnsw onto the server
        try {
            searchService.initialise(indexType, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable e) {
                    Window.alert("oh dear can't initialise: " + e);
                }

                @Override
                public void onSuccess(String s) {
                    buttonPanel.clear();
                    buttonPanel.add(searchButton);
                    searchButton.setEnabled(true);
                    buttonPanel.add(updateQuery);

                    buttonPanel.add(queryBox);
                    buttonPanel.add(queryIDButton);

                    buttonPanel.add(randomImages);
                    buttonPanel.add(clear);
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
                RootPanel.get("errorLabelContainer").clear();

                Set<Integer> ids = queryPanel.getQueryIds();
                imagePanel.clear();

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
//                            Window.alert("Got " + sr.result.size() + " results back");
                        } catch (Exception e) {
                            Window.alert("" + e);
                        }
                    }
                });
            }
        });
        return searchButton;
    }
}
