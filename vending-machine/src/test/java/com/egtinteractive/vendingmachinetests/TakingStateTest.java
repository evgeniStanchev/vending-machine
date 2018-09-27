package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class TakingStateTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void tryingToTakeAnItemInDifferentStateThanTaking(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	assertNull(vm.takeItem(item.getName()));
	assertTrue(Objects.equals(vm.getState(), StateMachine.SERVICE));
    }

    @Test(dataProvider = "machine")
    public void selectItemCorrectly(final VendingMachine vm) {
	final StateMachine expected = StateMachine.TAKING;
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void takeItemCorrectly(final VendingMachine vm) {
	final StateMachine expected = StateMachine.STAND_BY;
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	assertEquals(vm.takeItem(itemCounter.getItemName()), itemCounter.getItem());
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void takeItemCorrectlyDecreaseCount(final VendingMachine vm) {

	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int count = ThreadLocalRandom.current().nextInt(1, 10);
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, count);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	vm.takeItem(RANDOM_NAME);
	assertTrue(vm.getItemCount(RANDOM_NAME) == count - 1);
    }

    @Test(dataProvider = "machine")
    public void takeThaLastProductFromThisKind(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final int count = ThreadLocalRandom.current().nextInt(1, vm.getCountLimit());
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, count);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	for (int i = 0; i < count; i++) {
	    assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	    assertTrue(vm.selectItem(RANDOM_NAME));
	    vm.takeItem(RANDOM_NAME);
	}
	assertFalse(vm.itemExists(RANDOM_NAME));
    }
}
