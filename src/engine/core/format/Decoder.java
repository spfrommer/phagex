package engine.core.format;

public interface Decoder<V, O> {
	public boolean isDecodable(V value);
	public O decode(V value);
}
