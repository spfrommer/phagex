package commons;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtils {
	public static String join(String del, Object... objs) {
		return join(del, Arrays.asList(objs));
	}
	public static String join(String del, Collection<?> col) {
		StringBuilder builder = new StringBuilder();
		Iterator<?> it = col.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			builder.append(o.toString());
			if (it.hasNext()) builder.append(del);
		}
		return builder.toString();
	}
}
