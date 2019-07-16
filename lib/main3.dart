import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

var testDown = TestDown();

void main() {
	WidgetsFlutterBinding.ensureInitialized()
		..attachRootWidget(TestContainer())
		..scheduleWarmUpFrame();

//	GestureBinding.instance.pointerRouter.addGlobalRoute((event) {
//		if(event is PointerDownEvent) {
//			testDown.addPointer(event);
//		}
//	});
}

class TestContainer extends StatefulWidget {

	@override
	TestContainerState createState() => TestContainerState();
}

class TestContainerState extends State<TestContainer> {
	var flag = true;
	
	@override
	Widget build(BuildContext context) {
		
		return Directionality (
			textDirection: TextDirection.ltr,
			child: Row(
				children: <Widget>[
					GestureDetector (
						onForcePressStart: (details) {
							print("press start");
						},
//						onTapDown: (details) {
//							print("tap down");
//							setState(() {
//								this.flag = !flag;
//							});
//
//						},
//						onTapCancel: () {
//							print("tap cancel");
//						},
//						onTap: () {
//							print("click");
//						},
//						onDoubleTap: () {
//							print("double click");
//						},
//						onLongPress: () {
//							print("long press");
//						},
//						onLongPressStart: (details) {
//							print("long press start");
//						},
//						onLongPressUp: () {
//							print("long press up");
//						},
						child: Text(
							"helloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworld"
//							flag ? "123123" : "helloworld"
						),
					)
				],
			),
		);
	}
}


class TestDown extends PrimaryPointerGestureRecognizer {
	TestDown(): super(deadline: Duration(milliseconds: 600));

	@override
	void handlePrimaryPointer(PointerEvent event) {

	}

	@override
	void didExceedDeadline() {
		resolve(GestureDisposition.accepted);
    }

    @override
    // TODO: implement debugDescription
    String get debugDescription => "TestDown";

	@override
    void acceptGesture(int pointer) {
        // TODO: implement acceptGesture
        super.acceptGesture(pointer);

        print("TestDown Accept");
    }

    @override
    void rejectGesture(int pointer) {
        // TODO: implement rejectGesture
        super.rejectGesture(pointer);

        print("TestDown Rejected");
    }
}

