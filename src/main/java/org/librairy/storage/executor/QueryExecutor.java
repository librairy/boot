package org.librairy.storage.executor;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.concurrent.*;

/**
 * Created by cbadenes on 09/03/16.
 */
@Component
public class QueryExecutor {

    private static final Integer NUM_THREADS = 50;

    ScheduledThreadPoolExecutor pool;

    @PostConstruct
    public void setup(){
//        //pool = Executors.newFixedThreadPool(50);
//
        int corePoolSize    = 25;
        int maximumPoolSize = 50;
        long keepAliveTime  = 60;
        TimeUnit unit       = TimeUnit.SECONDS;


        int initialCapacity = 100;
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                QueryTask qt1 = (QueryTask) o1;
                QueryTask qt2 = (QueryTask) o2;
                return qt1.getPriority().compareTo(qt2.getPriority());
            }
        };
        BlockingQueue workQueue = new PriorityBlockingQueue<>(initialCapacity,comparator);

//        pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        pool = new ScheduledThreadPoolExecutor(corePoolSize);
    }

    public void execute(QueryTask task){
        //pool.execute(task);
        pool.schedule(task,task.getDelay(), TimeUnit.MILLISECONDS);
    }



}
