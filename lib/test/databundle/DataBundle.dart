
import 'BaseData.dart';
import 'HalfSearch.dart';

class DataBundle {
	final int beginIdx;
	double _remainSpace = 1;
	int _count = 0;
	int _endIdx = 0;
	
	DataBundle({this.beginIdx}) {
		this._endIdx = this.beginIdx - 1;
	}
	
	bool checkAppendData(BaseData data) {
		if(_remainSpace == 0)
			return false;
		
		var useSpace = 1 / data.useSpanCount;
		if(_remainSpace - useSpace < 0) {
			return false;
		}
		
		_remainSpace -= useSpace;
		_count ++;
		this._endIdx ++;
		return true;
	}
	
	
	Compare comparePosition(int position) {
		if(position < beginIdx)
			return Compare.OVER;
		else if(position > _endIdx)
			return Compare.LESS;
		else return Compare.EQUAL;
	}
	
	int getCount() {
		return _count;
	}
	
	@override
	String toString() {
		var str = "beginIdx: $beginIdx, remainSpace: $_remainSpace, childCount: ${getCount()}";
		
		return str;
	}
}