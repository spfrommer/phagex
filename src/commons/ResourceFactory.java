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
	public static String readString(Resource resource) {
		if (resource == null)
			throw new ResourceException("Cannot read a null resource!");
		Scanner scanner = new Scanner(resource.open(), "UTF-8");
		String string = scanner.useDelimiter("\\A").next();
		scanner.close();
		return string;
	}
}
