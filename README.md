A Multi Item List Adapter
===========

__AMultiItemListAdapter__ is an adapter for your ListView that  allows you to create ListView with multiple items on a row based on your current adapter. Furthermore, you can also have the same funcionality using sections.

[![Phone example](http://danielebottillo.com/github/multilistitem_phone.jpg)](http://danielebottillo.com/github/multilistitem_phone.jpg)

[![Tablet example](http://danielebottillo.com/github/multilistitem_tablet.jpg)](http://danielebottillo.com/github/multilistitem_tablet.jpg)


## Usage

```java
int cellSpacing = (int)getResources().getDimension(R.dimen.cell_spacing);
int numberOfColumns = getResources().getInteger(R.integer.number_of_columns);
MultiItemListAdapter multiItemListAdapter = new MultiItemListAdapter(this, currentAdapter, numberOfColumns, cellSpacing);
list.setAdapter(multiItemListAdapter);
```

The constructor of MultiItemListAdapter takes four parameters: the current Context, the current adapter, the number of columns and the cell spacing. The currentAdapter is the original adapter of the list. If you specify the number of columns and the cell spacing inside and xml you can have your listview change based on the size of screen:
res/values/integers.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="items_per_row">1</integer>
</resources>
```

res/values-land/integers.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer name="items_per_row">2</integer>
</resources>
```


If you want to use the section version of AMultiItemListAdapter the behaviour is slighty different:

```java
int cellSpacing = (int)getResources().getDimension(R.dimen.cell_spacing);
int numberOfColumns = getResources().getInteger(R.integer.number_of_columns);
MultiItemListSectionAdapter multiItemListSectionAdapter = new MultiItemListSectionAdapter(this, numberOfColumns, cellSpacing);
MultiItemListHeader firstHeader = new MultiItemListHeader(0, firstViewHeader);
multiItemListSectionAdapter.addSection(firstHeader, firstAdapter);
multiItemListSectionAdapter.addSection(secondHeader, secondAdapter);        list.setAdapter(multiItemListSectionAdapter);
```
In this case you have to create a MultiItemListHeader for each section that takes two parameters: an index and the view that will use inside the list. Then for each section you have to add an header and the adapter for the elements.

## Supported Version

Android 4.0+


## LICENSE - "MIT License"

Copyright (c) 2013 Matteo Collina (http://matteocollina.com) and LevelGraph Contributors 

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
