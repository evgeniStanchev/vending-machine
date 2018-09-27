package com.egtinteractive.vendingmachinetests;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.testng.annotations.DataProvider;

import com.egtinteractive.item.Item;
import com.egtinteractive.item.ItemCounter;
import com.egtinteractive.vendingmachine.VendingMachine;
import com.egtinteractive.vendingmachine.VendingMachine.Builder;
import com.egtinteractive.writer.ConsoleWriter;
import com.egtinteractive.writer.Writer;

public class DataProviders {

    static final String NEW_PASSWORD = "1234";
    static final String FIRST_PASSWORD = "0000";
    static final String RANDOM_NAME = UUID.randomUUID().toString();
    static final BigDecimal RANDOM_PRICE = BigDecimal.ONE;

    @DataProvider(name = "machine")
    public final Object[][] vendingMachine() {
	return new Object[][] { { defaultProducts() } };
    }

    ItemCounter randomItemCounter(final VendingMachine vm) {
	final int count = ThreadLocalRandom.current().nextInt(1, vm.getCountLimit());
	final Item item = new Item(RANDOM_NAME, RANDOM_PRICE);
	final ItemCounter itemCounter = new ItemCounter(item, count);
	return itemCounter;
    }

    private static VendingMachine defaultProducts() {
	final Writer writer = new ConsoleWriter();
	final Builder builder = new Builder(writer);
	final VendingMachine vm = new VendingMachine(builder);
	final Item bestiniBecon = new Item("Bestini Becon", new BigDecimal("0.80"));
	final Item bestiniCheese = new Item("Bestini Cheese", new BigDecimal("0.90"));
	final Item bakeRolls = new Item("Bake Rolls", new BigDecimal("0.80"));
	final Item chewingGums = new Item("Chewing gums", new BigDecimal("1.00"));
	final Item waffleHyper = new Item("Waffle Hyper", new BigDecimal("0.45"));
	final Item waffleBorovets = new Item("Waffle Borovets", new BigDecimal("0.30"));
	final Item salty = new Item("Salty", new BigDecimal("0.30"));
	final Item ticTac = new Item("Tic tac", new BigDecimal("1.20"));
	final Item chips = new Item("Chips", new BigDecimal("1.80"));
	final Item lottoPizza = new Item("Lotto Pizza", new BigDecimal("0.80"));
	final Item sneakers = new Item("Sneakers", new BigDecimal("1.00"));

	vm.openTheMachine("0000");
	vm.addItem(new ItemCounter(bakeRolls, 3));
	vm.addItem(new ItemCounter(sneakers, 3));
	vm.addItem(new ItemCounter(chewingGums, 2));
	vm.addItem(new ItemCounter(waffleBorovets, 4));
	vm.addItem(new ItemCounter(waffleHyper, 4));
	vm.addItem(new ItemCounter(salty, 3));
	vm.addItem(new ItemCounter(ticTac, 2));
	vm.addItem(new ItemCounter(chips, 6));
	vm.addItem(new ItemCounter(lottoPizza, 5));
	vm.addItem(new ItemCounter(bestiniBecon, 1));
	vm.addItem(new ItemCounter(bestiniCheese, 3));
	vm.closeTheMachine();
	return vm;
    }

}
