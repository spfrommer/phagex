package commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Locates a resource. Copied from gltools with permission from owner.
 */
public interface ResourceLocator {
	/**
	 * Gets the InputStream from a resource. The String does not need a starting slash.
	 * 
	 * @param resource
	 * @return
	 */
	public InputStream getResource(String resource);

	public class ClasspathResourceLocator implements ResourceLocator {
		@Override
		public InputStream getResource(String resource) {
			InputStream in = ResourceLocator.class.getClassLoader().getResourceAsStream(resource);
			if (in == null)
				throw new RuntimeException("Resource " + resource + " not found");
			return in;
		}
	}

	public class FolderResourceLocator implements ResourceLocator {
		private File m_folder;

		public FolderResourceLocator(File folder) {
			m_folder = folder;
		}

		@Override
		public InputStream getResource(String resource) {
			try {
				return new FileInputStream(new File(m_folder, resource));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
