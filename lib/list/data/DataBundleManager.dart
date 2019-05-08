
import 'BaseData.dart';
import 'DataBundle.dart';

class DataBundleManager {
	var _dataBundleList = List<DataBundle>();
	var _dataList = List<BaseData>();
	int _count = 0;
	
	appendData(BaseData baseData) {
		var beginIdx = _count;
		
		var lastDataBundle = _dataBundleList.length == 0 ?
			_newDataBundle(beginIdx) : _dataBundleList.last;
		
		if(!lastDataBundle.checkAppendData(baseData)) {
			lastDataBundle = _newDataBundle(beginIdx);
			lastDataBundle.checkAppendData(baseData);
		}
		
		baseData.bundleIdx = _dataBundleList.length - 1;
		_dataList.add(baseData);
		_count ++;
	}
	
	
	appendDataList(List<BaseData> dataList, {bool isAllowAppend(int position, BaseData data)}) {
		var beginIdx = _count;
		
		var lastDataBundle = _dataBundleList.length == 0 ?
			_newDataBundle(beginIdx) : _dataBundleList.last;
		for(var baseData in dataList) {
			if(isAllowAppend != null && !isAllowAppend(beginIdx, baseData))
				continue;
			
			if(!lastDataBundle.checkAppendData(baseData)) {
				lastDataBundle = _newDataBundle(beginIdx);
				lastDataBundle.checkAppendData(baseData);
			}
			baseData.bundleIdx = _dataBundleList.length - 1;
			beginIdx ++;
			this._dataList.add(baseData);
			_count ++;
		}
	}
	
	
	DataBundle _newDataBundle(int beginIdx) {
		final dataBundle = DataBundle(beginIdx: beginIdx);
		_dataBundleList.add(dataBundle);
		return dataBundle;
	}
	
	BaseData findDataAt(int position) {
		if(position >= _count)
			return null;
		
		return _dataList[position];
	}
	
	int getRowCount() {
		return _dataBundleList.length;
	}
	
	int getCount() {
		return _count;
	}
	
	void clear() {
		_dataBundleList.clear();
		_dataList.clear();
		_count = 0;
	}
	
	void removeFrom(int idx, {bool isAllowAppend(int position, BaseData data)}) {
		_dataBundleList.clear();
		if(idx < _dataList.length)
			_dataList.removeRange(idx, _dataList.length);
		
		final _tempDataList = _dataList;
		_dataList = List();
		appendDataList(_tempDataList, isAllowAppend: isAllowAppend);
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