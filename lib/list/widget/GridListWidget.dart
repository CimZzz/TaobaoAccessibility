import 'package:flutter/material.dart';

import '../environment/Environment.dart';

import '../data/BaseData.dart';
import '../data/DataBundle.dart';
import '../data/DataBundleManager.dart';

/// 2019-5-8
/// 请求全部已完成
/// 下一步构建列表项框架

class GridListWidget extends StatefulWidget {
	final GridListState state;
	
	const GridListWidget(this.state, {Key key}) : super(key: key);
	
	@override
	State<StatefulWidget> createState() {
		// TODO: implement createState
		return this.state;
	}
}


class GridListState extends State<GridListWidget> with _InnerListProcessor {
	/* 内部属性 */
	
	/// 列表状态
	/// 其中包含:
	/// 1. [DataBundleManager] 列表数据
	/// 2. [ListStatus] 列表状态，
	/// 3. [ListContentStatus] 列表项状态
	_InnerListState _innerListState = _InnerListState.init();
	
	/// 配置参数 Map
	/// 数据不会随着刷新而刷新
	/// 用于设置常量参数和跨越刷新周期的参数
	Map<String, dynamic> _configMap;
	
	/* 配置方法 */
	/// 允许列表内容失败
	/// 作用是在刷新全部时，列表内容加载失败时会导致这次刷新失败
	bool get isAllowContentFailure => false;
	
	/// 如果列表目前的显示状态为 [ListStatus.List] 时
	/// 是否允许忽略 Request All 请求失败
	bool get isIgnoreFailure => true;
	
	/* Widget 回调方法 */
	
	//// 列表控件 Callback
	/// 根据列表状态 [ListStatus] 执行各自回调函数
	
	/// 构建列表加载中控件 Callback
	/// 当列表加载中显示的内容
	/// *状态为 [ListStatus.Loading]
	Widget buildLoadingWidget(BuildContext context) {
		return null;
	}
	
	/// 构建列表控件 Callback
	/// [ListView] 是整个列表控件
	/// 如果需要定制列表控件，要记得将 [ListView] 包含其中
	/// *状态为 [ListStatus.List]
	Widget buildListWidget(BuildContext context, ListView listView) {
		return listView;
	}
	
	/// 构建列表加载完成数据为空控件 Callback
	/// 当列表加载完成数据为空显示的内容
	/// 如果返回 null 则会调用 [buildListWidget] 显示默认列表内容
	/// *状态为 [ListStatus.Empty]
	Widget buildEmptyWidget(BuildContext context) {
		return null;
	}
	
	/// 构建列表加载失败控件 Callback
	/// 当列表加载失败显示的内容
	/// 1.头部加载失败
	/// 2.当 [isAllowContentFailure] = false 时，列表项加载失败
	/// *状态为 [ListStatus.Error]
	Widget buildErrorWidget(BuildContext context) {
		return null;
	}
	
	//// 列表底部内容控件 Callback
	/// 根据列表内容状态 [ListContentStatus] 执行各自回调函数
	
	/// 构建列表内容底部加载中控件 Callback
	/// 当列表滑动到底部时，列表内容正在加载中时底部显示的控件
	/// *状态为 [ListContentStatus.Loading]
	Widget buildContentLoadingWidget(BuildContext context) {
		return null;
	}
	
	/// 构建列表内容底部加载完成控件 Callback
	/// 当列表滑动到底部时，列表内容加载完成时底部显示的控件
	/// *状态为 [ListContentStatus.Over]
	Widget buildContentOverWidget(BuildContext context) {
		return null;
	}
	
	
	/// 构建列表内容底部加载完成数据为空控件 Callback
	/// 当列表滑动到底部时，列表内容加载完成且数据为空时底部显示的控件
	/// *状态为 [ListContentStatus.Empty]
	Widget buildContentEmptyWidget(BuildContext context) {
		return null;
	}
	
	/// 构建列表内容底部加载失败控件 Callback
	/// 当列表滑动到底部时，列表内容加载失败时底部显示的控件
	/// *状态为 [ListContentStatus.Error]
	Widget buildContentErrorWidget(BuildContext context) {
		return null;
	}
	
	/* 请求回调方法 */
	
