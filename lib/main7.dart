import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';

import 'package:flutter/services.dart';



void main() {
    WidgetsFlutterBinding.ensureInitialized()
        ..attachRootWidget(TestContainer())
        ..scheduleWarmUpFrame();

    SystemChrome.setEnabledSystemUIOverlays([]);
}

class TestContainer extends StatefulWidget {

    @override
    TestContainerState createState() => TestContainerState();
}


class TestContainerState extends State<TestContainer> implements TextInputClient {
    TextInputConnection connection;
    String useText;

    @override
    Widget build(BuildContext context) {
        return Directionality(
            textDirection: TextDirection.ltr,
            child: Row(
                children: <Widget>[
                    Expanded(
                        child: GestureDetector(
//							onPanStart: (details) {
//								print("pan start");
//							},
//							onPanEnd: (details) {
//								print("pan end");
//							},
//                            onHorizontalDragStart: (details) {
//							    print("hor drag start");
//                            },
//							onVerticalDragDown: (details) {
//								print("ver drag down");
//							},
//							onScaleUpdate: (details) {
//								print("detail: $details");
//							},
//							onScaleEnd: (details) {
//								print("end: $details");
//							},
//							onScaleStart: (details) {
//								print("start: $details");
//							},
//						    onForcePressStart: (details) {
//						    	print("press start");
//						    },
//						    onTapDown: (details) {
//						    	print("tap down");
//						    	setState(() {
//						    		this.flag = !flag;
//						    	});
//
//						    },
//						    onTapCancel: () {
//						    	print("tap cancel");
//						    },
                            onTap: () {
                                if(connection == null) {
                                    connection = TextInput.attach(
                                            this, TextInputConfiguration(
                                            inputAction: TextInputAction.search))
                                    ..setEditingState(TextEditingValue());

                                    connection.show();
                                }
//								SystemSound.play(SystemSoundType.click);
//								var result = await SystemChannels.platform.invokeMethod(
//										"Clipboard.getData",
//										Clipboard.kTextPlain
//								);
//								useText = (result as Map<String, dynamic>)["text"];
//
//								setState(() {
//								});
//								print("click current time: ${SchedulerBinding.instance.currentSystemFrameTimeStamp}");
                            },
                            onDoubleTap: () {
//								print("double click");
                                if(connection != null) {
                                    connection.close();
                                    connection = null;
                                    setState(() {
                                    });
                                }
                            },
                            onLongPress: () {},
                            //						onLongPressStart: (details) {
                            //							print("long press start");
                            //						},
                            //						onLongPressUp: () {
                            //							print("long press up");
                            //						},
                            child: Text(
                                    useText == null
                                            ? "1233232131123\n12332321311231233232131123123323213112312332321311231233232131123"
                                            : useText

//															flag ? "123123" : "helloworld"
                            ),
                        ),
                    )
                ],
            ),
        );
    }

    @override
    void performAction(TextInputAction action) {
        // TODO: implement performAction
        print("action: $action");
    }

    @override
    void updateEditingValue(TextEditingValue value) {
        // TODO: implement updateEditingValue
        print("value: ${value.text}");
    }

    @override
    void updateFloatingCursor(RawFloatingCursorPoint point) {
        // TODO: implement updateFloatingCursor
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

