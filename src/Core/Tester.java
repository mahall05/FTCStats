package Core;

public class Tester {
    public int num;

    public Tester(int num){
        this.num = num;
    }
    public static void main(String[] args){
        Tester t = new Tester(5);
        System.out.println(func(Tester::getNum, t));
    }

    private static int func(IntNumPredicate p, Tester t){
        return p.getNum(t);
    }

    public interface IntNumPredicate{
        int getNum(Tester t);
    }

    public int getNum(){
        return num;
    }
}
