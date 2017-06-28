# Time series
Scala time series for reading huge files and print into 
the standard ouptut without huge amount of memory.

## Build project

For building project just clone it from github. And than type

```
sbt assembly
```

Sbt will genererate a jar archive, wich can run with arg of your file.
```
scala target/scala-2.11/time-series-assembly-1.0.jar </path/to/file.txt>
```

It prints header and data line by line.
