public class RemoveThread extends TestThread {
    public RemoveThread(RandomSeq seq, int seqPart, SortList setList) {
        super(seq, seqPart, setList);
    }
    private int successCount = 0;
    private int failureCount = 0;

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            boolean result=list.remove(nums[i]);
            if(result){
                successCount++;
            }
            else{
                failureCount++;
            }

        }
    }
}