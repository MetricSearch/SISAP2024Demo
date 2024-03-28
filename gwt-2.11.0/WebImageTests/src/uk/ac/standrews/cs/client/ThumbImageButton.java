package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;

import java.util.List;
import java.util.Set;

public class ThumbImageButton extends VerticalPanel {
    ThumbImageButton(int id, Set<Integer> selectedIds,boolean checkBoxStatus) {
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

        CheckBox cb = new CheckBox();
        cb.setValue(checkBoxStatus);
        cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                if (valueChangeEvent.getValue()) {
                    selectedIds.add(id);
                } else{
                    selectedIds.remove(id);
                }
            }
        });
        this.add(cb);
    }
}
