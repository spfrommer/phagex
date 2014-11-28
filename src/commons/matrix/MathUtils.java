package commons.matrix;

public class MathUtils {
	private MathUtils() {

	}

	/**
	 * Returns 1 if i is even and -1 otherwise.
	 * 
	 * @param i
	 * @return
	 */
	public static int changeSign(int i) {
		if (i % 2 == 0)
			return 1;
		return -1;
	}

	/**
	 * @param f
	 * @return the sign of f, or 0 if no sign
	 */
	public static int sign(float f) {
		return f > 0 ? 1 : (f < 0 ? -1 : 0);
	}
}
