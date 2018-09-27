package com.egtinteractive.item;

import java.math.BigDecimal;
import java.util.Objects;

public final class ItemCounter {

    private static final String INDEX_OUT_OF_BOUNDS_EXCEPTION_MESSAGE = "You can't create an Item Counter with negative index";
    private static final String NULL_POINTER_EXCEPTION_MESSAGE = "Your item cannot be null";

    private final Item item;
    private int count;

    public ItemCounter(final Item item, final int count) {
	if (count < 0) {
	    throw new IndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_EXCEPTION_MESSAGE);
	}
	if (Objects.isNull(item) || Objects.isNull(item.getName())) {
	    throw new NullPointerException(NULL_POINTER_EXCEPTION_MESSAGE);
	}
	this.item = item;
	this.count = count;
    }

    public Item getItem() {
	return this.item;
    }

    public int getCount() {
	return this.count;
    }

    public String getItemName() {
	return this.item.getName();
    }

    public BigDecimal getItemPrice() {
	return this.item.getPrice();
    }

    public void decreaseCount(int count) {
	while (count > 0) {
	    if (this.count > 0) {
		this.count--;
		count--;
	    }
	}
    }

    public ItemCounter increaseCount(final int count) {
	this.count += count;
	return this;
    }

}