	/// 头部请求 Future Callback
	/// 只在刷新全部时调用
	/// 如果返回 null 表示不存在头部请求
	/// 如果执行完成后 [HeaderResultListBean._headerDataList] 为 null 时，表示
	/// 此次请求失败
	Future<HeaderResultListBean> requestHeader(ResultBundle resultBundle) async {
		return null;
	}
	
	/// 列表内容 Future Callback
	/// 刷新全部时和加载下一页时调用
	/// 如果返回 null 表示不存在列表内容请求
	/// 如果执行完成后 [ContentResultListBean._contentDataList] 为 null 时，表示
	/// 此次请求失败
	Future<ContentResultListBean> requestContent(ResultBundle resultBundle) async {
		return null;
	}
	
	/// 重置全部数据 Callback
	void onResetForAll() {
	
	}
	
	/// 重置列表内容数据 Callback
	void onResetForContent() {
	
	}
	
	/// 构建 Result Bundle For 请求全部数据
	void buildResultBundleForAll(ResultBundle resultBundle) {
	
	}
	
	/// 构建 Result Bundle For 请求列表内容数据
	void buildResultBundleForContent(ResultBundle resultBundle) {
	
	}
	
	
	
	/// 刷新全部成功 Callback
	void onRequestAllSuccess() {
	
	}
	
	/// 刷新全部失败 Callback
	void onRequestAllError() {
	
	}
	
	/* 取值方法 */
	
	/// 从 [_InnerListState._tempMap] 中取临时数据
	/// 只有指定 key 下存在正确类型的值，才会返回，其余情况下返回 null
	T findTempValue<T>(String key) {
		dynamic value;
		var map = this._innerListState._tempMap;
		if(map != null)
			value = map[key];
		
		if(value != null) {
			if (value is T)
				return value;
		}
		return null;
	}
	
	/// 向 [_InnerListState._tempMap] 中存储临时数据
	void saveTempValue(String key, dynamic value) {
		if(this._innerListState._tempMap == null)
			this._innerListState._tempMap = Map();
		
		this._innerListState._tempMap[key] = value;
	}
	
	/// 从 [_configMap] 中取数据
	/// 只有指定 key 下存在正确类型的值，才会返回，其余情况下返回 null
	T findConfigValue<T>(String key) {
		dynamic value;
		var map = this._configMap;
		if(map != null)
			value = map[key];
		
		if(value != null) {
			if (value is T)
				return value;
		}
		return null;
	}
	
	/// 向 [_configMap] 中存储数据
	void saveConfigValue(String key, dynamic value) {
		if(this._configMap == null)
			this._configMap = Map();
		
		this._configMap[key] = value;
	}
	
	
	/* 构建当前列表页面 */
	@override
	Widget build(BuildContext context) {
		switch(_innerListState._listStatus) {
			case ListStatus.Loading:
				return buildLoadingWidget(context);
			case ListStatus.Empty:
				return buildEmptyWidget(context) ?? buildListWidget(context, _buildListView(context));
			case ListStatus.Error:
				return buildErrorWidget(context);
			case ListStatus.List:
			default:
				return buildListWidget(context, _buildListView(context));
		}
	}
	
	/// 构建列表项控件
	ListView _buildListView(BuildContext context) {
		return ListView.builder(itemBuilder: null);
	}
	
	/* 覆盖 _InnerListProcessor 中的方法 */
	
	
	/// 头部请求 Future Callback
	/// 只在刷新全部时调用
	/// #不可继承#
	/// 调用 [requestHeader] 生成头部请求 Future，并提供缺省的异常捕获
	@override
	Future<HeaderResultListBean> _requestHeader(ResultBundle resultBundle) {
		return this.requestHeader(resultBundle).catchError((_){ });
	}
	
	/// 列表内容 Future Callback
	/// 刷新全部时和加载下一页时调用
	/// #不可继承#
	/// 调用 [requestHeader] 生成头部请求 Future，并提供缺省的异常捕获
	@override
	Future<ContentResultListBean> _requestContent(ResultBundle resultBundle) {
		return this.requestContent(resultBundle).catchError((_){ });
	}
	
	/// 重置全部数据 Callback
	/// #不可继承#
	/// 初始化 [_innerListState] 并刷新
	/// 调用 [onResetForAll] 允许外部做更多的重置操作
	@override
	void _onResetForAll() {
		setState(() {
			this._innerListState = _InnerListState.init();
			this.onResetForAll();
		});
	}
	
