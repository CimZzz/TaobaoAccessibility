import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';
import 'package:flutter/src/rendering/sliver.dart';
import 'package:flutter/src/rendering/sliver_grid.dart';

class TestStatefulPage extends StatefulWidget {
	@override
	State createState() => TestStatefulState();
}

class TestStatefulState extends State<TestStatefulPage> {
	final _suggestions = <WordPair>[];
	final _suggestionsSelected = <WordPair>[];
	final _biggerFont = TextStyle(
		fontSize: 18
	);
	
	
	@override
	Widget build(BuildContext context) {
		return new Scaffold (
			appBar: new AppBar(
				title: new Text('Startup Name Generator'),
				actions: <Widget>[
					new IconButton(icon: new Icon(Icons.list), onPressed: _pushSaved)
				],
			),
			body: _buildSuggestions(),
		);
	}
	
	
	String getStr() => "HAHA:" + WordPair.random().asPascalCase;
	
	void _pushSaved() {
		Navigator.of(context).push(
			new MaterialPageRoute(
				builder: (context) {
					final tiles = _suggestionsSelected.map(
								(pair) {
							return new ListTile(
								title: new Text(
									pair.asPascalCase,
									style: _biggerFont,
								),
							);
						},
					);
					final divided = ListTile
							.divideTiles(
						context: context,
						tiles: tiles,
					).toList();
					
					
					return new Scaffold(
						appBar: new AppBar(
							title: new Text('Saved Suggestions'),
						),
						body: new ListView(children: divided),
					);
				},
			),
		);
	}
	
	
	Widget _buildSuggestions() {
		return RefreshIndicator(
			onRefresh: () => Future.delayed(Duration(seconds: 200)),
			child: GridView.builder(
				gridDelegate: Delegate(),
				padding: EdgeInsets.all(20.0),
				itemBuilder: (BuildContext context, int index) {
					
					return Text('entry $index');
				},
			),
		);
		
//		return new ListView.builder(
//			itemCount: 40,
//			itemBuilder: (context, i) {
//				if(i.isOdd)
//					return Divider(
//						color: Colors.green,
//					);
//
//				final idx = i ~/ 2;
//
//				if(idx >= _suggestions.length) {
//					_suggestions.addAll(generateWordPairs().take(10));
//				}
//
//				print("build idx:$idx, ${_suggestions[idx].asPascalCase}");
//
//				return _buildRow(idx, _suggestions[idx]);
//			},
//		);
	}
	
	
	
	Widget _buildRow(int idx, WordPair wordPair) {
		final isSelected = _suggestionsSelected.contains(wordPair);
		
		return ListTile (
			title: Text(
				wordPair.asPascalCase,
				style: _biggerFont,
			),
			trailing: Text(
				isSelected ? "选中" : "未选中",
				style: _biggerFont,
			),
			onTap: () {
				setState(() {
					final isSelected = _suggestionsSelected.contains(wordPair);
					if(isSelected)
						_suggestionsSelected.remove(wordPair);
					else _suggestionsSelected.add(wordPair);
				});
			},
		);
	}
	
}


class Delegate extends SliverGridDelegate {
	final SliverGridDelegateWithFixedCrossAxisCount delegate;
	
	Delegate({int rowCount: 5}): delegate = SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: rowCount);
	
	
	@override
	SliverGridLayout getLayout(SliverConstraints constraints) {
		final count = (5 % 2 == 1) ? 5 : 3;
		
		final double usableCrossAxisExtent = constraints.crossAxisExtent - 0 * (count - 1);
		final double childCrossAxisExtent = usableCrossAxisExtent / count;
		final double childMainAxisExtent = childCrossAxisExtent / 1.0;
		
		print("offset: ${constraints.scrollOffset}, usableCrossAxisExtent: $usableCrossAxisExtent , childCrossAxisExtent: $childCrossAxisExtent, childMainAxisExtent: $childMainAxisExtent");
		return SliverGridRegularTileLayout(
			crossAxisCount: count,
			mainAxisStride: childMainAxisExtent + 0,
			crossAxisStride: childCrossAxisExtent + 0,
			childMainAxisExtent: childMainAxisExtent,
			childCrossAxisExtent: childCrossAxisExtent,
			reverseCrossAxis: axisDirectionIsReversed(constraints.crossAxisDirection),
		);
	}
	
	@override
	bool shouldRelayout(SliverGridDelegate oldDelegate) {
		return delegate.shouldRelayout(oldDelegate);
	}
}