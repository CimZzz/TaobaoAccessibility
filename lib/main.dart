
import 'package:flutter/material.dart';
import 'test/stateless/TestStatelessPage.dart';
import 'test/utils/ToolKits.dart';

void main() {
	runApp(TTPage());
}

class TTPage2 extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
	  print("new state TTState2");
    return TTState2();
  }

}

class TTState2 extends State<TTPage2> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Text(
	    "123",
	    textDirection: TextDirection.ltr,
    );
  }

}

class TTPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
//  	print("new state TTState");
    return TTState();
  }

}

class TTState extends State<TTPage> {
	var listViewKey = UniqueKey();
	var itemCount = 100;
	var controller = ScrollController();
	
	@override
	Widget build(BuildContext context) {
		return MaterialApp(
			home: Scaffold(
				body: NotificationListener(
					onNotification: (notification) {
						print("$notification");
						return true;
					},
					child: ListView.builder(
						key: listViewKey,
						controller: controller,
						primary: false,
						itemCount: itemCount,
						itemBuilder: itemBuilder
					)
				),
			),
		);
	}
	
	Widget itemBuilder(BuildContext context, int position) {
//		print("position: $position, ${context.findRenderObject().getTransformTo(null).getTranslation().y}");
//		print("build position: $position");
		if(position % 3 == 0)
			return ListTile(
				title: Text(
					"$position",
					textDirection: TextDirection.ltr,
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
			return Container(
				height: 100,
				color: Colors.blue,
				child: Text("abvc"),
			);
	}
	
}

