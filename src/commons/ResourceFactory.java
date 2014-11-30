package commons;

import java.util.Scanner;

/**
 * Contains methods for getting resources.
 */
public class ResourceFactory {
	private ResourceFactory() {

	}

	/**
	 * @param locator
	 * @param resource
	 * @return the read String
	 */
	public static String readString(ResourceLocator locator, String resource) {
		Scanner scanner = new Scanner(locator.getResource(resource), "UTF-8");
		String string = scanner.useDelimiter("\\A").next();
		scanner.close();
		return string;
	}
}
