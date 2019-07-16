import 'dart:collection';
import 'dart:developer' as dev;
import 'dart:io';
import 'dart:isolate';

import 'package:quicklibs/quicklibs.dart';

abstract class Interface {
	void printA();
}

class Base {
	Base({this.a});
	final int a;
}

void func () {

}

class Singleton {
	static get instance => _singleton;
	static final Singleton _singleton = Singleton();
}


main() {
	double num = 1.5;
	print(num.clamp(0.0, 1.0));
}