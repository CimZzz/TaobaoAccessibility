
import 'package:flutter/material.dart';
import 'test/stateless/TestStatelessPage.dart';
import 'test/utils/ToolKits.dart';

void main() {
	runApp(TTPage());
}


class TTPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return TTState();
  }

}


class TTState extends State<TTPage> {
	var itemCount = 100;
	
	@override
	Widget build(BuildContext context) {
		return MaterialApp(
			title: "Hello world",
			home: Scaffold(
				appBar: AppBar(
					title: Text("Happy New Year!"),
					centerTitle: true,
				),
				body: new NotificationListener(
					child: ListView.builder(
						itemCount: itemCount,
						itemExtent: null,
						itemBuilder: itemBuilder
					)
				)
			),
		);
	}
	
	Widget itemBuilder(BuildContext context, int position) {
//		print("build position: $position");
		if(position % 2 == 0)
			return ListTile(
				title: Center(
					child: Text("$position"),
				),
				onTap: () {
					setState(() {
//						if(itemCount == 10)
//							itemCount = 123;
//						else itemCount = 10;
						print("tap at: $position");
					});
				}
			);
		else
			return ListTile(
				title: Container(
					child: Text("哈哈哈: $position"),
				),
				onTap: () {
					setState(() {
						itemCount = 0;
					});
					
//					setState(() {
//
//						itemCount = 100;
////						if(itemCount == 10)
////							itemCount = 123;
////						else itemCount = 10;
//						print("tap at: $position");
//					});
				}
			);
	}
}

