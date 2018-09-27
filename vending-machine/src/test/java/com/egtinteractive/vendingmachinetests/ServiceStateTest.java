package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class ServiceStateTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void closeTheMachine(final VendingMachine vm) {
	final StateMachine expected = StateMachine.SERVICE;
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertTrue(vm.closeTheMachine());
	final StateMachine actual = vm.getState();
	assertNotEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void cashInMachineCloseToMaxCash(final VendingMachine vm) {
	for (int times = 0; times < vm.getCashLimit().intValue(); times++) {
	    final ItemCounter itemCounter = randomItemCounter(vm);
	    final String name = itemCounter.getItemName();
	    vm.openTheMachine(FIRST_PASSWORD);
	    vm.addItem(itemCounter);
	    vm.closeTheMachine();
	    vm.putCoin(Coin.LEV.getValue());
	    vm.selectItem(name);
	    vm.takeItem(name);
	}
	final StateMachine expected = StateMachine.SERVICE;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void selectItemFromServiceState(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertFalse(vm.selectItem(itemCounter.getItemName()));
    }

    @Test(dataProvider = "machine")
    public void takeItemFromServiceState(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertNull(vm.takeItem(itemCounter.getItemName()));
    }

    @Test(dataProvider = "machine")
    public void putCoinFromServiceState(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertEquals(vm.putCoin(RANDOM_PRICE), BigDecimal.ZERO);
    }
}
