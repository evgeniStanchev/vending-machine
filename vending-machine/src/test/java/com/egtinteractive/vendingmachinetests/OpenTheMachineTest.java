package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class OpenTheMachineTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void openTheMachineByCorrectPassword(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertTrue(vm.machineIsOpened());
	assertTrue(vm.getState().equals(StateMachine.SERVICE));
    }

    @Test(dataProvider = "machine")
    public void openTheMachineTheMachineIsAlreadyOpened(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	assertFalse(vm.openTheMachine(FIRST_PASSWORD));
    }

    @Test(dataProvider = "machine")
    public void openTheMachineByIncorrectpassword(final VendingMachine vm) {
	final StateMachine expected = vm.getState();
	assertFalse(vm.openTheMachine(NEW_PASSWORD));
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
	assertFalse(vm.machineIsOpened());
    }

}
