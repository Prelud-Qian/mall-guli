package com.example.mall_search.thread;

import java.util.concurrent.*;

public class ThreadTest {

    // 当前系统中池只有一两个，每个异步任务提交给线程池让它自己去执行
    public static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");

//        CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//        }, service);

        /**
         * 方法完成后的感知
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, service).whenComplete((res, exception) -> {
//            // 虽然能得到异常信息，但是没法修改返回数据
//            System.out.println("异步任务成功完成了。。。结果是：" + res + "；异常是：" + exception);
//        }).exceptionally(throwable -> {
//            // 可以感知异常 同时返回默认值
//            return 10;
//        });

        /**
         * 方法执行完成后的处理
         */
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, service).handle((res, exception) -> {
//            if (res != null){
//                return res * 2;
//            }
//            if (exception != null){
//                return 0;
//            }
//            return 0;
//        });

        /**
         * 线程串行化
         * 1. thenRun: 不能获取到上一步的执行结果 无返回值
         * .thenRunAsync(() -> {
         *             System.out.println("任务2启动了...");
         *         }, service);
         *
         * 2. thenAcceptAsync: 能接收上一步结果 但是无返回值
         * .thenAcceptAsync(res -> {
         *             System.out.println("任务2启动了..." + res);
         *         }, service);
         *
         * 3. thenApplyAsync: 能接收上一步结果 有返回值
         * .thenApplyAsync(res -> {
         *             System.out.println("任务2启动了..." + res);
         *             return "Hello" + res;
         *         }, service);
         */
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, service).thenApplyAsync(res -> {
//            System.out.println("任务2启动了..." + res);
//            return "Hello" + res;
//        }, service);

        /**
         * 两个都完成
         *
         */
        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("任务一结束：" + i);
            return i;
        }, service);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二线程：" + Thread.currentThread().getId());
            System.out.println("任务二结束");
            return "Hello";
        }, service);

//        future01.runAfterBothAsync(future02, () -> {
//            System.out.println("任务三开始");
//        }, service);

//        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
//            System.out.println("任务三开始...之前线程的结果:" + f1 + "-->" + f2);
//        }, service);

//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
//            return f1 + "-->" + f2 + "-->World";
//        }, service);

        /**
         * 两个任务,只要有一个完成,我们就执行任务3
         * runAfterEitherAsync: 不感知结果,自己也无返回值
         * acceptEitherAsync: 感知结果,自己无返回值
         * applyToEitherAsync: 感知结果,自己有返回值
         */
//        future01.runAfterEitherAsync(future02, () -> {
//            System.out.println("任务三开始");
//        }, service);

//        future01.acceptEitherAsync(future02, (res) -> {
//            System.out.println("任务三开始...之前的结果:" + res);
//        }, service);

//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
//            System.out.println("任务三开始...之前的结果:" + res);
//            return res.toString() + "-->Hello";
//        }, service);


        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        }, service);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的属性");
            return "黑色+256G";
        }, service);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品介绍");
            return "华为";
        }, service);

//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get(); // 等待所有结果完成

//        System.out.println("main...end..." + futureImg.get() + "=>" + futureAttr.get() + "=>" + futureDesc.get());
        System.out.println("main...end..." + anyOf.get());

    }

    public static void thread(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");

        Thread thread = new Thread01();
        thread.start();

        Runnable01 runnable01 = new Runnable01();
        new Thread(runnable01).start();

        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        new Thread(futureTask).start();
        // 等待整个线程执行完成，获取返回结果
        Integer integer = futureTask.get();

        service.execute(new Runnable01());

        /**
         * 七大参数
         * int corePoolSize, 核心线程数【一直存在 除非设置了allowCoreThreadTimeOut】
         *                  线程池创建好以后就准备就绪的线程数量 就等待来接收异步任务去执行
         * int maximumPoolSize, 最大线程数量 控制资源
         * long keepAliveTime, 存活时间 如果当前线程数量大于corePoolSize
         *                      释放空闲的线程（maximumPoolSize - corePoolSize）
         *                      只要线程空闲大于指定的keepAliveTime
         * TimeUnit unit, 时间单位
         * BlockingQueue<Runnable> workQueue, 阻塞队列 如果任务有很多，就会将目前多的任务放在队列里面
         *                                  只要有线程空闲，就会去队列里面取出新的任务继续执行
         * ThreadFactory threadFactory, 线程的创建工厂
         * RejectedExecutionHandler handler 如果 队列满了 并且 maximumPoolSize也满了 的情况下 按照我们指定的拒绝策略拒绝执行任务
         *
         * 工作顺序：
         * 1. 线程池创建，准备好corePoolSize的核心线程，准备接受任务 （线程池创建时 不会立即准备好核心线程，默认是懒加载的）
         * 1.1 corePoolSize满了，就将再进来的任务放入阻塞队列中 空闲的corePoolSize就会自己去阻塞队列获取任务执行
         * 1.2 阻塞队列满了，就直接开新线程执行，最大只能开到max指定的数量
         * 1.3 maximumPoolSize满了 阻塞队列满了 就用RejectedExecutionHandler拒绝
         * 1.4 maximumPoolSize都执行完成，有很多空闲 在指定的时间keepAliveTime以后，回收非核心线程（超过 corePoolSize 的那些）核心线程默认不会被回收，除非设置 allowCoreThreadTimeOut(true)
         *
         *  new LinkedBlockingDeque<>() 默认是Integer的最大值 内存不够
         *
         *  一个线程池 core 7  max 20  queue 50  100并发进来怎么分配的
         *  7个核心线程会立即得到执行  50个会进入队列  再开13个新线程进行执行   剩下的30个就使用拒绝策略
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

//        Executors.newCachedThreadPool() core是0 所有线程都可回收
//        Executors.newFixedThreadPool() 固定大小 core=max 所有线程都不可回收
//        Executors.newScheduledThreadPool() 定时任务的线程池
//        Executors.newSingleThreadExecutor() 单线程的线程池 后台从队列里面获取任务 挨个执行

        System.out.println("main...end..." + integer);

    }

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Runnable01 implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }

    public static class Callable01 implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }
    }
}
