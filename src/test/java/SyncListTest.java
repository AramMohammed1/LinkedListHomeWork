import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


public class SyncListTest extends TestCase {
    private int listLengthAfterAdds;
    private int listLengthAfterRemove;
    private String isSorted="Sorted";
    public void testAddList(){

        SyncList syncList = new SyncList();
//        syncList.remove(Integer.MAX_VALUE);
        syncList.add(1);
        syncList.add(2);
        syncList.add(3);
        syncList.add(Integer.MIN_VALUE);
        syncList.add(3);
        System.out.println(syncList.contain(5));
        System.out.println(syncList.contain(2));
        System.out.println(syncList.contain(3));
    }

    public void testRandSeq() {
        RandomSeq randomSeq = new RandomSeq(0, 80_000);
        for (int i = 0; i < 10; i++) {
            System.out.print(randomSeq.next() + " ");
        }
    }

    public void testHelp(SortList list, String label,int randLen, int threadCount) {
        RandomSeq seq = new RandomSeq(0, 80_000);
        isSorted="Sorted";
        List<AddThread> addThreads = new ArrayList<>();
        List<ContainThread> containThreads = new ArrayList<>();
        List<RemoveThread> removeThreads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            AddThread addThread = new AddThread(seq, randLen /threadCount, list);
            ContainThread containThread = new ContainThread(seq, randLen / threadCount, list);
            RemoveThread removeThread = new RemoveThread(seq, randLen / threadCount, list);
            Thread threadA = new Thread(addThread);
            addThreads.add(addThread);
            Thread threadC = new Thread(containThread);
            containThreads.add(containThread);
            Thread threadR = new Thread(removeThread);
            removeThreads.add(removeThread);
        }

        long startA = System.currentTimeMillis();

        addThreads.stream().forEach(e -> e.start() );
        addThreads.stream().forEach(e -> {
            try {
                e.join();

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        long endA = System.currentTimeMillis() - startA;
        System.out.println("ADD "+label+" execution task: "+endA+" ms");
        this.listLengthAfterAdds = calculateListLength(list);
        System.out.println("List Length after adds: " + this.listLengthAfterAdds);
        System.out.println("The List is " + isSorted);

        List<Integer> successCountList =new ArrayList<>();
        List<Integer> failureCountList =new ArrayList<>();
        long startC = System.currentTimeMillis();
       containThreads.stream().forEach(e -> e.start() );
       containThreads.stream().forEach(e -> {
           try {
               e.join();
               failureCountList.add(e.getFailureCount());
               successCountList.add(e.getSuccessCount());
           } catch (InterruptedException ex) {
               throw new RuntimeException(ex);
           }
       });
       long endC = System.currentTimeMillis() - startC;

       System.out.println("Contain "+label+" execution task: "+endC+" ms");
        int totalSuccess = successCountList.stream().mapToInt(e -> e).sum();
        int totalFailure = failureCountList.stream().mapToInt(e -> e).sum();
        System.out.println("Total number of successes found:" + totalSuccess + ", failures found:"+totalFailure);
        successCountList.clear();
        failureCountList.clear();
       long startR = System.currentTimeMillis();

       removeThreads.stream().forEach(e -> e.start() );
       removeThreads.stream().forEach(e -> {
           try {
               e.join();
                successCountList.add(e.getSuccessCount());
                failureCountList.add(e.getFailureCount());
           } catch (InterruptedException ex) {
               throw new RuntimeException(ex);
           }
       });
       long endR = System.currentTimeMillis() - startR;

        this.listLengthAfterRemove = calculateListLength(list);
        totalSuccess = successCountList.stream().mapToInt(e -> e).sum();
        totalFailure = failureCountList.stream().mapToInt(e -> e).sum();

       System.out.println("Remove "+label+" execution task: "+endR+" ms");
       System.out.println("List length after remove  "+listLengthAfterRemove);
        System.out.println("Total number of successes Removed:" + totalSuccess + ", failures found:"+totalFailure);
    }
    private int calculateListLength(SortList list) {
        int length = 0;
        Entry current = list.head;
        int prev=Integer.MIN_VALUE;
        while (current != null) {
            length++;
            if(current.object<prev){
                isSorted="Not Sorted";
            }
            prev=current.object;
            current = current.next;

        }
        return length - 2;
    }


    public void testRun() {
        int[] listLengths = {10000, 20000, 50000,100000};
        int[] threadCounts = {2, 4, 6, 8, 16, 32};

        for (int length : listLengths) {
            for (int threadCount : threadCounts) {
                System.out.println("Testing with list length: " + length + " and thread count: " + threadCount);

                SyncList syncList = new SyncList();
                System.out.println("Testing SyncList:");
                testHelp(syncList, "Synchronization", length, threadCount);

                RWLockList rwLockList = new RWLockList();
                System.out.println("Testing RWLockList:");
                testHelp(rwLockList, "RWLock", length, threadCount);

                LockList lockList = new LockList();
                System.out.println("Testing LockList:");
                testHelp(lockList, "Lock", length, threadCount);

                System.out.println("==============");
            }
        }
    }}
