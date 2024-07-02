package uk.ac.standrews.cs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import uk.ac.standrews.cs.shared.IndexSearchResult;

import java.util.*;

import static com.google.gwt.dom.client.Style.Unit.PCT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebImageTest implements EntryPoint {
    private final SearchServiceAsync searchService = GWT.create(SearchService.class);
    List<Integer> resultIds;
    IndexSearchResult lastSearch;
    HashMap<String, Integer> exampleQueries;
    boolean toggleId = true;
    boolean toggleDist, toggleRank;
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
        exampleQueries = new HashMap<>();

        LayoutPanel dp = new LayoutPanel();

        HorizontalPanel title = new HorizontalPanel();
        title.setWidth("100%");
        title.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        title.add(new HTML("<h1>Dino2s Encoded MirFlickr 1M Polyquery</h1>"));

        resultIds = new ArrayList<>();
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Panel imagePanel = new ScrollPanel();

        Panel queryPanelContainer = new HorizontalPanel();
        QueryPanel queryPanel = new QueryPanel();
        queryPanelContainer.add(queryPanel);

        Panel examplePanelContainer = new ScrollPanel();
        examplePanelContainer.setWidth("100%");
        ExamplesPanel examples = new ExamplesPanel(queryPanel);
        examplePanelContainer.add(examples);
        HorizontalPanel timePanel = new HorizontalPanel();

        dp.add(title);
        dp.add(buttonPanel);
        dp.add(queryPanel);
        dp.add(imagePanel);
        dp.add(examplePanelContainer);
        dp.add(timePanel);

        dp.setWidgetTopHeight(buttonPanel, 10, PCT, 10, PCT);
        dp.setWidgetLeftWidth(buttonPanel, 5, PCT, 100, PCT);

        dp.setWidgetTopHeight(title, 0, PCT, 10, PCT);

        dp.setWidgetTopHeight(queryPanel, 20, PCT, 20, PCT);
        dp.setWidgetLeftWidth(queryPanel, 5, PCT, 100, PCT);

        dp.setWidgetTopHeight(imagePanel, 35, PCT, 58, PCT);
        dp.setWidgetLeftWidth(imagePanel, 5, PCT, 100, PCT);

        dp.setWidgetRightWidth(examplePanelContainer, 10, PCT, 10, PCT);
        dp.setWidgetTopHeight(examplePanelContainer, 5, PCT, 90, PCT);

        dp.setWidgetBottomHeight(timePanel, 1, PCT, 5, PCT);
        dp.setWidgetLeftWidth(timePanel, 50, PCT, 100, PCT);
        RootLayoutPanel.get().add(dp);

        Button searchButton = getSearchButton(queryPanel, imagePanel, timePanel);

        CheckBox showIds = new CheckBox();
        showIds.setValue(true);
        CheckBox showDists = new CheckBox();
        CheckBox showRank = new CheckBox();

        showIds.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                toggleId = valueChangeEvent.getValue();
                imagePanel.clear();
                imagePanel.add(new ThumbnailPanel(lastSearch.result, lastSearch.distances, queryPanel.getQueryIds(), showIds.getValue(), showDists.getValue(), showRank.getValue()));
            }
        });

        showDists.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                toggleDist = valueChangeEvent.getValue();
                imagePanel.clear();
                imagePanel.add(new ThumbnailPanel(lastSearch.result, lastSearch.distances, queryPanel.getQueryIds(), showIds.getValue(), showDists.getValue(), showRank.getValue()));
            }
        });

        showRank.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                toggleRank = valueChangeEvent.getValue();
                imagePanel.clear();
                imagePanel.add(new ThumbnailPanel(lastSearch.result, lastSearch.distances, queryPanel.getQueryIds(), showIds.getValue(), showDists.getValue(), showRank.getValue()));
            }
        });



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
                Window.Location.assign("https://similarity.cs.st-andrews.ac.uk/PolyDemo/PolyadicQuery.html");
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
                queryPanel.updateImageIds(new TreeSet<>());
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

        buttonPanel.add(new Label("initialising index Dino2 L2 normed , one moment please..." ));
        //first, load the dino2/msed hnsw onto the server
        try {
            searchService.initialise(new AsyncCallback<String>() {
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
//
                    buttonPanel.add(queryBox);
                    buttonPanel.add(queryIDButton);
                    buttonPanel.add(randomImages);
                    buttonPanel.add(clear);
                    buttonPanel.add(copyButton);
                    buttonPanel.add(helpButton);

                    SimplePanel test = new SimplePanel();
                    buttonPanel.add(new Label("Show ID"));
                    buttonPanel.add(showIds);

                    buttonPanel.add(new Label("Show Dist"));
                    buttonPanel.add(showDists);

                    buttonPanel.add(new Label("Show Rank"));
                    buttonPanel.add(showRank);

                    Image goat = new Image();
                    goat.setUrl("https://avatars.githubusercontent.com/u/126571492?s=200&v=4");
                    goat.setPixelSize(64, 64);
                    title.add(goat);

                }
            });
        } catch (Exception e) {
            Window.alert("oh dear: " + e);
        }

    }

    private Button getSearchButton(QueryPanel queryPanel, Panel imagePanel, HorizontalPanel timePanel) {
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
                            lastSearch = sr;
                            resultIds = sr.result;
                            List<Float> dists = sr.distances;
                            timePanel.clear();
                            imagePanel.add(new ThumbnailPanel(resultIds, dists, queryPanel.getQueryIds(), toggleId, toggleDist, toggleRank));
                            timePanel.add(new HTML("<h3 style=color:red> time: "  + sr.time + "ms</style>"));
//                            Window.alert("Got " + sr.result.size() + " results back");
                        } catch (Exception e) {
                            Window.alert("Uh oh search problem! " + e);
                            Window.alert("Size of result set: " + sr.result.size() + " dists " + sr.distances.size());
                        }
                    }
                });
            }
        });
        return searchButton;
    }
}
