package util;

import java.util.Random;

public class RandomUtil {
	public static String getRandom(String[] array) {
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
}
