package engine.core.asset;

/**
 * An Asset - contains the identifier String.
 */
public class Asset {
	private String m_identifier;

	/**
	 * Sets the identifier String.
	 * 
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		m_identifier = identifier;
	}

	/**
	 * @return the identifier String
	 */
	public String getIdentifier() {
		return m_identifier;
	}
}
