package engine.core;

public interface Component {
	public String getName();

	public String[] getIdentifiers();

	public Object getData(String identifier);

	public void setData(String identifier, Object data);
}
