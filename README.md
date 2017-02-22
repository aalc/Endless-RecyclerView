# Endless-RecyclerView
>Endless support for RecyclerView
 
## Gradle Dependency

  1. Add the JitPack repository to your build file

	```gradle
	allprojects {
				repositories {
					...
					maven { url "https://jitpack.io" }
				}
	}
	```

  2. Add the dependency

 ```gradle
dependencies {
    compile 'com.github.aalc:Endless-RecyclerView:1.0.4'
 }
```


## Usage
 
```java
RecyclerView recyclerView = ...;
View loadingView = ...;
SwipeRefreshLayout pullToRefreshLayout = ...;

endless = new Endless.Builder(recyclerView, loadingView)
                .pullToRefreshLayout(pullToRefreshLayout)
                .build();

endless.setAdapter(
                textAdapter = new TextAdapter(),
                new Endless.LoadMoreListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }

                    @Override
                    public void onLoadMore(int page) {
                        loadData(page);
                    }
                });

// call this when refresh data is complete
endless.refreshComplete();

// call this when load more is complete 
endless.loadMoreComplete();

```


## Acknowledgements
- [https://gist.github.com/mipreamble/b6d4b3d65b0b4775a22e](https://gist.github.com/mipreamble/b6d4b3d65b0b4775a22e).

## License
The MIT License (MIT) 

Copyright © 2016 ybq

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.



