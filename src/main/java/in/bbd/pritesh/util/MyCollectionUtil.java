package in.bbd.pritesh.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MyCollectionUtil {

	// JDK1.8 -- static methods in interface
	public static Map<Integer,String> convertListToMap(List<Object[]> list) {
		// JDK 8 - Streams (convert List-> Map)
		Map<Integer,String> map =
				list.stream()
				.collect(
						Collectors.toMap(
								ob->Integer.valueOf(ob[0].toString()), 
								ob->ob[1].toString())
						);
		/*Map<Integer,String> map = new LinkedHashMap<>();
		for(Object[] ob:list) {
			map.put(
					Integer.valueOf(ob[0].toString()), 
					ob[1].toString()
				);
		}*/
		return map;
	}
}
