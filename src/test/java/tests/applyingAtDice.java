package tests;

import org.junit.Test;
import pages.diceApply;

import java.io.IOException;

public class applyingAtDice {
@Test
	public static void main(String[] args) throws InterruptedException, IOException {
		diceApply dApp = new diceApply();
		dApp.applyAndUpdate();

	}
}
