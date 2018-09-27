package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class ChangingThePasswordTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void changingThePasswordCorrectly(final VendingMachine vm) {
	assertEquals(vm.changePassword(FIRST_PASSWORD, NEW_PASSWORD), NEW_PASSWORD);
	assertTrue(vm.openTheMachine(NEW_PASSWORD));
	final StateMachine expected = StateMachine.SERVICE;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void changingThePasswordIncorrectly(final VendingMachine vm) {
	assertEquals(vm.changePassword("00100", NEW_PASSWORD), FIRST_PASSWORD);
	final StateMachine expected = StateMachine.STAND_BY;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void changingThePasswordInServiceMode(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertEquals(vm.changePassword(FIRST_PASSWORD, NEW_PASSWORD), FIRST_PASSWORD);
	assertTrue(vm.closeTheMachine());
	assertFalse(vm.openTheMachine(NEW_PASSWORD));
    }

    @Test(dataProvider = "machine")
    public void changingThePasswordInTakingMode(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	vm.addItem(randomItemCounter(vm));
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	assertEquals(vm.changePassword(FIRST_PASSWORD, NEW_PASSWORD), FIRST_PASSWORD);
    }
}
