import 'package:flutter/material.dart';

import '../environment/Environment.dart';

import '../data/BaseData.dart';
import '../data/DataBundle.dart';
import '../data/DataBundleManager.dart';


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
	
	
	
	/* 覆盖 _InnerListProcessor 中的方法 */
	
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
	/// 清空 [_innerListState] 中全部列表内容数据并刷新
	/// 调用 [onResetForContent] 允许外部做更多的重置操作
	@override
	void _onResetForContent() {
		setState(() {
			this._innerListState = _InnerListState.init();
			this.onResetForContent();
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
	
	
	
}


/// 内部列表状态
/// 主要包含列表数据以及当前列表状态
class _InnerListState {
	
	/// 初始化构造函数
	/// 列表最初的状态
	_InnerListState.init();
	
	/// 列表数据集
	DataBundleManager _bundleManager;
	
	/// 列表总状态
	/// 表示当前列表的状态 [ListStatus]
	/// 1.[ListStatus.Loading] 表示当前列表正在加载中，会调用 [GridListState.buildLoadingWidget] 生成当前列表显示内容
	/// 2.[ListStatus.List] 表示当前列表加载完成，会调用 [GridListState.buildListWidget] 生成当前列表显示内容
	/// 2.[ListStatus.Empty] 表示当前列表加载完成且数据为空，会调用 [GridListState.buildEmptyWidget] 生成当前列表显示内容
	/// 3.[ListStatus.Error] 表示当前列表正在加载中，会调用 [GridListState.buildErrorWidget] 生成当前列表显示内容
	ListStatus _listStatus = ListStatus.Loading;
	
	/// 列表内容状态
	/// 表示当前列表内容状态 [ListContentStatus]
	/// 体现在最底部列表项
	/// 1.[ListContentStatus.Loading] 表示当前列表内容正在加载中，滑动到列表最底部时会调用 [GridListState.buildContentLoadingWidget] 生成底部列表控件
	/// 2.[ListContentStatus.Over] 表示当前列表内容加载完成，滑动到列表最底部时会调用 [GridListState.buildContentOverWidget] 生成底部列表控件
	/// 3.[ListContentStatus.Empty] 表示当前列表内容加载完成且数据为空，滑动到列表最底部时会调用 [GridListState.buildContentEmptyWidget] 生成底部列表控件
	/// 4.[ListContentStatus.Error] 表示当前列表内容加载失败，滑动到列表最底部时会调用 [GridListState.buildContentErrorWidget] 生成底部列表控件
	ListContentStatus _contentStatus = ListContentStatus.Loading;
	
	/// 临时数据 Map
	/// 每次刷新全部时刷新
	/// 用于存储再一次刷新周期内参数数据，如筛选选择的参数等等
	Map<String, dynamic> _tempMap;
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
	/// 通过调用 [_resetForContentStatus] 方法来重置 Request Content 的状态码
	/// 之后执行 [_onResetForAll] 回调方法
	void _resetForAll() {
		_isDoingRequestAll = false;
		_requestAllCode ++;
		if(_requestAllCode >= 0xFFFFFFF)
			_requestAllCode = 0;
		_resetForContentStatus();
		_onResetForAll();
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
		_doRequestAll(_buildResultBundleForAll(ResultBundle._init(_requestAllCode ++)));
	}
	
	/// 执行刷新全部请求
	void _doRequestAll(ResultBundle bundle) async {
		try {
			final headerFuture = _requestHeader(bundle);
			final contentFuture = _requestContent(null);
			
			bool isHeaderEmpty = false;
			bool isHasContent = false;
			HeaderResultListBean headerResultListBean;
			ContentResultListBean contentResultListBean;
			
//			bundle._afterListStatus = ListStatus.Empty;
			
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
					_requestAllError(bundle);
				}
			}
			
			try {
				// 列表内容请求阻塞
				contentResultListBean = await contentFuture;
			}
			catch(e) {
				// 如果列表内容请求失败时
				// 如果 ResultBundle._isAllowContentFailure 为 false，直接判定该次刷新动作失败
				// 如果 ResultBundle._isAllowContentFailure 为 true，该次刷新动作不算做失败
				// 但是列表内容状态会变为 ListContentStatus.Error
				if(!bundle._isAllowContentFailure) {
					_requestAllError(bundle);
					return;
				}
				
				isHasContent = true;
			}
			
			// 如果存在列表内容请求，处理列表内容结果
			if(contentResultListBean != null || isHasContent) {
				// 列表内容数据列表不为 null 时，表示列表内容数据获取成功
				var contentDataList = contentResultListBean?._contentDataList;
				if(contentDataList != null) {
					// contentDataList isEmpty == true
					if(contentDataList.isEmpty) {
						// 首先标记列表内容状态为 ListContentStatus.Empty
						bundle._afterListContentStatus = ListContentStatus.Empty;
						// 然后判断头部数据是否同样为空
						if(isHeaderEmpty) {
							// 标记此次请求的数据全部为空
							bundle._afterListStatus = ListStatus.Empty;
						}
					}
					else {
						// 如果 contentDataList 不为空，判断列表内容是否加载完成
						if(contentResultListBean._isContentDataOver)
							bundle._afterListContentStatus = ListContentStatus.Over;
						// 若列表内容未加载完成，则继续加载列表内容
						else bundle._afterListContentStatus = ListContentStatus.Loading;
					}
				}
				else {
					// 不存在列表内容数据列表，标记列表内容状态为 ListContentStatus.Error
					bundle._afterListContentStatus = ListContentStatus.Error;
				}
			}
			
			
			/// 基本状态处理完成
			/// 下一步处理数据列表
			
		}
		catch(e) {
			_requestAllError(bundle);
		}
	}
	
	void _requestAllError(ResultBundle bundle) {
	
	}
	
	
	/* 抽象方法 */
	/// 重置全部数据 Callback
	void _onResetForAll();
	
	/// 重置列表内容数据 Callback
	void _onResetForContent();
	
	/// 构建 Result Bundle For 请求全部数据
	ResultBundle _buildResultBundleForAll(ResultBundle bundle);
	
	/// 构建 Result Bundle For 请求列表内容数据
	ResultBundle _buildResultBundleForContent(ResultBundle bundle);
	
	
	/// 头部请求 Future Callback
	/// 只在刷新全部时调用
	Future<HeaderResultListBean> _requestHeader(ResultBundle resultBundle);
	
	/// 列表内容 Future Callback
	/// 刷新全部时和加载下一页时调用
	Future<ContentResultListBean> _requestContent(ResultBundle resultBundle);
}




