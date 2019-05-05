import 'package:flutter/material.dart';


void main() => runApp(TestPage());



class TestPage extends StatelessWidget {
	
	@override
	Widget build(BuildContext context) {
		return MaterialApp(
			title: "哈哈",
			home: Scaffold (
				body: new Image.asset(
					'images/screen_shot.jpg',
					fit: BoxFit.contain,
				),
//				body: Center(
//					child: Image.network(
//						"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=718036190,460677062&fm=58&bpow=500&bpoh=333",
//						fit: BoxFit.fill,
//					),
//					child:
//				),
			)
		);
	}
}