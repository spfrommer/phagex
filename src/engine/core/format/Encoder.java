package engine.core.format;

public interface Encoder<O, V> {
	public boolean isEncodable(O object);
	public V encode(O object);
}
