package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.Set;

public class ExamplesPanel extends VerticalPanel {
    HashMap<String, Integer> exampleQueries;

    public ExamplesPanel(QueryPanel queryPanel) {
        this.add(new HTML("<h2>Example Images</h2>"));
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

        /**
         * Bottle
         *  ids: 999128,269098,205819 Burger
         *  ids: 238785,518748,779454
         */
        map.put("Bugatti", 181360);
        map.put("Dog", 370921);
        map.put("Peacock", 101102);
        map.put("Blue Tit", 263209);
        map.put("Clownfish", 224598);
        map.put("Burger1", 999128);
        map.put("Burger2", 269098);
        map.put("Burger3", 205819);

        map.put("Bottle", 808080);
        map.put("Bottle1", 999128);
        map.put("Bottle2", 269098);
        map.put("Bottle3", 205819);

        map.put("Train", 95685);

        map.put("BlueFlower1", 392663);
        map.put("BlueFlower2", 490535);

        map.put("Windmill", 196632);

        map.put("Beach", 30442);

        map.put("Police Car", 277685);

        map.put("Paddy Field", 482589);

        map.put("Submarine", 745180);

        map.put("Insect", 588519);

        map.put("Sunset", 513890);
        return map;
    }
}
