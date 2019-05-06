import 'package:flutter/material.dart';
import 'package:second_app/list/data/ResultBundle.dart';

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


class GridListState extends State<GridListWidget> {
	
	/*配置方法*/
	
	/// 允许列表内容失败
	bool get isAllowContentFailure => false;
	
	/// 头部请求 Future
	Future<ResultBundle> get headerFuture async {
		return null;
	}
	
	/// 列表内容 Future
	Future<ResultBundle> get contentFuture async {
		return null;
	}
	
	
	/*内部数据*/
	
	/// 列表数据集
	DataBundleManager _bundleManager;
	
	/// 列表总状态
	ListStatus _listStatus = ListStatus.Loading;
	
	/// 列表内容状态
	ListStatus _contentStatus = ListStatus.Loading;

	
	
	@override
	Widget build(BuildContext context) {
		// TODO: implement build
		return null;
	}
}