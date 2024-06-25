package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.HashMap;
import java.util.Set;

public class ExamplesPanel extends VerticalPanel {
    HashMap<String, Integer> exampleQueries;

    public ExamplesPanel(QueryPanel queryPanel) {
        DisclosurePanel examples = new DisclosurePanel("Examples");
        examples.setAnimationEnabled(true);

        exampleQueries = addExampleQueries();

        for (String s : exampleQueries.keySet()) {
            Image example = new Image(ThumbnailPanel.getThumbUrl(exampleQueries.get(s)));
            example.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    Set<Integer> newIds = queryPanel.getQueryIds();
                    newIds.add(exampleQueries.get(s));
                    queryPanel.updateImageIds(newIds);
                }
            });
            this.add(example);
        }

//
//        Button openExamples = new Button("Toggle Options", new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent clickEvent) {
//                examples.setOpen(!examples.isOpen());
//            }
//        });
    }

    private HashMap<String, Integer> addExampleQueries() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Bugatti", 181360);
        map.put("Bottle", 808080);
        map.put("Dog", 370921);
        map.put("Peacock", 101102);
        map.put("Blue Tit", 263209);
        map.put("Clownfish", 224598);

        return map;
    }
}
