package engine.core.format;

public interface MetaEncoder<O, V> {
	public boolean isEncodable(O object);
	public V encode(O object);
}
