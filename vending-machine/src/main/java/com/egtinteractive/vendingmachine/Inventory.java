package com.egtinteractive.vendingmachine;

import java.util.HashMap;
import java.util.Map;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;

public final class Inventory {

    final Map<String, ItemCounter> items = new HashMap<>();

    Item getItem(final String name) {
	return this.items.get(name).getItem();
    }

    ItemCounter getItemCounter(final String name) {
	return this.items.get(name);
    }

    void putItem(final ItemCounter itemCounter) {
	if (itemCounter.getCount() != 0)
	    items.put(itemCounter.getItemName(), itemCounter);
    }

    void removeItemCounter(final ItemCounter itemCounter) {
	this.items.remove(itemCounter.getItemName());
    }

    boolean itemExists(final String name) {
	if (this.items.containsKey(name)) {
	    return true;
	}
	return false;
    }

    int getItemCount(final String name) {
	if (itemExists(name)) {
	    return this.items.get(name).getCount();
	} else {
	    return 0;
	}
    }

    void decreaseItemCount(final String name, final int count) {
	this.items.get(name).decreaseCount(count);
    }

    void increaseItemCount(final String name, final int count) {
	final ItemCounter itemCounter = items.get(name);
	itemCounter.increaseCount(count);
    }

}
