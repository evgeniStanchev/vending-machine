package com.egtinteractive.writer;

public final class ConsoleWriter implements Writer {
    @Override
    public final void write(final String string) {
	System.out.println(string);
    }
}