	/// 重置列表内容数据 Callback
	/// #不可继承#
	/// 允许执行条件:
	/// 一.
	/// [_innerListState] 中 [_InnerListState._listStatus] :
	/// 1. [ListStatus.List] 显示列表项时
	/// 2. [ListStatus.Empty] 显示数据为空时
	/// 注: 请求成功的情况下
	///
	/// 二.
	/// [_innerListState] 中 [_InnerListState._listDataType] :
	/// 1. [ListDataType.ONLY_CONTENT] 只包含列表内容数据
	/// 1. [ListDataType.ALL] 包含头部和列表内容数据
	/// 注: 存在列表内容数据
	///
	/// 清空 [_innerListState] 中全部列表内容数据并刷新
	/// 调用 [onResetForContent] 允许外部做更多的重置操作
	@override
	void _onResetForContent() {
		setState(() {
			if(this._innerListState._listStatus != ListStatus.List &&
				this._innerListState._listStatus != ListStatus.Empty)
				return;
			
			if(this._innerListState._listDataType == ListDataType.ONLY_CONTENT ||
				this._innerListState._listDataType == ListDataType.ALL) {
				if(this._innerListState._listStatus == ListStatus.Empty)
					this._innerListState._listStatus = ListStatus.Loading;
				this._innerListState._clearContentData();
				this.onResetForContent();
			}
		});
	}
	
	/// 构建 Result Bundle For 请求全部数据
	/// #不可继承#
	/// 将 [_innerListState] 中 [_InnerListState._tempMap] 赋值给 [ResultBundle._finalTempMap]
	/// 将 [isAllowContentFailure] 赋值给 [ResultBundle.__isAllowContentFailure]
	/// 调用 [buildResultBundleForAll] 允许外部做更多的构建操作
	@override
	ResultBundle _buildResultBundleForAll(ResultBundle bundle) {
		bundle._finalTempMap = this._innerListState._tempMap;
		bundle._isAllowContentFailure = this.isAllowContentFailure;
		this.buildResultBundleForAll(bundle);
		return bundle;
	}
	
	/// 构建 Result Bundle For 请求列表内容数据
	/// #不可继承#
	/// 将 [_innerListState] 中 [_InnerListState._tempMap] 赋值给 [ResultBundle._finalTempMap]
	/// 将 [isAllowContentFailure] 赋值给 [ResultBundle.__isAllowContentFailure]
	/// 调用 [buildResultBundleForContent] 允许外部做更多的构建操作
	@override
	ResultBundle _buildResultBundleForContent(ResultBundle bundle) {
		bundle._finalTempMap = this._innerListState._tempMap;
		bundle._isAllowContentFailure = this.isAllowContentFailure;
		this.buildResultBundleForContent(bundle);
		return bundle;
	}
	
	
	/// 刷新全部成功回调
	/// 只在刷新全部时调用
	/// #不可继承#
	/// 调用 [onRequestAllSuccess] 允许外部做更多的操作
	@override
	void _onRequestAllSuccess(int syncCode, ResultBundle bundle) {
		setState(() {
			// 检查是否为最新的刷新请求
			if(_checkAllSyncCode(syncCode)) {
				
				_InnerListState newState = _InnerListState.init();
				newState._listStatus = bundle._afterListStatus;
				newState._listDataType = bundle._afterListDataType;
				newState._contentStatus = bundle._afterContentStatus;
				newState._headerEndIdx = bundle._afterHeaderEndIdx;
				newState._nextPageCursor = bundle._afterNextPageCursor;
				newState._tempMap = bundle._afterTempMap;
				
				if(newState._listStatus == ListStatus.List)
					newState._appendDataList(bundle._afterDataList);
				
				this._innerListState = newState;
				onRequestAllSuccess();
			}
		});
	}
	
	/// 刷新全部失败回调
	/// 只在刷新全部时调用
	/// #不可继承#
	/// 调用 [onRequestAllError] 允许外部做更多的操作
	@override
	void _onRequestAllError(int syncCode, ResultBundle bundle) {
		setState(() {
			// 检查是否为最新的刷新请求
			if(_checkAllSyncCode(syncCode)) {
				if(this._innerListState._listStatus == ListStatus.List &&
					this.isIgnoreFailure)
					return;
				
				_InnerListState newState = _InnerListState.init();
				newState._listStatus = bundle._afterListStatus;
				this._innerListState = newState;
				onRequestAllError();
			}
		});
	}
	
	
	
}


