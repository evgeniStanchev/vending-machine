package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class CoinsTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void insertingCoinsCreditIncreaseCorrectly(final VendingMachine vm) {
	assertEquals(vm.putCoin(Coin.FIFTY.getValue()), Coin.FIFTY.getValue());
	final BigDecimal expected = Coin.FIFTY.getValue();
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void ejectMoneyCorrectly(final VendingMachine vm) {
	assertEquals(vm.putCoin(Coin.FIFTY.getValue()), Coin.FIFTY.getValue());
	assertEquals(vm.returnMoney(), Coin.FIFTY.getValue());
	final BigDecimal expected = BigDecimal.ZERO;
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void machineFullOfCash(final VendingMachine vm) {
	final int maxCash = vm.getCashLimit().intValue();
	for (int i = 0; i < maxCash; i++) {
	    final ItemCounter itemCounter = randomItemCounter(vm);
	    final String name = itemCounter.getItem().getName();
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
    public void putCoinsWhileTheMachineIsFull(final VendingMachine vm) {
	for (int i = 0; i < vm.getCashLimit().intValue() + 100; i++) {
	    vm.putCoin(RANDOM_PRICE);
	}
	final BigDecimal expected = vm.getCashLimit().subtract(vm.getCashInMachine()).subtract(RANDOM_PRICE);
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void putNullValue(final VendingMachine vm) {
	final BigDecimal actual = vm.putCoin(null);
	final BigDecimal expected = BigDecimal.ZERO;
	assertEquals(actual, expected);
    }
}
