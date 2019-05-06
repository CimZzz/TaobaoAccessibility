import 'package:flutter/material.dart';

class ToolKits {
	static Expanded wrapWeight(int weight, Widget child)
		=> Expanded(
			flex: weight,
			child: child,
		);
}
