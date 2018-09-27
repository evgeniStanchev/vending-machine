package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import org.testng.annotations.Test;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.VendingMachine;

public class ExceptionsTest extends DataProviders {

    @Test(dataProvider = "machine", expectedExceptions = NullPointerException.class)
    public void addingANullItem(final VendingMachine vm) throws NullPointerException {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	vm.addItem(null);
    }

    @Test(dataProvider = "machine", expectedExceptions = IndexOutOfBoundsException.class)
    public void creatAnItemCounterWithNegativeCount(final VendingMachine vm) throws IndexOutOfBoundsException {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	int count = ThreadLocalRandom.current().nextInt();
	if (count > 0) {
	    count = -count;
	}
	final ItemCounter itemCounter = new ItemCounter(item, count);
	vm.addItem(itemCounter);
    }

    @Test(dataProvider = "machine", expectedExceptions = NullPointerException.class)
    public void createItemCountWithItemNameNull(final VendingMachine vm) throws NullPointerException {
	final int count = ThreadLocalRandom.current().nextInt(1, 10);
	final Item item = new Item(null, null);
	@SuppressWarnings("unused")
	final ItemCounter itemCounter = new ItemCounter(item, count);
    }

    @Test(dataProvider = "machine", expectedExceptions = IndexOutOfBoundsException.class)
    public void removeWithNegativeCount(final VendingMachine vm) throws IndexOutOfBoundsException {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	int count = ThreadLocalRandom.current().nextInt();
	if (count > 0) {
	    count = -count;
	}
	final ItemCounter itemCounter = new ItemCounter(item, count);
	vm.removeItem(itemCounter);
    }

}