/// 内部列表状态
/// 主要包含列表数据以及当前列表状态
class _InnerListState {
	
	/// 初始化构造函数
	/// 列表最初的状态
	_InnerListState.init();
	
	/// 列表数据集
	DataBundleManager _bundleManager;
	
	/// 分页游标
	dynamic _nextPageCursor;
	
	/// 头部最后一项下标
	int _headerEndIdx;
	
	/// 临时数据 Map
	/// 每次刷新全部时刷新
	/// 用于存储再一次刷新周期内参数数据，如筛选选择的参数等等
	Map<String, dynamic> _tempMap;
	
	/// 去重 set
	/// 数据请求成功之后会进行数据去重，将重复的数据筛选出去
	Set<dynamic> _deDuplicateSet;
	
	/// 列表总状态
	/// 表示当前列表的状态 [ListStatus]
	/// 1.[ListStatus.Loading] 表示当前列表正在加载中，会调用 [GridListState.buildLoadingWidget] 生成当前列表显示内容
	/// 2.[ListStatus.List] 表示当前列表加载完成，会调用 [GridListState.buildListWidget] 生成当前列表显示内容
	/// 2.[ListStatus.Empty] 表示当前列表加载完成且数据为空，会调用 [GridListState.buildEmptyWidget] 生成当前列表显示内容
	/// 3.[ListStatus.Error] 表示当前列表正在加载中，会调用 [GridListState.buildErrorWidget] 生成当前列表显示内容
	ListStatus _listStatus = ListStatus.Loading;
	
	
	/* 当列表总状态为 ListStatus.List 时，以下属性才会生效 */
	
	/// 列表数据类型
	/// 表示当前列表数据种类的类型
	/// 1.[ListDataType.ONLY_HEADER] 表示当前列表数据只包含头部
	///     此状态下无法显示筛选框和最底部状态列表项
	/// 2.[ListDataType.ONLY_CONTENT] 表示当前列表数据只包含列表内容
	/// 3.[ListDataType.ALL] 表示当前列表数据即包含头部，又包含列表内容
	ListDataType _listDataType;
	
	/// 列表内容状态
	/// 表示当前列表内容状态 [ListContentStatus]
	/// 体现在最底部列表项
	/// 1.[ListContentStatus.Loading] 表示当前列表内容正在加载中，滑动到列表最底部时会调用 [GridListState.buildContentLoadingWidget] 生成底部列表控件
	/// 2.[ListContentStatus.Over] 表示当前列表内容加载完成，滑动到列表最底部时会调用 [GridListState.buildContentOverWidget] 生成底部列表控件
	/// 3.[ListContentStatus.Empty] 表示当前列表内容加载完成且数据为空，滑动到列表最底部时会调用 [GridListState.buildContentEmptyWidget] 生成底部列表控件
	/// 4.[ListContentStatus.Error] 表示当前列表内容加载失败，滑动到列表最底部时会调用 [GridListState.buildContentErrorWidget] 生成底部列表控件
	ListContentStatus _contentStatus = ListContentStatus.Loading;
	
	
	/* 内部方法 */
	
	/// 去重方法
	/// 如果不存在 key ，添加 key 并返回 true
	/// 如果存在 key 返回 false
	bool _duplicate(dynamic key) {
		if(key == null)
			return true;
		
		if(_deDuplicateSet == null)
			_deDuplicateSet = Set();
		else if(_deDuplicateSet.contains(key))
			return false;
		
		_deDuplicateSet.add(key);
		return true;
	}
	
	/// 执行 [_appendDataList] 数据 Callback
	/// 调用 [_duplicate] 方法去重，并处理头部数据被去重，[_headerEndIdx] 变化的逻辑
	bool _appendCallback(int beginIdx, BaseData data) {
		if(!_duplicate(data.getUniqueKey)) {
			// 如果当前数据的 idx 小于等于头部数据最后一项的 idx
			// 表示头部数据被去重过滤掉，则 _headerEndIdx 位置向前进一位
			// 如果头部数据全部被去重掉
			if(_headerEndIdx != null) {
				if(beginIdx <= _headerEndIdx) {
					_headerEndIdx --;
					
					if(_headerEndIdx < 0)
						_headerEndIdx = null;
				}
				
			}
			return false;
		}
		
		return true;
	}
	
