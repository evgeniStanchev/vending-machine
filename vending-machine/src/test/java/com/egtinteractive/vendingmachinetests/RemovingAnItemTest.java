package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import org.testng.annotations.Test;
import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class RemovingAnItemTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void removeItemsFromOneType(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int count = ThreadLocalRandom.current().nextInt(2, vm.getCountLimit());
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, count);
	final ItemCounter itemCounterForRemove = new ItemCounter(item, count - 1);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	final int expected = itemCounter.getCount() - itemCounterForRemove.getCount();
	assertEquals(vm.removeItem(itemCounterForRemove), itemCounterForRemove);
	assertTrue(vm.closeTheMachine());
	final int actual = itemCounter.getCount();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void removeAllItemsFromOneType(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertEquals(vm.removeItem(itemCounter), itemCounter);
	final boolean expected = false;
	final boolean actual = vm.itemExists(RANDOM_NAME);
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void removeAndStateStaysService(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertEquals(vm.removeItem(itemCounter), itemCounter);
	final StateMachine expected = StateMachine.SERVICE;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void tryingToRemoveMoreItemsThanExisting(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	final int expected = itemCounter.getCount();
	assertEquals(vm.removeItem(itemCounter), itemCounter);
	final int actual = itemCounter.getCount();
	assertEquals(actual, expected);
    }
}
