package lc;

public class Solution {
    public static void main(String[] args){
//        System.out.println(StringToInteger("00100"));
        palindrome(10);
    }

    static boolean palindrome(int x ){
        int revert = 0;
        while (x > revert){
            revert = revert * 10 + x % 10;
            x = x / 10;
            System.out.println(x + " " + revert);
        }
        return x == revert || x == revert / 10;
    }

    private static int StringToInteger(String str){
        int n = str.length();
        long result = 0, sign=1;
        boolean negativeSetting = true, end = false;
        int[] nums = new int[n];
        int numsIndex = 0;
        for (int i = 0; i < n; i++){
            char c = str.charAt(i);
            if (negativeSetting && c == ' ')
                if (numsIndex != 0)
                    break;
                else continue;
            if (negativeSetting && (c == '-' || c == '+')){
                sign = c == '-' ? -1:1;
                negativeSetting = false;
            }else if (c>='0' && c<='9'){
                int value = c-'0';
                nums[numsIndex++] = value;
            }else if (!negativeSetting){
                break;
            }
        }
        if (numsIndex == 0)
            return 0;
        int exp = 0;
        for (int i = numsIndex-1; i>=0; i--){
            long times = (long)Math.pow(10, exp);
            result += nums[i]*times;
            exp++;
        }
        if (result < 0)
            if (sign == 1)
                return Integer.MAX_VALUE;
            else return Integer.MIN_VALUE;
        result *= sign;
        if (result > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        if (result < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        return (int)result;
    }
}
