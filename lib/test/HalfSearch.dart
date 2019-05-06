
import '../list/widget/GridListWidget.dart';

enum Compare {
	EQUAL,
	OVER,
	LESS
}

class HalfSearch {
	static T search<T>(List<T> list, Compare check(T elem)) {
		int count = list.length;
		if(count == 0)
			return null;
		if(count == 1)
			return check(list[0]) == Compare.EQUAL ? list[0] : null;
		
		int hIdx, lIdx;
		//forward
		
		lIdx = 0;
		hIdx = count;
		
		var sCount = 1;
		while(lIdx < hIdx) {
			print("search count: ${sCount ++}");
			var midIdx = (lIdx + hIdx) ~/ 2;
			final subItem = list[midIdx];
			switch(check(subItem)) {
				case Compare.EQUAL:
					return subItem;
				case Compare.LESS:
					lIdx = midIdx;
					break;
				case Compare.OVER:
					hIdx = midIdx;
					break;
			}
		}
		
		//backward
		
		
		return null;
	}
}