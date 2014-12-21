package engine.transform;

/**
 * Transforms an object of type C into an object of type D.
 * 
 * @param <C>
 * @param <D>
 */
public interface Transformer<C, D> {
	public D transform(C c);
}
