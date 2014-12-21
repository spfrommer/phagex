package engine.transform.asset;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import commons.Resource;
import commons.ResourceException;

import engine.transform.Transformer;

/**
 * Reads a String from a Resources.
 */
public class StringReader implements Transformer<Resource, String> {
	@Override
	public String transform(Resource resource) {
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
