package engine.core.format;

public abstract class Format<F, E, D> implements Encoder<E, F>, Decoder<F, D> {
	public abstract boolean isDecodable(F value);
	public abstract boolean isEncodable(E object);
	public abstract F encode(E object);
	public abstract D decode(F value);
}