	/// 添加数据列表到 [_bundleManager] 中
	/// 调用 [_duplicate] 做去重操作
	/// 如果 set 中存在 key，则表示数据重复被筛选掉
	/// 否则认为数据唯一，并将唯一 key 存在 set 中
	/// 如果返回 null，则默认数据唯一
	void _appendDataList(List<BaseData> dataList) {
		if(_bundleManager == null)
			_bundleManager = DataBundleManager();
		
		_bundleManager.appendDataList(dataList, isAllowAppend: _appendCallback);
	}
	
	/// 清空列表内容数据
	/// 1. 清空去重 set [_deDuplicateSet]
	/// 2. 根据 [_headerEndIdx] 判断是否存在头部数据，如果不存在则全部清空；
	///    如果存在则重新构建头部数据
	void _clearContentData() {
		if(_deDuplicateSet != null) {
			_deDuplicateSet.clear();
			_deDuplicateSet = null;
		}
		
		_nextPageCursor = null;
		
		if(this._bundleManager == null)
			return;
		
		if(this._headerEndIdx == null)
			this._bundleManager.clear();
		else this._bundleManager.removeFrom(this._headerEndIdx, isAllowAppend: _appendCallback);
		
	}
}


/// 列表请求处理单元
/// 用于处理列表数据请求逻辑
abstract class _InnerListProcessor {
	
	/* 内部属性 */
	/// 是否正在执行 Request All 请求
	bool _isDoingRequestAll = false;
	
	/// 当前 Request All 的同步码
	/// 如果有多个 Request All 请求时，最终执行与同步码相同的请求
	int _requestAllCode = 0;
	
	/// 是否正在执行 Request Content 请求
	bool _isDoingRequestContent = false;
	
	/// 当前 Request Content 的同步码
	/// 如果有多个 Request Content 请求时，最终执行与同步码相同的请求
	int _requestContentCode = 0;
	
	/* 重置方法 */
	
	/// 内部重置 Request All 状态码方法
	/// 通过调用 [_resetForAllStatus] 方法来重置 Request All 的状态码
	/// 通过调用 [_resetForContentStatus] 方法来重置 Request Content 的状态码
	/// 之后执行 [_onResetForAll] 回调方法
	void _resetForAll() {
		_resetForAllStatus();
		_resetForContentStatus();
		_onResetForAll();
	}
	
	/// 内部重置 Request All 状态码具体方法
	void _resetForAllStatus() {
		_isDoingRequestAll = false;
		_requestAllCode ++;
		if(_requestAllCode >= 0xFFFFFFF)
			_requestAllCode = 0;
	}

	/// 内部重置 Request Content 状态码方法
	/// 通过调用 [_resetForContentStatus] 方法来重置 Request Content 的状态码
	/// 之后执行 [_onResetForContent] 回调方法
	void _resetForContent() {
		_resetForContentStatus();
		_onResetForContent();
	}
	
	/// 内部重置 Request Content 状态码具体方法
	void _resetForContentStatus() {
		_isDoingRequestContent = false;
		_requestContentCode ++;
		if(_requestContentCode >= 0xFFFFFFF)
			_requestContentCode = 0;
	}
	
	/* 请求方法 */
	
	/// 执行刷新全部请求逻辑
	/// 当此次请求时，上一次请求还未完成，则会停止此次请求，继续上一次请求
	/// 如果 isForce == true，则会立即中断上一次请求，执行当前请求
	void _requestAll({bool isForce = false}) {
		if(isForce)
			_resetForAll();
		
		if(_isDoingRequestAll)
			return;
		
		_isDoingRequestAll = true;
		_requestAllCode ++;
		_doRequestAll(_requestAllCode, _buildResultBundleForAll(ResultBundle()));
	}
	
