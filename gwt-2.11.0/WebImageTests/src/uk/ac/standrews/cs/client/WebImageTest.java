package uk.ac.standrews.cs.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import uk.ac.standrews.cs.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private final SearchServiceAsync searchService = GWT.create(SearchService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        doSmokingGirl();
    }

    private void doSmokingGirl() {
        Button button = new Button("show smoking girl!");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                searchService.searchServer("", new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Window.alert("oh fuck");
                    }
                    @Override
                    public void onSuccess(String s) {
                        Window.alert(s);
                    }
                });
                RootPanel.get("smGirlGoesHere").add(new Image("https://similarity.cs.st-andrews.ac.uk/mirflickr/images/0/0.jpg"));
                button.setEnabled(false);
            }
        });
        RootPanel.get("smButtonContainer").add(button);
    }
}
