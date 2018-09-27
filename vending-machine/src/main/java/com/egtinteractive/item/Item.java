package com.egtinteractive.item;

import java.math.BigDecimal;

public final class Item {

    private final String name;
    private final BigDecimal price;

    public Item(final String name, final BigDecimal price) {
	this.price = price;
	this.name = name;
    }

    public final BigDecimal getPrice() {
	return price;
    }

    public final String getName() {
	return name;
    }
}
