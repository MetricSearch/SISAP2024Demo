package uk.ac.standrews.cs.shared;

public class IndexTypes {
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

