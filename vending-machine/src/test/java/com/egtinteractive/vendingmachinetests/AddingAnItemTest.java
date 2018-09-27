package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;
import org.testng.annotations.Test;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class AddingAnItemTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void addingAnItemInStateStandBy(final VendingMachine vm) {
	final StateMachine expected = StateMachine.STAND_BY;
	assertNull(vm.addItem(randomItemCounter(vm)));
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void addItemWithZeroCount(final VendingMachine vm) {
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, 0);
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertNull(vm.addItem(itemCounter));
    }

    @Test(dataProvider = "machine")
    public void addingAnItemWhileMachineIsNotOpened(final VendingMachine vm) {
	final StateMachine expected = vm.getState();
	assertNull(vm.addItem(randomItemCounter(vm)));
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void addingAnItemThatDoesntExists(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int expected = ThreadLocalRandom.current().nextInt(1, vm.getCountLimit());
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, expected);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	final int actual = itemCounter.getCount();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void addingAnItemCorrectly(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int someCount = ThreadLocalRandom.current().nextInt(1, (vm.getCountLimit() / 2));
	final int expected = someCount * 2;
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, someCount);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	final int actual = itemCounter.getCount();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void addingAnItemWithBiggerCountThanLimit(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int someCount = ThreadLocalRandom.current().nextInt((vm.getCountLimit() / 2) + 1, vm.getCountLimit());
	final int expected = someCount;
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, someCount);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertNull(vm.addItem(itemCounter));
	final int actual = vm.getItemCount(RANDOM_NAME);
	assertEquals(actual, expected);
    }

}
