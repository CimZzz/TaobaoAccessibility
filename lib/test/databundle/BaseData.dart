
class BaseData {
	int bundleIdx = 0;
	int get useSpanCount => 1;
}


class SubData extends BaseData {
	final String name;

    SubData(this.name);
	
	@override
	// TODO: implement useSpanCount
	int get useSpanCount => 2;
	
	@override
	String toString() {
		// TODO: implement toString
		return "SubData($name): 0.5";
	}
}

class SubData2 extends BaseData {
	final String name;
	
	SubData2(this.name);
	
	@override
	// TODO: implement useSpanCount
	int get useSpanCount => 4;
	
	@override
	String toString() {
		// TODO: implement toString
		return "SubData2($name): 0.25";
	}
}