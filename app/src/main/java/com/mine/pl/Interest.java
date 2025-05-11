package com.mine.pl;

/** Simple POJO representing a selectable interest/topic. */
public class Interest {
    private String id;
    private String label;

    // Required for Gson or other deserializers
    public Interest() {}

    public Interest(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
