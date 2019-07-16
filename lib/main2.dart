import 'package:flutter/material.dart';
import 'package:quicklibs/quicklibs.dart';


void main() => runApp(TestContainer());

class TestContainer extends StatefulWidget {
	
	@override
	TestContainerState createState() => TestContainerState();
}

class TestContainerState extends State<TestContainer> {
	@override
	Widget build(BuildContext context) {
		return new Directionality(
			textDirection: TextDirection.ltr,
			child: ListView(
				children: intEachList<Widget>(
					total: 10,
					callback: (position) => Text("position at: $position")
				)
			),
		);
	}
}

class ErrorTest extends StatelessWidget {
	
	@override
	Widget build(BuildContext context) {
		throw Exception("hahhaha");
	}
}


class Text1 extends StatefulWidget {
	const Text1({Key key, this.data}): super(key: key);
	
	final String data;
	
    @override
    State<StatefulWidget> createState() => TextState();
}

class TextState extends State<Text1> {
	Color color;
	
	@override
	void didChangeDependencies() {
		super.didChangeDependencies();
		final colorWidget1 = context.inheritFromWidgetOfExactType(InheritedColor1);
		if(colorWidget1 != null) {
			final color1 = colorWidget1 as InheritedColor1;
			color = Colors.yellow;
			return;
		}
		
		
		final colorWidget2 = context.inheritFromWidgetOfExactType(InheritedColor2);
		if(colorWidget2 != null) {
			final color2 = colorWidget2 as InheritedColor2;
			color = Colors.blue;
			return;
		}
		
		final colorWidget3 = context.inheritFromWidgetOfExactType(InheritedColor3);
		if(colorWidget3 != null) {
			final color3 = colorWidget3 as InheritedColor3;
			color = Colors.red;
			return;
		}
		
		
		
		color = Colors.green;
	}
	
	@override
	Widget build(BuildContext context) {
		// TODO: implement build
		return Text(
			this.widget.data,
			textDirection: TextDirection.ltr,
			style: TextStyle (
				color: this.color
			),
		);
	}
}

abstract class InheritedColor1 extends InheritedWidget {
	const InheritedColor1({Key key, Widget child}): super(key: key, child: child);
	
	final Color color = Colors.red;
	
	@override
	bool updateShouldNotify(InheritedWidget oldWidget) => false;
}



class InheritedColor2 extends InheritedColor1 {
	const InheritedColor2({Key key, Widget child}): super(key: key, child: child);
	
	final Color color = Colors.blue;
	
	@override
	bool updateShouldNotify(InheritedWidget oldWidget) => false;
}


class InheritedColor3 extends InheritedColor1 {
	const InheritedColor3({Key key, Widget child}): super(key: key, child: child);
	
	final Color color = Colors.blue;
	
	@override
	bool updateShouldNotify(InheritedWidget oldWidget) => false;
}