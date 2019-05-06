
import 'BaseData.dart';

class DataBundle {
	final int beginIdx;
	int _remainSpace = 100;
	int _count = 0;
	int _endIdx = 0;
	
	DataBundle({this.beginIdx}) {
		this._endIdx = this.beginIdx - 1;
	}
	
	bool checkAppendData(BaseData data) {
		if(_remainSpace == 0)
			return false;
		
		var useSpace = data.useSpaceCount;
		if(_remainSpace - useSpace < 0) {
			return false;
		}
		
		_remainSpace -= useSpace;
		_count ++;
		this._endIdx ++;
		return true;
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