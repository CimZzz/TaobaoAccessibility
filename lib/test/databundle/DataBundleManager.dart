
import 'BaseData.dart';
import 'DataBundle.dart';
import 'HalfSearch.dart';

class DataBundleManager {
	var _dataBundleList = List<DataBundle>();
	var dataList = List<BaseData>();
	int _count = 0;
	
	appendData(BaseData baseData) {
		var beginIdx = _count;
		_count ++;
		
		var lastDataBundle = _dataBundleList.length == 0 ?
			_newDataBundle(beginIdx) : _dataBundleList.last;
		
		if(!lastDataBundle.checkAppendData(baseData)) {
			lastDataBundle = _newDataBundle(beginIdx);
			lastDataBundle.checkAppendData(baseData);
		}
		
		baseData.bundleIdx = _count - 1;
		dataList.add(baseData);
	}
	
	
	appendDataList(List<BaseData> dataList) {
		var beginIdx = _count;
		_count += dataList.length;
		
		var lastDataBundle = _dataBundleList.length == 0 ?
			_newDataBundle(beginIdx) : _dataBundleList.last;
		for(var baseData in dataList) {
			if(!lastDataBundle.checkAppendData(baseData)) {
				lastDataBundle = _newDataBundle(beginIdx);
				lastDataBundle.checkAppendData(baseData);
			}
			baseData.bundleIdx = _count - 1;
			dataList.add(baseData);
			beginIdx ++;
		}
	}
	
	
	DataBundle _newDataBundle(int beginIdx) {
		final dataBundle = DataBundle(beginIdx: beginIdx);
		_dataBundleList.add(dataBundle);
		return dataBundle;
	}
	
	DataBundle dataBundle() {
	
	}
	
	BaseData findDataAt(int position) {
		if(position >= _count)
			return null;
		
		return dataList[position];
	}
	
	int getRowCount() {
		return _dataBundleList.length;
	}
	
	int getCount() {
		return _count;
	}
	
	@override
	String toString() {
		var str = "count: $_count, rowCount: ${getRowCount()}, list: \n";
		for(var dataBundle in _dataBundleList) {
			str += dataBundle.toString() + "\n";
		}
		
		return str;
	}
}