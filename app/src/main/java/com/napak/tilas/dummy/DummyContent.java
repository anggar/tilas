package com.napak.tilas.dummy;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy HH:mm", Locale.getDefault());

        return new DummyItem(String.valueOf(position), "Item " + position, format.format(new Date()));
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String caption;
        public final String details;
        public final String photoUrl;

        public DummyItem(String id, String caption, String details) {
            this.id = id;
            this.caption = caption;
            this.details = details;
            this.photoUrl = "https://picsum.photos/1000/600";
        }

        @NonNull
        @Override
        public String toString() {
            return caption;
        }
    }
}