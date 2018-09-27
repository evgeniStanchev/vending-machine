package com.egtinteractive.coin;

import java.math.BigDecimal;

public enum Coin {
    FIVE(new BigDecimal("0.05")), TEN(new BigDecimal("0.10")), TWENTY(new BigDecimal("0.20")), FIFTY(
	    new BigDecimal("0.50")), LEV(BigDecimal.ONE);
    private BigDecimal value;

    public BigDecimal getValue() {
	return this.value;
    }

    private Coin(BigDecimal value) {
	this.value = value;
    }
}
