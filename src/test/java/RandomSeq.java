import java.util.Random;

public class RandomSeq {

    int seed;
    Random random;
    int maxNum;

    public RandomSeq(int setseed ,int setmaxNum) {
        random = new Random(setseed);
        this.seed = setseed;
        this.maxNum = setmaxNum;
    }
    public int next(){
        return random.nextInt(maxNum);
    }
}
