package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.Set;

public class ThumbImageButton extends VerticalPanel {
    public static boolean showID;
    public static boolean showDistancel;

    ThumbImageButton(int id, float dist, Set<Integer> selectedIds, boolean checkBoxStatus) {


        Image th = new Image(ThumbnailPanel.getThumbUrl(id));
        Label label;
        if (dist != -1) {
            String formattedDist = NumberFormat.getFormat("0.####E0").format(dist);
            label  = new HTML("<b>ID:</b> " + id + "<br><b>Dist:</b> " + formattedDist);
        } else {
            label = new Label("ID: " + id);
        }

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
        this.add(label);

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
