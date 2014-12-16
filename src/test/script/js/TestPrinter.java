package test.script.js;

import commons.Logger;

public class TestPrinter {
	public void printHelloString() {
		Logger.instance().out("hello");
	}

	public String getHelloString() {
		return "hello";
	}
}
