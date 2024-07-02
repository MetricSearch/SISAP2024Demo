package uk.ac.standrews.cs.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndexTypes implements IsSerializable {
    public IndexTypes() {}

    public enum INDEX_TYPES {
        DINO2 {
            @Override
            public String toString() {
                return "Dino2";
            }
        },
        DINO2_L2 {
            @Override
            public String toString() {
                return "Dino2 L2 Normed";
            }
        }
    }
}

