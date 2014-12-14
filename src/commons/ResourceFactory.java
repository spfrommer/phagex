package commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
		try {
			StringBuilder text = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.open()));
			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line).append('\n');
			}
			reader.close();

			return text.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
