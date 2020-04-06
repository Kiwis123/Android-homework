package com.example.chapter3.homework;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private static final long serialVersionUID = -6099312954099962806L;
    private static String[] index = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};
    private String title;
    private String body;


    public Item(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public static ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        for(int i = 0; i < 10; i++){
            items.add(new Item("Item " + i, "This is the " + index[i] + " item"));
        }
        return items;
    }

    @Override
    public String toString() {
        return getTitle();
    }

}
