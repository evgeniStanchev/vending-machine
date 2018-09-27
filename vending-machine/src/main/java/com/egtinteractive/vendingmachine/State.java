
package com.egtinteractive.vendingmachine;

import java.math.BigDecimal;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;

public interface State {

    public String changePassword(final VendingMachine vm, final String oldPassword, final String newPassword);

    public boolean openTheMachine(final VendingMachine vm, final String suggestedPassword,
	    final String machinePassword);

    public boolean closeTheMachine(final VendingMachine vm);

    public ItemCounter addItem(final VendingMachine vendingMachine, final ItemCounter item);

    public ItemCounter removeItem(final VendingMachine vm, final ItemCounter item);

    public BigDecimal ejectMoney(final VendingMachine vm);

    public BigDecimal putCoin(final VendingMachine vm, final BigDecimal coin);

    public boolean selectItem(final VendingMachine vm, final String name);

    public boolean makeItem(final VendingMachine vm, final String name);

    public Item takeItem(final VendingMachine vm, final String name);

    public BigDecimal getCash(final VendingMachine vm, final BigDecimal cashInMachine, final BigDecimal cashMinimum);

}
