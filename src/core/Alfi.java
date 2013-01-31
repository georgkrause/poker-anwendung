package core;

import java.util.Random;

public class Alfi extends Player {

	Alfi(int credit) {
		super(credit);
	}
	
	public int decide() throws InterruptedException {
		Thread.sleep(5000);
		Random r = new Random();
		return r.nextInt(3);
	}

}
