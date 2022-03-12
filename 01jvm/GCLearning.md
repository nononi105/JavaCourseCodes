##SerialGC

配置命令：<u>-XX:+UseSerialGC</u>

串行GC对年轻代使用mark-copy（标记-复制）算法，对老年代使用mark-sweep-compact(标记-清除-整理)算法。

特点：单线程GC，GC时会stop-the-world(STW)，停止应用线程，只适合几百MB堆内存的JVM，单核CPU时比较有用。年轻代默认分配内存-1/3堆内存左右，老年代默认分配内存-2/3堆内存左右。

通过作业1可以看出当堆内存变大时，串行GC收集的频率下降了，但是每一次收集所用的时间有所增加，会增加STW的时间，系统延迟增加，所以串行GC不适合占用内存很大的系统。

<u>-XX:+UseParNewGC</u>改进版本的SerialGC（并行回收年轻代），可以配合CMS使用。

---
##Parallel GC

配置命令：
<u>-XX:+UseParallelGC 
-XX:+UseParallelOldGC
-XX:+UseParallelGC -XX:+UseParallelOldGC</u>

并行GC在年轻代使用标记-复制(mark-copy)算法,在老年代使用标记-清除-整理(mark-sweep-compact)算法。

特点：多线程GC（并行GC），GC同样会STW，适用于多核服务器，主要目标是增加吞吐量。
+ 在GC期间，所有CPU内核（应该是JVM拥有的内核吧？）都在并行清理垃圾，所以总暂停时间更短；
+ 在两次GC周期的间隔期，没有GC线程在运行，不会消耗任何系统资源

通过作业1可以看出并行GC进行一次垃圾收集的时间要明显少于串行GC，在堆内存较大时能比串行GC条件下生成更多的对象，有更高的吞吐量。

---
##CMS GC
###Mostly Concurrent Mark and Sweep Garbage Collector

配置命令：<u> -XX:+UseConcMarkSweepGC</u>

CMS GC对年轻代采用（ParNew）并行STW方式的mark-copy（标记-复制）算法，对老年代主要使用mark-sweep(标记-清除)算法。

目标：避免在老年代垃圾收集时出现长时间的卡顿
特点：
+ 不对老年代进行整理，而是使用空闲列表(free-lists)来管理内存空间的回收
+ 在mark-and-sweep（标记-清除）阶段的大部分工作和应用线程一起并发执行。
+ 默认情况下，CMS使用的并发线程数等于CPU核心数的1/4。
+ 进行老年代的并发回收时，可能会伴随着多次年轻代的minorGC。

处理过程：
1. Initial Mark(初始标记)。伴随STW暂停，初始标记的目标是标记所有的根对象，包括根对象直接引用的对象，以及被年轻代中所有存活对象所引用的对象（老年代单独回收）
2. Concurrent Mark(并发标记)。CMS GC遍历老年代，标记所有的存活对象。
3. Concurrent Preclean(并发预清理)。如果在并发标记的过程中引用关系发生了变化，JVM会通过"Card(卡片)"的方式将发生了改变的区域标记成"脏"区，这就是所谓的卡片标记(Card Marking)
4. Final Remark(最终标记)。伴随STW，完成老年代中所有存活对象的标记。
5. Concurrent Sweep(并发清除)
6. Concurrent Reset(并发重置) 删除不再使用的对象，并回收他们占用的内存空间

存在问题：老年代内存碎片问题（因为不压缩），在某些情况下GC会造成不可预测的暂停时间，特别是堆内存较大的情况下。

最大young区大小：
ParallelGC：1024M/3 = 341.3M
CMS: 64M\*GC线程数4\*13/10 = 332.8M

通过作业1的观察，CMS GC在Final Remark之前可能会做几次年轻代的回收，以提高Final Remark的效率。同时如果GC无法处理产生过快的垃圾，会发生concurrent mode failure，此时所有应用线程会被暂停，CMS GC发生退化。

---

##G1 GC
###Garbage-First

配置命令：<u> -XX:+UseG1GC -XX:MaxGCPauseMillis=50</u>

