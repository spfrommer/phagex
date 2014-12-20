package engine.core.exceptions;

import java.io.IOException;

public class AssetException extends RuntimeException {
	private static final long serialVersionUID = -5876407936298263747L;

	public AssetException(String exception) {
		super(exception);
	}

	public AssetException(String string, IOException e) {
		super(string, e);
	}
}
