import 'package:flutter/material.dart';
import 'package:second_app/test/stateful/TestStatefulPage.dart';


class TestStatelessPage extends StatelessWidget {
	@override
	Widget build(BuildContext context) {
		return MaterialApp(
			title: "TestPage",
			home: new Scaffold(
				appBar: new AppBar(
					title: new Text('Welcome to Flutter'),
					centerTitle: true,
				),
				body: new Center(
					child: new TestStatefulPage(),
//					child: new Image.asset(
//						'images/screen_shot.png',
//						fit: BoxFit.fill,
//					)
				),
			),
		);
	}
}