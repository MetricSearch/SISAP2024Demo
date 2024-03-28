package uk.ac.standrews.cs.client;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ThumbnailPanel extends VerticalPanel {

    ThumbnailPanel(List<Integer> ids, Set<Integer> chosenIds) {
        Panel currentHPanel = new HorizontalPanel();
        int noOfImagesAdded = 0;
        for (int id : ids) {
            if (noOfImagesAdded != 0 && noOfImagesAdded % 10 == 0) {
                this.add(currentHPanel);
                currentHPanel = new HorizontalPanel();
            }
            currentHPanel.add(new ThumbImageButton(id, chosenIds,false));
            noOfImagesAdded++;
        }
        this.add(currentHPanel);
    }

    protected static String getFullUrl(int imageId) {
        imageId = imageId - 1; //numbering all images from 1 instead of 0
        String template = "https://similarity.cs.st-andrews.ac.uk/mirflickr/images/**dirNo**/**imNo**.jpg";
        String dir = "" + ((imageId / 10000));
        String imNo = "" + imageId;
        return template.replace("**dirNo**", dir).replace("**imNo**", imNo);
    }

    protected static String getThumbUrl(int imageId) {
        imageId = imageId - 1; //numbering all images from 1 instead of 0
        String template = "https://similarity.cs.st-andrews.ac.uk/mirflickr/thumbs/**dirNo**/im**imNo**.jpg";
        String dir = "" + ((imageId / 10000) + 1);
        String imNo = "" + (imageId + 1);
        return template.replace("**dirNo**", dir).replace("**imNo**", imNo);
    }
}
