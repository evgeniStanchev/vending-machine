package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class SelectItemTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void selectItemIncorrectly(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertFalse(vm.selectItem(RANDOM_NAME));
	assertTrue(vm.getState().equals(StateMachine.STAND_BY));
    }

    @Test(dataProvider = "machine")
    public void selectItemCorrectly(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());

	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	assertTrue(vm.getState().equals(StateMachine.TAKING));
    }

    @Test(dataProvider = "machine")
    public void selectCorrectlyCreditChange(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	final BigDecimal expected = vm.getCredit().subtract(RANDOM_PRICE);
	assertTrue(vm.selectItem(RANDOM_NAME));
	vm.takeItem(RANDOM_NAME);
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void selectTwoTimesWithoutAddingCoinsSecondTime(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	itemCounter.increaseCount(1);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	while (itemCounter.getItemPrice().pow(2).compareTo(vm.getCredit()) >= 0) {
	    assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	}
	assertTrue(vm.selectItem(RANDOM_NAME));
	vm.takeItem(RANDOM_NAME);
	assertNotNull(vm.selectItem(RANDOM_NAME));
    }

    @Test(dataProvider = "machine")
    public void changeShouldIncreaseIfThereIsSo(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	final BigDecimal expected = BigDecimal.ZERO;
	assertTrue(vm.selectItem(RANDOM_NAME));
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }
}
