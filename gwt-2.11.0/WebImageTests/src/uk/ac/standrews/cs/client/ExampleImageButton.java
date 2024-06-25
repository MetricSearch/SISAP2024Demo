package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.Set;

public class ExampleImageButton extends VerticalPanel {

    public ExampleImageButton(int id) {
        Image th = new Image(ThumbnailPanel.getThumbUrl(id));


        th.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                PopupPanel pp = new PopupPanel();
                Image im = new Image(ThumbnailPanel.getFullUrl(id));
                pp.add(im);
                im.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        pp.hide();
                    }
                });
                pp.show();
            }
        });
        this.add(th);
    }
}
