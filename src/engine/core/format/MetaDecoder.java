package engine.core.format;

public interface MetaDecoder<V, O> {
	public boolean isDecodable(V value);
	public void decode(V value, O object);
}
