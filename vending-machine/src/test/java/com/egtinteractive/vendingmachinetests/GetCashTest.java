package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class GetCashTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void getTheCash(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final BigDecimal expected = vm.getCashInMachine();
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(itemCounter.getItemName()));
	assertEquals(vm.takeItem(itemCounter.getItemName()), itemCounter.getItem());
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	vm.getCash(vm);
	assertEquals(vm.getState(), StateMachine.SERVICE);
	assertEquals(vm.getCashInMachine(), expected);
    }

    @Test(dataProvider = "machine")
    public void getTheCashInStandBy(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final BigDecimal expected = vm.getCashInMachine();
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(itemCounter.getItemName()));
	assertEquals(vm.takeItem(itemCounter.getItemName()), itemCounter.getItem());
	vm.getCash(vm);
	assertEquals(vm.getState(), StateMachine.STAND_BY);
	assertNotEquals(vm.getCashInMachine(), expected);
    }

    @Test(dataProvider = "machine")
    public void getTheCashInTaking(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final BigDecimal expected = vm.getCashInMachine();
	final ItemCounter itemCounter = randomItemCounter(vm);
	itemCounter.increaseCount(1);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(itemCounter.getItemName()));
	vm.getCash(vm);
	assertEquals(vm.getState(), StateMachine.TAKING);
	assertNotEquals(vm.getCashInMachine(), expected);
    }
}
