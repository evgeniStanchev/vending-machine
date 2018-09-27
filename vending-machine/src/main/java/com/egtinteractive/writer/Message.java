package com.egtinteractive.writer;

public enum Message {

    INDEX_OUT_OF_BOUNDS_MESSAGE("Your count must be at least 1"), //
    NULL_POINTER_MESSAGE("Your item/itemCounter cannot be null!"), //
    PUT_ITEMS_FAILURE("Sorry,// you can't put so many items! In this moment you can put only "), //
    REMOVE_FAILURE_MESSAGE("You can't remove so many items right now! You can remove:"), //
    REMOVE_SUCCESSFULL_MESSAGE("OPERATION SUCCESSFULL! You removed:"), //

    NO_MORE_COINS_MESSAGE("OPERATION FAILED! You can't put more coins!"), //
    NEED_STANDBY_MODE_MESSAGE("OPERATION FAILED! The machine have to be in stand by mode!"), //
    MACHINE_IS_NOT_OPENED_MESSAGE("OPERATION FAILED! The machine have to be opened!"), //
    MACHINE_IS_CLOSED_ALREADY_MESSAGE("OPERATION FAILED! The machine is closed already!"), //
    ITEM_DOESNT_EXISTS_MESSAGE("OPERATION FAILED! The requested item doesn't exists!"), //
    YOU_NEED_TO_SELECT_MESSAGE("OPERATION FAILED! You need to select an item first!"), //
    NOT_ENOUGH_MONEY_MESSAGE("OPERATION FAILED! You don't have money for getting!"), //
    INCORRECT_PASSWORD_CHANGING_MESSAGE("OPERATION FAILED! The password is incorrect!"), //
    OLD_PASSWORD_IS_INCORRECT_MESSAGE("OPERATION FAILED! Old password is incorrect!"), //
    NEED_MORE_COINS_MESSAGE("OPERATION FAILED! You need more coins to buy this item!"), //

    MACHINE_OPENED_SUCCESSFULLY_MESSAGE("OPERATION SUCCESSFUL! The machine is opened!"), //
    ADD_ITEM_SUCCESSFULLY_MESSAGE("You added the following item/s successfully!"), //
    PUT_COIN_SUCCESSFULLY_MESSAGE("You putted a coin successfully!"), //
    SELECTING_AN_ITEM_SUCCESSFULLY_MESSAGE("OPERATION SUCCESSFUL! You will receive the item soon as possibly!"), //
    CLOSING_THE_MACHINE_SUCCESSFULLY_MESSAGE("OPERATION SUCCESSFUL! The machine is closed now!"), //
    YOU_CAN_TAKE_YOUR_MONEY_MESSAGE("OPERATION SUCCESSFUL! You can take your money!"), //
    CHANGING_PASSWORD_SUCCESSFULLY_MESSAGE("OPERATION SUCCESSFUL! The password is changed!"), //
    TAKE_AN_ITEM_SUCCESSFULLY_MESSAGE("OPERATION SUCCESSFUL! Enjoy your product !"), //
    MAKING_AN_ITEM_MESSAGE("The product is preparing ..."), //

    SORRY_WE_NEED_A_REPAIR_MESSAGE("Sorry,// the machine is broken! We need to repair it first!");

    private final String message;

    public String getMessage() {
	return this.message;
    }

    Message(String message) {
	this.message = message;
    }

}