目标：将STW停顿的时间和分布，变成可预期且可配置的。（启发性配置？）

特点：
+ G1 GC的堆内存不再分成年轻代和老年代，而是划分成多个（通常是2048个）可以存放对象的小块堆区域（smaller heap regions)。
+ 增量收集垃圾。每次GC暂停都会收集所有年轻代的内存块，但一般只包含部分老年代的内存块。

重要配置参数：
+ -XX：G1NewSizePercent:初始年轻代占整个 Java Heap 的大小，默认值为 5%
+ -XX：G1MaxNewSizePercent：最大年轻代占整个 Java Heap 的大小，默认值为 60%；
+ -XX：G1HeapRegionSize：设置每个 Region 的大小，单位 MB，需要为 1、2、4、8、16、32 中的某个值，默认是堆内存的1/2000。如果这个值设置比较大，那么大对象就可以进入 Region 了；
+ -XX：ConcGCThreads：与 Java 应用一起执行的 GC 线程数量，默认是 Java 线程的 1/4，减少这个参数的数值可能会提升并行回收的效率，提高系统内部吞吐量。如果这个数值过低，参与回收垃圾的线程不足，也会导致并行回收机制耗时加长；
+ -XX：+InitiatingHeapOccupancyPercent（简称 IHOP）：G1 内部并行回收循环启动的阈值，默认为 Java Heap的 45%。这个可以理解为老年代使用大于等于 45% 的时候，JVM 会启动垃圾回收。这个值非常重要，它决定了在什么时间启动老年代的并行回收；
+ -XX：G1HeapWastePercent：G1停止回收的最小内存大小，默认是堆大小的 5%。GC 会收集所有的 Region 中的对象，但是如果下降到了 5%，就会停下来不再收集了。就是说，不必每次回收就把所有的垃圾都处理完，可以遗留少量的下次处理，这样也降低了单次消耗的时间；
+ -XX：+GCTimeRatio：这个参数就是计算花在 Java 应用线程上和花在 GC 线程上的时间比率，默认是 9，跟新生代内存的分配比例一致。这个参数主要的目的是让用户可以控制花在应用上的时间，G1 的计算公式是 100/（1+GCTimeRatio）。这样如果参数设置为9，则最多 10% 的时间会花在 GC 工作上面。Parallel GC 的默认值是 99，表示 1% 的时间被用在 GC 上面，这是因为 Parallel GC 贯穿整个 GC，而 G1 则根据 Region 来进行划分，不需要全局性扫描整个内存堆.
+ -XX：MaxGCPauseMills：预期 G1 每次执行 GC 操作的暂停时间，单位是毫秒，默认值是 200 毫秒，G1 会尽量保证控制在这个范围内。

处理过程
1. 年轻代模式转移暂停(Evacuation Pause):
2. 并发标记（Concurrent Marking):过程基本与CMS一样
    + Phase1:Initial Mark（初始标记）
    + Phase2:Root Region Scan（Root区扫描）
    + Phase3:Concurrent Mark（并发标记）
    + Phase4:Remark（再次标记）
    + Phase5:Cleanup（清理）
3. 转移暂停: 混合模式（Evacuation Pause (mixed)）

G1 GC触发Full GC的三种情况（退化成Serial GC）
1. 并发模式失败。
<u>解决办法</u>
    增加堆大小，或者调整周期
2. 晋升失败
<u>解决办法</u>
+ 增加 –XX：G1ReservePercent 选项的值（并相应增加总的堆大小）增加预留内存量。
+ 通过减少 –XX：InitiatingHeapOccupancyPercent 提前启动标记周期。
+ 增加 –XX：ConcGCThreads 选项的值来增加并行标记线程的数目。
3. 巨型对象分配失败
<u>解决办法</u> 增加内存或者增大 -XX：G1HeapRegionSize

通过作业1的观察，混合模式的转移暂停不一定紧跟并发标记阶段。在并发标记与混合转移暂停之间，可能存在多次young模式的转移暂停，并且混合模式的转移暂停在一次并发标记后会发生多次。

---