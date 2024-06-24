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
    List<Integer> resultIds;
    /**
     * From https://stackoverflow.com/questions/1317052/how-to-copy-to-clipboard-with-gwt
     * @param text
     */
    public static native void copyTextToClipboard(String text) /*-{
        var textArea = document.createElement("textarea");
        //
        // *** This styling is an extra step which is likely not required. ***
        //
        // Why is it here? To ensure:
        // 1. the element is able to have focus and selection.
        // 2. if element was to flash render it has minimal visual impact.
        // 3. less flakyness with selection and copying which **might** occur if
        //    the textarea element is not visible.
        //
        // The likelihood is the element won't even render, not even a flash,
        // so some of these are just precautions. However in IE the element
        // is visible whilst the popup box asking the user for permission for
        // the web page to copy to the clipboard.
        //

        // Place in top-left corner of screen regardless of scroll position.
        textArea.style.position = 'fixed';
        textArea.style.top = 0;
        textArea.style.left = 0;

        // Ensure it has a small width and height. Setting to 1px / 1em
        // doesn't work as this gives a negative w/h on some browsers.
        textArea.style.width = '2em';
        textArea.style.height = '2em';

        // We don't need padding, reducing the size if it does flash render.
        textArea.style.padding = 0;

        // Clean up any borders.
        textArea.style.border = 'none';
        textArea.style.outline = 'none';
        textArea.style.boxShadow = 'none';

        // Avoid flash of white box if rendered for any reason.
        textArea.style.background = 'transparent';


        textArea.value = text;

        document.body.appendChild(textArea);

        textArea.select();

        try {
            var successful = document.execCommand('copy');
        } catch (err) {
            console.log('Unable to copy');
        }
        document.body.removeChild(textArea);
    }-*/;



    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        resultIds = new ArrayList<>();
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


        Button copyButton = new Button("copy result IDs", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // Grab the result IDs

                StringBuilder sb = new StringBuilder();

                for (int i : resultIds) {
                    sb.append(i);

                    if (i != resultIds.get(resultIds.size() - 1)) {
                        sb.append(',');
                    }
                }

                copyTextToClipboard(sb.toString());
            }
        });

        Button helpButton = new Button("help", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {

            }
        });

        Button examplesButton = new Button("examples", new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {

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
                    buttonPanel.add(examplesButton);
                    buttonPanel.add(clear);
                    buttonPanel.add(copyButton);
                    buttonPanel.add(helpButton);
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
                            resultIds = sr.result;
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
