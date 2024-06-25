package uk.ac.standrews.cs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.Set;

public class ThumbImageButton extends VerticalPanel {

    ThumbImageButton(int id, float dist, Set<Integer> selectedIds, boolean checkBoxStatus, boolean ids, boolean dists, boolean ranks, int rank) {
        Image th = new Image(ThumbnailPanel.getThumbUrl(id));

        StringBuilder sb = new StringBuilder();

        if (ids) {
            sb.append("<b>ID:</b>" + id + "<br>");
        }

        // TODO change
        if (ranks) {
            sb.append("<b>Rank:</b>" + rank + "<br>");
        }

        if (dist != -1 && dists) {
            String formattedDist = NumberFormat.getFormat("0.####E0").format(dist);
            sb.append("<b>Dist:</b> " + formattedDist + "<br>");
            //label = new HTML("<b>ID:</b> " + id + "<br><b>Dist:</b> " + formattedDist);
        }

        Label label = new HTML(sb.toString());


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
