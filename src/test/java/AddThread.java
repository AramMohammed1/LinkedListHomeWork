


public class AddThread extends TestThread  {
    public AddThread(RandomSeq seq, int seqPart, SortList setList) {
        super(seq, seqPart, setList);
    }

    @Override
    public void run() {
        for (int i = 0; i < nums.length; i++) {
            list.add(nums[i]);
        }
    }
}
