package com.egtinteractive.vendingmachinetests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import org.testng.annotations.Test;

import com.egtinteractive.coin.Coin;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.VendingMachine;

public class ReturnMoneyTest extends DataProviders {
    @Test(dataProvider = "machine")
    public void getTheMoneyInTheCredit(final VendingMachine vm) {
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertEquals(vm.returnMoney(), Coin.LEV.getValue());
	final BigDecimal expected = BigDecimal.ZERO;
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }

    @Test(dataProvider = "machine")
    public void selectAProductCreditMustBeZero(final VendingMachine vm) {
	assertTrue(vm.openTheMachine(FIRST_PASSWORD));
	final ItemCounter itemCounter = randomItemCounter(vm);
	assertEquals(vm.addItem(itemCounter), itemCounter);
	assertTrue(vm.closeTheMachine());
	assertEquals(vm.putCoin(Coin.LEV.getValue()), vm.getCredit());
	assertTrue(vm.selectItem(RANDOM_NAME));
	final BigDecimal expected = BigDecimal.ZERO;
	final BigDecimal actual = vm.getCredit();
	assertEquals(actual, expected);
    }
}