/// 结果数据集
class ResultBundle {
	/// 请求同步码
	final int _bundleCodeId;
	
	/// 是否允许列表内容请求失败
	bool _isAllowContentFailure;
	
	/// 请求过后列表状态
	ListStatus _afterListStatus;
	
	/// 请求过后列表内容状态
	ListContentStatus _afterListContentStatus;
	
	/// 不可更改的临时存放数据 Map
	/// 来自 [_InnerListState._tempMap]
	Map<String, dynamic> _finalTempMap;
	
	/// 临时存放数据 Map
	/// 当一次请求完成此数据 Map 不为空时，会将此 Map 的数据增量覆盖到已经存在的 [_InnerListState._tempMap] 中
	Map<String, dynamic> _tempMap;
	
	/// 构造函数 初始化
    ResultBundle._init(this._bundleCodeId);
    
    /* 值方法 */
    
    /// 从当前结果数据集中取临时存储数据
	/// 先从自身的临时数据 Map 中取，然后再从已经存在的 [_finalTempMap] 中取
	/// 只有指定 key 下存在正确类型的值，才会返回，其余情况下返回 null
    T findTempValue<T>(String key) {
	    dynamic value;
    	var map = this._tempMap;
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
    	if(this._tempMap == null)
    		this._tempMap = Map();
	
	    this._tempMap[key] = value;
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
	
}

