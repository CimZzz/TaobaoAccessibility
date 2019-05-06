import 'dart:async';
import 'dart:io';
import 'dart:convert';
import 'dart:isolate';
import 'dart:math';



const kk = const [1, 2, 3];
const printMethod = printHaHa;

//
//void main() async {
//
//	//	kk[1] = 2;
//
//var str =
//'''hellow
//orl
//dhaha''';
//
//print(str is String);
//
//
//
//	print(str.runes.toList());
//
//
//	print(String.fromCharCodes(Runes("\u{1f600}")));
//
////	generateStream(10).take(2).forEach((num) {
////		print("count: $num");
////	});
//
////	await for(var x in generateDoubleStream().take(5)) {
////		print("count: $x");
////	}
////
////	await async2();
////	print("haha");
//
//	const map =  {
//		"abc": "123",
//		"haha": 123,
//	};
//
//	var mapStr = map.toString();
//	print(mapStr);
//	print(jsonEncode(map));
//
//	final Map<String, dynamic> map2 = jsonDecode(jsonEncode(map));
//	print(map2);
//
////	map.forEach(printHaHa);
//
////	str.runes.forEach(printMethod);
//
////	printMethod("Happy");
//
//
//	var adderFunc = makeAdder(12);
//
//	print(adderFunc(1));
//	print(adderFunc(2));
//	print(adderFunc(3));
//
//
//	var cObj = C(12);
//	cObj.num2 = 23;
//
//	print(cObj);
//}

var item = 1;

class CC {
	final bool abc;

  CC({this.abc = false});
}

Future exc() {
	return Future.delayed(Duration(seconds: 1), () {
		throw HttpException("hello world");
	});
}


void asyncException() async {
	try {
		var result = await exc();
	}
	catch(e) {
		print("e: $e");
	}
	
	
	await Future.delayed(Duration(seconds: 2));
	
	
	print("end");
}



void main() async {
	
	asyncException();
//
//	var future =
//
//	future.catchError((e) {
//		print(e);
//		return false;
//	});
//
//
//
//	await Future.delayed(Duration(seconds: 2));
//
//	future.then((number) {
//		print(number);
//	})
//		..catchError((e) {
//			print(e);
//			return false;
//		});
//
//
//	future.then((number) {
//		print(number);
//	})
//		..catchError((e) {
//			print(e);
//			return false;
//		});
	
//	var number = await future;

//	var c = CC();
//
//	print(c.abc);

//	final dataBundleManager = DataBundleManager();
//
//	for(var subData in generateRandomData(20)) {
//		dataBundleManager.appendData(subData);
//	}
//
//
//	print(dataBundleManager);
//
//	print(dataBundleManager.findDataAt(12));
//	print(dataBundleManager.findDataAt(7));
//
}
//
//Iterable<BaseData> generateRandomData(int count) {
//	var random = Random();
//	return Iterable.generate(count, (idx) => random.nextBool() ? SubData("$idx") : SubData2("$idx"));
//}


//void main() async {
//	print("begin");
//	var id = 0;
//	var future = delayMethod(id ++, 2);
//	future.then(printMethod);
//	future.then((_) => delayMethod(id ++, 2))
//		.then((_) => delayMethod(id ++, 2))
//		.then((_) => delayMethod(id ++, 2));
//	future.
	
//	test();
//	test();
//	test();
//	test();
//	test();
//}

test() async {
	var receivePort = ReceivePort();
	
	await Isolate.spawn(load, receivePort.sendPort);
	print("item: $item");
	item = 123;
	print("item: $item");
	
	var sendPort = await receivePort.first;
	var msg = await sendReceive(sendPort, "asdad");
	print("msg: $msg");
}

load(SendPort sendPort) async {
	var port = new ReceivePort();
	
	sendPort.send(port.sendPort);
	
	item ++;
	
	await for (var msg in port) {
		var data = msg[0];
		SendPort replyTo = msg[1];
		replyTo.send(data + " $item");
		if (data == "bar") port.close();
	}
}


Future sendReceive(SendPort port, msg) {
	ReceivePort response = new ReceivePort();
	port.send([msg, response.sendPort]);
	return response.first;
}

Future delayMethod(int id, int seconds) {
	return Future.delayed(Duration(seconds: seconds), () {
		print("Completed $id");
		return id;
	});
}


class A {
	int num1;
	A(int num1) {
		this.num1 = num1;
	}
}

class B {
	int num2;
}

class C extends A with B {
  C(int num) : super(num);
  
  @override
  String toString() {
    // TODO: implement toString
    return "num1: $num1, num2: $num2";
  }
}


makeAdder(num addBy) {
	var mixNum = addBy + 12;
	
	return (num i) => mixNum + i;
}

void printHaHa(element) {
	print("ha ha! $element");
}


Stream<double> generateStream(int count) {
	return Stream.fromIterable(Iterable.generate(count, (position){return position * 1.0;}));
}

Stream<double> generateDoubleStream() async* {
	var id = 0.0;
	
	while(true) {
		yield id ++;
	}
}

var total = 0;
var run = 0;

Future async1({int id: 0, int seconds: 2}) {
	print("doGenerate: $run, id: $id");
	run ++;
	var future = Future.delayed(Duration(seconds: seconds), () {
		print("Completed: $id Success");
	});
	
	return future;
}

Stream<Future> generateFutures({int batch}) async* {
	var count = total;
	while(true) {
		print("doGenerateXXXX");
		count += 1;
		yield async1(id: count, seconds: 2);
	}
}

async2() async {
//	var futures = ;
//	futures.forEach((item) async {
//		await item;
//	});
	
//	var futures = [async1(seconds: 5), async1(seconds: 1)];
//
	await for(var future in generateFutures().take(1)) {
//		await future;
	}
	
	print("excite");
}
