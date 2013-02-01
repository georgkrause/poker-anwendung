package core;

import java.util.Random;

public class Alfi extends Player {

	Alfi(int credit) {
		super(credit);
	}
	
	public int decide() throws InterruptedException  {
		Thread.sleep(5000);
		Random r = new Random();
		int random = r.nextInt(10);
		
		if(random < 6) {
			return 1;
		} else if (random < 8) {
			return 1;
		} else {
			return 1;
		}
	}

}
