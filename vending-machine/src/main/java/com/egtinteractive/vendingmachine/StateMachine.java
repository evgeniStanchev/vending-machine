package com.egtinteractive.vendingmachine;

import java.math.BigDecimal;
import java.util.Objects;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import static com.egtinteractive.writer.Message.*;

public enum StateMachine implements State {

    STAND_BY {
	@Override
	public String changePassword(final VendingMachine vm, final String oldPassword, final String newPassword) {
	    if (!Objects.equals(oldPassword, vm.getPassword())) {
		vm.getWriter().write(OLD_PASSWORD_IS_INCORRECT_MESSAGE.getMessage());
		return vm.getPassword();
	    }
	    if (newPassword.length() < 4) {
		write(INCORRECT_PASSWORD_CHANGING_MESSAGE.getMessage(), vm);
		return oldPassword;
	    }
	    write(CHANGING_PASSWORD_SUCCESSFULLY_MESSAGE.getMessage(), vm);
	    return newPassword;
	}

	@Override
	public boolean openTheMachine(final VendingMachine vm, final String suggestedPassword,
		final String machinePassword) {
	    if (!vm.getState().equals(STAND_BY)) {
		write(NEED_STANDBY_MODE_MESSAGE.getMessage(), vm);
		return false;
	    } else {
		if (!Objects.equals(suggestedPassword, machinePassword)) {
		    write(INCORRECT_PASSWORD_CHANGING_MESSAGE.getMessage(), vm);
		    return false;
		} else {
		    vm.setState(SERVICE);
		    write(MACHINE_OPENED_SUCCESSFULLY_MESSAGE.getMessage(), vm);
		    return true;
		}
	    }
	}

	@Override
	public BigDecimal ejectMoney(final VendingMachine vm) {
	    if (vm.getCredit().equals(BigDecimal.ZERO)) {
		write(NOT_ENOUGH_MONEY_MESSAGE.getMessage(), vm);
		return BigDecimal.ZERO;
	    } else {
		final BigDecimal change = vm.getCredit();
		vm.setCredit(BigDecimal.ZERO);
		write(YOU_CAN_TAKE_YOUR_MONEY_MESSAGE.getMessage(), vm);
		return change;
	    }
	}

	@Override
	public BigDecimal putCoin(final VendingMachine vm, final BigDecimal coin) {
	    if (Objects.isNull(coin)) {
		return BigDecimal.ZERO;
	    }
	    boolean isMachineFull = false;
	    if (!Objects.equals(coin, BigDecimal.ZERO)) {
		if (vm.getCashInMachine().add(coin).add(vm.getCredit()).compareTo(vm.getCashLimit()) >= 0) {
		    isMachineFull = true;
		}
	    }
	    if (isMachineFull) {
		if (vm.getCredit().compareTo(BigDecimal.ONE) <= 0) {
		    vm.setState(StateMachine.SERVICE);
		    write(SORRY_WE_NEED_A_REPAIR_MESSAGE.getMessage(), vm);
		    return BigDecimal.ZERO;
		} else {
		    write(NO_MORE_COINS_MESSAGE.getMessage(), vm);
		    return BigDecimal.ZERO;
		}
	    }
	    write(PUT_COIN_SUCCESSFULLY_MESSAGE.getMessage(), vm);
	    vm.setCredit(vm.getCredit().add(coin));
	    return coin;
	}

	@Override
	public boolean selectItem(final VendingMachine vm, final String name) {
	    if (!vm.getInventory().itemExists(name)) {
		write(ITEM_DOESNT_EXISTS_MESSAGE.getMessage(), vm);
		return false;
	    } else {
		final BigDecimal itemPrice = vm.getInventory().getItem(name).getPrice();
		if (vm.getCredit().compareTo(itemPrice) < 0) {
		    write(NEED_MORE_COINS_MESSAGE.getMessage(), vm);
		    return false;
		} else {
		    final BigDecimal newCredit = vm.getCredit().subtract(itemPrice);
		    if (!newCredit.equals(BigDecimal.ZERO)) {
			write(YOU_CAN_TAKE_YOUR_MONEY_MESSAGE.getMessage(), vm);
		    }
		    write(SELECTING_AN_ITEM_SUCCESSFULLY_MESSAGE.getMessage(), vm);
		    vm.setState(MAKING);
		    vm.setCredit(vm.getCredit().subtract(itemPrice));
		    vm.setCashInMachine(vm.getCashInMachine().add(itemPrice));
		    return true;
		}
	    }
	}
    },
    MAKING {

	@Override
	public boolean makeItem(final VendingMachine vm, final String item) {
	    write(MAKING_AN_ITEM_MESSAGE.getMessage(), vm);
	    vm.setState(TAKING);
	    vm.getWriter().write("\n");
	    return true;
	}
    },
    SERVICE {
	@Override
	public boolean closeTheMachine(final VendingMachine vm) {
	    write(CLOSING_THE_MACHINE_SUCCESSFULLY_MESSAGE.getMessage(), vm);
	    vm.setState(STAND_BY);
	    return true;
	}

	@Override
	public ItemCounter addItem(final VendingMachine vm, final ItemCounter itemCounter) {

	    if (Objects.isNull(itemCounter)) {
		throw new NullPointerException(NULL_POINTER_MESSAGE.getMessage());
	    }

	    final String name = itemCounter.getItemName();
	    final int count = itemCounter.getCount();

	    if (count == 0) {
		return null;
	    }

	    if (vm.getCountLimit() < count + vm.getInventory().getItemCount(name)) {
		vm.getWriter().write(PUT_ITEMS_FAILURE.getMessage() + (vm.getCountLimit() - count));
		return null;
	    }

	    if (vm.getInventory().itemExists(name)) {
		vm.getInventory().increaseItemCount(name, count);
	    } else {
		if (itemCounter.getCount() != 0) {
		    vm.getInventory().putItem(itemCounter);
		    write(ADD_ITEM_SUCCESSFULLY_MESSAGE.getMessage() + count + " " + name, vm);
		}
	    }

	    return itemCounter;
	}

	@Override
	public ItemCounter removeItem(final VendingMachine vm, final ItemCounter itemCounter) {
	    final int countToRemove = itemCounter.getCount();
	    if (Objects.isNull(itemCounter)) {
		throw new NullPointerException(NULL_POINTER_MESSAGE.getMessage());
	    }
	    if (countToRemove < 1) {
		throw new IndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE.getMessage());
	    }
	    if (!vm.getInventory().itemExists(itemCounter.getItemName())) {
		write(ITEM_DOESNT_EXISTS_MESSAGE.getMessage(), vm);
		return null;
	    }

	    final String itemName = itemCounter.getItemName();
	    final int itemCount = vm.getInventory().getItemCount(itemCounter.getItemName());

	    if (itemCount > countToRemove) {
		vm.getWriter().write(getRemoveMessage(vm, itemName, countToRemove, true, false));
		vm.getInventory().decreaseItemCount(itemName, countToRemove);
	    } else if (vm.getInventory().getItemCount(itemName) == countToRemove) {
		vm.getInventory().removeItemCounter(itemCounter);
		vm.getWriter().write(getRemoveMessage(vm, itemName, countToRemove, true, true));
	    } else {
		vm.getWriter().write(getRemoveMessage(vm, itemName, countToRemove, false, false));
		return null;
	    }
	    return itemCounter;
	}