	/// 执行刷新全部请求
	void _doRequestAll(int syncCode, ResultBundle bundle) async {
		try {
			final headerFuture = _requestHeader(bundle);
			final contentFuture = _requestContent(null);
			
			bool isHeaderEmpty = true;
			bool isHasContent = false;
			HeaderResultListBean headerResultListBean;
			ContentResultListBean contentResultListBean;
			
			bundle._afterListStatus = ListStatus.List;
			
			// 头部请求阻塞
			headerResultListBean = await headerFuture;
			// 如果存在头部请求，处理头部结果
			if(headerResultListBean != null) {
				// 头部数据列表不为 null 时，表示头部数据获取成功
				var headerDataList = headerResultListBean._headerDataList;
				if(headerDataList != null) {
					// headerDataList isEmpty == true
					if(headerDataList.isEmpty)
						// 标记头部数据是空数据
						isHeaderEmpty = true;
					else isHeaderEmpty = false;
				}
				else {
					// 不存在头部列表，表示此次请求失败
					_requestAllError(syncCode, bundle);
					return;
				}
			}
			
			try {
				// 列表内容请求阻塞
				contentResultListBean = await contentFuture;
				if(contentResultListBean != null)
					isHasContent = true;
			}
			catch(e) {
				// 如果列表内容请求失败时
				// 如果 ResultBundle._isAllowContentFailure 为 false，直接判定该次刷新动作失败
				// 如果 ResultBundle._isAllowContentFailure 为 true，该次刷新动作不算做失败
				// 但是列表内容状态会变为 ListContentStatus.Error
				if(!bundle._isAllowContentFailure) {
					_requestAllError(syncCode, bundle);
					return;
				}
				
				isHasContent = true;
			}
			
			// 如果存在列表内容请求，处理列表内容结果
			if(isHasContent)
				_processContentState(bundle, contentResultListBean);
			
			
			// 然后判断头部数据是否同样为空
			if(isHeaderEmpty && bundle._afterContentStatus == ListContentStatus.Empty) {
				// 标记此次请求的数据全部为空
				bundle._afterListStatus = ListStatus.Empty;
				
				if(!isHeaderEmpty)
					bundle._afterListDataType = ListDataType.ONLY_HEADER;
				
				if(isHasContent) {
					// 如果存在过头部数据，标记列表数据类型为 ListDataType.ALL
					if (bundle._afterListDataType != null)
						bundle._afterListDataType = ListDataType.ALL;
					else bundle._afterListDataType = ListDataType.ONLY_CONTENT;
				}
			}
			
			
			// 基本状态处理完成
			// 下一步处理数据列表
			
			// 处理数据类型
			if(bundle._afterListStatus == ListStatus.List) {
				// 若存在头部数据，优先处理头部数据
				if(!isHeaderEmpty) {
					var headerDataList = headerResultListBean._headerDataList;
					bundle._afterListDataType = ListDataType.ONLY_HEADER;
					bundle._afterHeaderEndIdx = headerDataList.length - 1;
					bundle._afterDataList.addAll(headerDataList);
				}
				
				// 若存在列表内容数据，处理之
				if(isHasContent) {
					var contentDataList = contentResultListBean._contentDataList;
					
					// 如果存在过头部数据，标记列表数据类型为 ListDataType.ALL
					if(bundle._afterListDataType != null)
						bundle._afterListDataType = ListDataType.ALL;
					else bundle._afterListDataType = ListDataType.ONLY_CONTENT;
					
					bundle._afterNextPageCursor = contentResultListBean._nextPageCursor;
					bundle._afterDataList.addAll(contentDataList);
				}
			}
			
			// 处理完成，发送成功状态
			_requestAllSuccess(syncCode, bundle);
		}
		catch(e) {
			// 处理过程发生错误，请求失败
			_requestAllError(syncCode, bundle);
		}
	}
	
	/// 刷新全部请求失败
	/// 会重新构建一个内部状态
	void _requestAllError(int syncCode, ResultBundle bundle) {
		bundle._afterListStatus = ListStatus.Error;
		_onRequestAllError(syncCode, bundle);
	}
	
	/// 刷新全部请求成功
	/// 会重新构建一个内部状态
	void _requestAllSuccess(int syncCode, ResultBundle bundle) {
		_onRequestAllSuccess(syncCode, bundle);
	}
	
	/// 处理列表内容数据状态
	void _processContentState(ResultBundle bundle, ContentResultListBean contentResultListBean) {
		if(contentResultListBean != null) {
			// 列表内容数据列表不为 null 时，表示列表内容数据获取成功
			var contentDataList = contentResultListBean?._contentDataList;
			if(contentDataList != null) {
				// contentDataList isEmpty == true
				if(contentDataList.isEmpty) {
					// 标记列表内容状态为 ListContentStatus.Empty
					bundle._afterContentStatus = ListContentStatus.Empty;
				}
				else {
					// 如果 contentDataList 不为空，判断列表内容是否加载完成
					if(contentResultListBean._isContentDataOver)
						bundle._afterContentStatus = ListContentStatus.Over;
					// 若列表内容未加载完成，则继续加载列表内容
					else bundle._afterContentStatus = ListContentStatus.Loading;
				}
			}
			else {
				// 不存在列表内容数据列表，标记列表内容状态为 ListContentStatus.Error
				bundle._afterContentStatus = ListContentStatus.Error;
			}
		}
		else {
			// 不存在列表内容数据列表，标记列表内容状态为 ListContentStatus.Error
			bundle._afterContentStatus = ListContentStatus.Error;
		}
	}
	
