import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';

import 'package:flutter/services.dart';


void main() async {
    WidgetsFlutterBinding.ensureInitialized()
        ..attachRootWidget(TestContainer())
        ..scheduleWarmUpFrame();

    SystemChrome.setApplicationSwitcherDescription(ApplicationSwitcherDescription(
        label: "123",
        primaryColor: Colors.red.value
    )).asStream().listen((data) {
        print("success");
    });
}

class TestContainer extends StatefulWidget {

    @override
    TestContainerState createState() => TestContainerState();
}


class TestContainerState extends State<TestContainer> implements TextInputClient {
    var isSuccess = false;

    @override
    void initState() {
        // TODO: implement initState
        super.initState();
    }

    @override
    Widget build(BuildContext context) {
        return Directionality(
            textDirection: TextDirection.ltr,
            child: AndroidView(
                viewType: "test.app.flutter/customView",
                creationParams: "这是Android Native View",
                creationParamsCodec: const StandardMessageCodec(),
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
