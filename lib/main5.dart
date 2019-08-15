import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
    runApp(TestView());
}


class TestView extends StatefulWidget {
    @override
    State<StatefulWidget> createState() => TestState();
}


class TestState extends State<TestView> {
    @override
    Widget build(BuildContext context) {
        // TODO: implement build
        return Directionality(
            textDirection: TextDirection.ltr,
            child: Column(
                children: <Widget>[
                    GestureDetector(
                        onTap: () {
                            HapticFeedback.vibrate().then((_) {
                                print("vibrate 完成");
                            });
                        },
                        child: Text(
                                "vibrate"
                        ),
                    ),
                    GestureDetector(
                        onTap: () {
                            HapticFeedback.lightImpact().then((_) {
                                print("lightImpact 完成");
                            });
                        },
                        child: Text(
                                "lightImpact"
                        ),
                    ),
                    GestureDetector(
                        onTap: () {
                            HapticFeedback.mediumImpact().then((_) {
                                print("mediumImpact 完成");
                            });
                        },
                        child: Text(
                                "mediumImpact"
                        ),
                    ),
                    GestureDetector(
                        onTap: () {
                            HapticFeedback.heavyImpact().then((_) {
                                print("heavyImpact 完成");
                            });
                        },
                        child: Text(
                                "heavyImpact"
                        ),
                    ),
                    GestureDetector(
                        onTap: () {
                            HapticFeedback.selectionClick().then((_) {
                                print("selectionClick 完成");
                            });
                        },
                        child: Text(
                                "selectionClick"
                        ),
                    ),
                ],
            ),
        );
    }
}


