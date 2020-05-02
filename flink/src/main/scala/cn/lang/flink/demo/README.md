[TOC]

# Flink Demo Instruments
> 对于源码中的具体注释，请参考对应的Github地址查看


## Based Classes Or Interfaces
### `AssignerWithPeriodicWatermarks`
1. 指定`event time`时间戳，并生成标识在流中event time处理进度的`low watermarks`低水位；
2. 在周期间隔时间内生成watermarks；
3. `getCurrentWatermark`表明只有在新水位高于之前有效的水位时才起作用；
### `BoundedOutOfOrdernessTimestampExtractor`


