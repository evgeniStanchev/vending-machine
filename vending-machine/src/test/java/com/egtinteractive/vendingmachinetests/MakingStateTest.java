package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class MakingStateTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void selectItemFromServiceState(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertFalse(vm.selectItem(RANDOM_NAME));
	assertTrue(vm.getState().equals(StateMachine.SERVICE));
    }

    @Test(dataProvider = "machine")
    public void selectItemWithNotEnoughCredit(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertTrue(vm.closeTheMachine());
	final String RANDOM_NAME = "Bake rolls";
	assertEquals(vm.putCoin(Coin.FIVE.getValue()), vm.getCredit());
	final BigDecimal expected = vm.getCredit();
	assertFalse(vm.selectItem(RANDOM_NAME));
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
	assertTrue(vm.getState().equals(StateMachine.STAND_BY));
    }

    @Test(dataProvider = "machine")
    public void selectNoExistingItem(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()),vm.getCredit());
	assertEquals(vm.putCoin(Coin.LEV.getValue()),vm.getCredit());
	final BigDecimal expected = vm.getCredit();
	assertFalse(vm.selectItem(RANDOM_NAME));
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
	assertTrue(vm.getState().equals(StateMachine.STAND_BY));
    }

}
