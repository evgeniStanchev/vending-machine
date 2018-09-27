package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.StateMachine;
import com.egtinteractive.vendingmachine.VendingMachine;

public class StandByStateTest extends DataProviders {

    @Test(dataProvider = "machine")
    public void openTheMachine(final VendingMachine vm) {
	final StateMachine expected = StateMachine.SERVICE;
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void takeTheProduct(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	assertEquals(vm.takeItem(itemCounter.getItemName()), itemCounter.getItem());
	final StateMachine expected = StateMachine.STAND_BY;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void firstStateShouldBeStandBy(final VendingMachine vm) {
	final StateMachine expected = StateMachine.STAND_BY;
	final StateMachine actual = vm.getState();
	assertEquals(actual, expected);
    }
}