	/* 快速方法 */
	
	/// 检查 Request All 同步码是否一致
	bool _checkAllSyncCode(int syncCode) {
		return _requestAllCode == syncCode;
	}
	
	/// 检查 Request Content 同步码是否一致
	bool _checkContentSyncCode(int syncCode) {
		return _requestContentCode == syncCode;
	}
	
	
	
	/* 抽象方法 */
	
	/// 头部请求 Future Callback
	/// 只在刷新全部时调用
	Future<HeaderResultListBean> _requestHeader(ResultBundle resultBundle);
	
	/// 列表内容 Future Callback
	/// 刷新全部时和加载下一页时调用
	Future<ContentResultListBean> _requestContent(ResultBundle resultBundle);
	
	/// 重置全部数据 Callback
	void _onResetForAll();
	
	/// 重置列表内容数据 Callback
	void _onResetForContent();
	
	/// 构建 Result Bundle For 请求全部数据
	ResultBundle _buildResultBundleForAll(ResultBundle bundle);
	
	/// 构建 Result Bundle For 请求列表内容数据
	ResultBundle _buildResultBundleForContent(ResultBundle bundle);
	
	/// 刷新全部成功 Callback
	void _onRequestAllSuccess(int syncCode, ResultBundle bundle);
	
	/// 刷新全部失败 Callback
	void _onRequestAllError(int syncCode, ResultBundle bundle);
}


/// 结果数据集
class ResultBundle {
	/// 是否允许列表内容请求失败
	bool _isAllowContentFailure;
	
	/// 请求过后列表状态
	ListStatus _afterListStatus;
	
	/// 请求过后列表数据类型
	ListDataType _afterListDataType;
	
	/// 请求过后列表内容状态
	ListContentStatus _afterContentStatus;
	
	/// 请求过后的全部数据列表
	List<BaseData> _afterDataList = List<BaseData>();
	
	/// 请求过后头部最后一项下标
	int _afterHeaderEndIdx;
	
	/// 请求过后的分页游标
	dynamic _afterNextPageCursor;
	
	/// 不可更改的临时存放数据 Map
	/// 来自 [_InnerListState._tempMap]
	Map<String, dynamic> _finalTempMap;
	
	/// 临时存放数据 Map
	/// 当一次请求完成此数据 Map 不为空时，会将此 Map 的数据增量覆盖到已经存在的 [_InnerListState._tempMap] 中
	Map<String, dynamic> _afterTempMap;
    
    /* 值方法 */
    
    /// 从当前结果数据集中取临时存储数据
	/// 先从自身的临时数据 Map 中取，然后再从已经存在的 [_finalTempMap] 中取
	/// 只有指定 key 下存在正确类型的值，才会返回，其余情况下返回 null
    T findTempValue<T>(String key) {
	    dynamic value;
    	var map = this._afterTempMap;
    	if(map != null)
		    value = map[key];
    	
    	if(value != null) {
    		if(value is T)
    			return value;
    		
    		return null;
	    }
	    
	    map = this._finalTempMap;
	    if(map != null)
		    value = map[key];
	    
	    if(value != null) {
		    if(value is T)
			    return value;
	    }
	
	    return null;
    }
    
    /// 向当前结果数据集中存储临时数据
    void saveTempValue(String key, dynamic value) {
    	if(this._afterTempMap == null)
    		this._afterTempMap = Map();
	
	    this._afterTempMap[key] = value;
    }
}

/// 头部数据结果列表
class HeaderResultListBean {
	/// 头部数据列表
	List<BaseData> _headerDataList;
}

/// 列表内容数据结果列表
class ContentResultListBean {
	/// 列表内容数据列表
	List<BaseData> _contentDataList;
	
	/// 标记列表内容是否加载完毕
	bool _isContentDataOver;
	
	/// 下一页的分页游标
	dynamic _nextPageCursor;
}

