
import 'package:flutter/material.dart';
import 'test/stateless/TestStatelessPage.dart';
import 'test/utils/ToolKits.dart';

void main() {
	runApp(MaterialApp(
		title: "Hello world",
		home: Scaffold(
			appBar: AppBar(
				title: Text("Happy New Year!"),
				centerTitle: true,
			),
			body: Row(
				children: <Widget>[
					ToolKits.wrapWeight(1, Text("你好啊")),
					Text("世界真大"),
					Text("啊娃娃"),
				],
			),
		),
	));
//  Row
//  runApp(TestStatelessPage());

}