	@Override
	public BigDecimal getCash(final VendingMachine vm, final BigDecimal cashInMachine,
		final BigDecimal cashMinimum) {
	    if (cashInMachine.compareTo(cashMinimum) > 0) {
		BigDecimal money = vm.getCashInMachine().subtract(vm.getCashMinimum());
		vm.setCashInMachine(vm.getCashInMachine().subtract(money));
		write(YOU_CAN_TAKE_YOUR_MONEY_MESSAGE.getMessage(), vm);
		return money;
	    } else {
		write(NOT_ENOUGH_MONEY_MESSAGE.getMessage(), vm);
		return BigDecimal.ZERO;
	    }
	}
    },

    TAKING {
	@Override
	public Item takeItem(final VendingMachine vm, final String name) {
	    if (!vm.getInventory().itemExists(name)) {
		return null;
	    }
	    final Item item = vm.getInventory().getItem(name);
	    vm.getInventory().decreaseItemCount(name, 1);
	    vm.setState(StateMachine.STAND_BY);
	    if (vm.getInventory().getItemCount(name) == 0) {
		final ItemCounter itemCounter = vm.getInventory().getItemCounter(name);
		vm.getInventory().removeItemCounter(itemCounter);
	    }
	    write(TAKE_AN_ITEM_SUCCESSFULLY_MESSAGE.getMessage(), vm);
	    return item;
	}
    };

    @Override
    public String changePassword(final VendingMachine vm, final String oldPassword, final String newPassword) {
	vm.getWriter().write(NEED_STANDBY_MODE_MESSAGE.getMessage());
	return vm.getPassword();
    }

    @Override
    public boolean openTheMachine(final VendingMachine vm, final String suggestedPassword,
	    final String machinePassword) {
	vm.getWriter().write(NEED_STANDBY_MODE_MESSAGE.getMessage());
	return false;
    }

    @Override
    public boolean closeTheMachine(final VendingMachine vm) {
	vm.getWriter().write(MACHINE_IS_CLOSED_ALREADY_MESSAGE.getMessage());
	return false;
    }

    @Override
    public ItemCounter addItem(final VendingMachine vm, final ItemCounter itemCounter) {
	vm.getWriter().write(MACHINE_IS_NOT_OPENED_MESSAGE.getMessage());
	return null;
    }

    @Override
    public ItemCounter removeItem(final VendingMachine vm, final ItemCounter itemCounter) {
	vm.getWriter().write(MACHINE_IS_NOT_OPENED_MESSAGE.getMessage());
	return null;
    }

    @Override
    public BigDecimal ejectMoney(final VendingMachine vm) {
	vm.getWriter().write(NOT_ENOUGH_MONEY_MESSAGE.getMessage());
	return BigDecimal.ZERO;
    }

    @Override
    public boolean selectItem(final VendingMachine vm, final String name) {
	vm.getWriter().write(NEED_STANDBY_MODE_MESSAGE.getMessage());
	return false;
    }

    @Override
    public BigDecimal getCash(final VendingMachine vm, final BigDecimal cashInMachine, final BigDecimal cashMinimum) {
	vm.getWriter().write(MACHINE_IS_NOT_OPENED_MESSAGE.getMessage());
	return BigDecimal.ZERO;
    }

    @Override
    public boolean makeItem(final VendingMachine vm, final String name) {
	vm.getWriter().write(YOU_NEED_TO_SELECT_MESSAGE.getMessage());
	return false;
    }

    @Override
    public Item takeItem(final VendingMachine vm, final String name) {
	vm.getWriter().write(YOU_NEED_TO_SELECT_MESSAGE.getMessage());
	return null;
    }

    @Override
    public BigDecimal putCoin(final VendingMachine vm, final BigDecimal coin) {
	vm.getWriter().write(NEED_STANDBY_MODE_MESSAGE.getMessage());
	return BigDecimal.ZERO;
    }

    private static void write(final String message, final VendingMachine vm) {
	vm.getWriter().write(message);
    }

    private static String getRemoveMessage(final VendingMachine vm, final String name, final int count,
	    final boolean isSuccessfull, final boolean isAbsoluteRemoving) {
	if (isSuccessfull) {
	    if (isAbsoluteRemoving) {
		return REMOVE_SUCCESSFULL_MESSAGE.getMessage() + name;
	    }
	    return REMOVE_SUCCESSFULL_MESSAGE.getMessage() + count + " " + name;
	} else {
	    return REMOVE_FAILURE_MESSAGE.getMessage() + vm.getInventory().getItemCount(name);
	}
    }
}
