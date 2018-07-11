package com.zhxh.xchart.dummy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhxh.xchart.ChartData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    static String testResult = "[{\"logDay\":\"20160912\",\"yield\":\"0\"},{\"logDay\":\"20161021\",\"yield\":\"126.73\"},{\"logDay\":\"20161123\",\"yield\":\"170.76\"},{\"logDay\":\"20161226\",\"yield\":\"251.48\"},{\"logDay\":\"20170203\",\"yield\":\"260.03\"},{\"logDay\":\"20170308\",\"yield\":\"282.79\"},{\"logDay\":\"20170412\",\"yield\":\"283.97\"},{\"logDay\":\"20170516\",\"yield\":\"367.10\"},{\"logDay\":\"20170620\",\"yield\":\"275.20\"},{\"logDay\":\"20170721\",\"yield\":\"148.27\"},{\"logDay\":\"20170823\",\"yield\":\"279.44\"},{\"logDay\":\"20170925\",\"yield\":\"401.01\"},{\"logDay\":\"20171102\",\"yield\":\"425.70\"},{\"logDay\":\"20171205\",\"yield\":\"401.86\"},{\"logDay\":\"20180108\",\"yield\":\"430.53\"},{\"logDay\":\"20180208\",\"yield\":\"308.31\"},{\"logDay\":\"20180320\",\"yield\":\"359.98\"},{\"logDay\":\"20180424\",\"yield\":\"337.41\"},{\"logDay\":\"20180529\",\"yield\":\"314.25\"},{\"logDay\":\"20180709\",\"yield\":\"230.41\"}]";

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

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
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public List<ChartData> testDatas;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.testDatas = new Gson().fromJson(testResult, new TypeToken<List<ChartData>>() {
            }.getType());
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
