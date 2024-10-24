public class ContainThread extends TestThread {
    private int successCount = 0;
    private int failureCount = 0;
    public ContainThread(RandomSeq seq, int seqPart, SortList setList) {
        super(seq, seqPart, setList);
    }

    public int getFailureCount() {
        return failureCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            boolean result = list.contain(nums[i]);
            if(result){
                successCount++;
            }
            else{
                failureCount++;
            }
        }
    }
}
