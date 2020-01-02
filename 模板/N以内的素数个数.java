
/**
 * @Classname Test
 * @Description 埃拉托斯特尼筛法
 * @Date 2019/12/31 10:18
 * @Author SonnSei
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(primeFilter(2));
    }
    static int primeFilter(int num){
        boolean array[] = new boolean[num+1];
        for(int i = 2;i<=num;i++){
            array[i] = true;
        }
        int count =0;//素数个数
        for(int i=2;i*i<=num;i++){
            if(!array[i])//不是素数
                continue;
            //将当期素数的倍数置为false，为合数
            int j= i*2;
            while(j<=num){
                if(array[j])
                    array[j]= false;
                j+=i;
            }
        }
        for(boolean bool:array)
        {
            if(bool)
                count++;
        }
        return count;
    }
}
