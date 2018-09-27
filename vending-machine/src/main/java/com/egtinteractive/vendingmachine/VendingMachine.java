package com.egtinteractive.vendingmachine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.writer.Writer;

public final class VendingMachine {

    private final Inventory inventory;
    private final Writer writer;
    private final BigDecimal cashLimit;
    private final BigDecimal cashMinimum;
    private final int countLimit;
    private State state;
    private boolean isMachineOpened;
    private String password;
    private BigDecimal cashInMachine;
    private BigDecimal credit;

    public static class Builder {

	private final Writer writer;
	private int countLimit = 10;
	private Inventory inventory = new Inventory();
	private String password = "0000";
	private BigDecimal cashLimit = new BigDecimal("500");
	private BigDecimal cashMinimum = new BigDecimal("100");
	private final BigDecimal credit = BigDecimal.ZERO;
	private final StateMachine state = StateMachine.STAND_BY;

	public Builder(final Writer writer) {
	    this.writer = writer;
	}

	Builder countLimit(final int countLimit) {
	    this.countLimit = countLimit;
	    return this;
	}

	public Builder inventory(final Inventory inventory) {
	    this.inventory = inventory;
	    return this;
	}

	public Builder password(final String password) {
	    this.password = password;
	    return this;
	}

	public Builder cashLimit(final BigDecimal cashLimit) {
	    this.cashLimit = cashLimit;
	    return this;
	}

	public Builder cashMinimum(final BigDecimal cashMinimum) {
	    this.cashMinimum = cashMinimum;
	    return this;
	}

	public VendingMachine build() {
	    return new VendingMachine(this);
	}

    }

    public VendingMachine(final Builder builder) {
	this.password = builder.password;
	this.countLimit = builder.countLimit;
	this.writer = builder.writer;
	this.state = builder.state;
	this.cashMinimum = builder.cashMinimum;
	this.cashInMachine = this.cashMinimum;
	this.cashLimit = builder.cashLimit;
	this.credit = builder.credit;
	this.inventory = builder.inventory;
    }

    private boolean makeItem(final String item) {
	final boolean itemIsReady = state.makeItem(this, item);
	printCurrentScreen();
	return itemIsReady;
    }

    private void printCurrentScreen() {
	final StringBuilder sb = new StringBuilder();
	sb.append(System.lineSeparator());
	sb.append(System.lineSeparator());
	sb.append("******************************************");
	sb.append(System.lineSeparator());
	sb.append("**************CREDIT:");
	sb.append(this.credit.setScale(2, RoundingMode.CEILING));
	sb.append("*****************");
	sb.append(System.lineSeparator());
	sb.append("******************************************");
	sb.append(System.lineSeparator());
	sb.append(String.format("%s %32s", "PRODUCTS:", "PRICES:"));
	sb.append(System.lineSeparator());
	sb.append("------------------------------------------");
	sb.append(System.lineSeparator());
	for (Map.Entry<String, ItemCounter> entry : this.inventory.items.entrySet()) {
	    ItemCounter itemCounter = entry.getValue();
	    final String name = entry.getKey();
	    final BigDecimal price = itemCounter.getItemPrice().setScale(2, RoundingMode.CEILING);

	    sb.append(String.format("%-37s %s", name, price));
	    sb.append(System.lineSeparator());
	}
	sb.append("******************************************");
	sb.append(System.lineSeparator());
	sb.append(System.lineSeparator());
	writer.write(sb.toString());

    }

    void setState(final StateMachine state) {
	this.state = state;
    }

    void setCredit(BigDecimal credit) {
	this.credit = credit;
    }

    void setCashInMachine(BigDecimal cashInMachine) {
	this.cashInMachine = cashInMachine;
    }

    Writer getWriter() {
	return this.writer;
    }

    String getPassword() {
	return this.password;
    }

    Inventory getInventory() {
	return this.inventory;
    }

    BigDecimal getCashMinimum() {
	return cashMinimum;
    }

    public StateMachine getState() {
	return (StateMachine) this.state;
    }

    public BigDecimal getCashInMachine() {
	return cashInMachine;
    }

    public int getItemCount(final String name) {
	return this.inventory.getItemCount(name);
    }

    public int getCountLimit() {
	return countLimit;
    }

    public boolean itemExists(final String name) {
	return this.inventory.itemExists(name);
    }

    public boolean machineIsOpened() {
	return this.isMachineOpened;
    }

    public BigDecimal getCredit() {
	return this.credit;
    }

    public BigDecimal getCashLimit() {
	return this.cashLimit;
    }

    public BigDecimal returnMoney() {
	final BigDecimal change = state.ejectMoney(this);
	printCurrentScreen();
	return change;
    }

    public BigDecimal getCash(final VendingMachine vm) {
	BigDecimal money = state.getCash(vm, this.cashInMachine, this.cashMinimum);
	return money;
    }

    public String changePassword(final String oldPassword, final String newPassword) {
	final String password = state.changePassword(this, oldPassword, newPassword);
	this.password = password;
	return password;
    }

    public ItemCounter addItem(final ItemCounter itemCounter) {
	final ItemCounter addedItemCounter = state.addItem(this, itemCounter);
	printCurrentScreen();
	return addedItemCounter;
    }

    public ItemCounter removeItem(final ItemCounter itemCounter) {
	final ItemCounter removedItem = state.removeItem(this, itemCounter);
	printCurrentScreen();
	return removedItem;
    }

    public boolean openTheMachine(final String password) {
	this.isMachineOpened = state.openTheMachine(this, password, this.password);
	printCurrentScreen();
	return this.isMachineOpened;
    }

    public boolean closeTheMachine() {
	this.isMachineOpened = !state.closeTheMachine(this);
	return !this.isMachineOpened;
    }

    public BigDecimal putCoin(final BigDecimal coin) {
	state.putCoin(this, coin);
	return this.credit;
    }

    public boolean selectItem(final String item) {
	printCurrentScreen();
	final boolean itemIsSelectedSuccessfully = state.selectItem(this, item);
	if (itemIsSelectedSuccessfully) {
	    makeItem(item);
	}
	return itemIsSelectedSuccessfully;
    }

    public Item takeItem(final String name) {
	final Item item = state.takeItem(this, name);

	return item;
    }
}
