package lc;

public class ZigZagConvertion {
    public static void main(String[] args){
//        System.out.println(convert("Apalindromeisaword,phrase,number,orothersequenceofunitsthatcanbereadthesamewayineitherdirection,withgeneralallowancesforadjustmentstopunctuationandworddividers.", 3));
        System.out.println(reverse(1534236469));
    }

    private static String convert(String s, int numRows){
        if (numRows == 1)
            return s;
        int n = s.length();
        int loop = 2 * numRows - 1;
        int totalRound = n / loop + 1;
        int length = (numRows - 1)*totalRound + 1;
        int[][] temp = new int[numRows][length];
        int p = 0;
        for (int round = 0; round <= totalRound && p < n; round++){
            int row = 0;
            int loopIndex = round * (numRows-1);
            while (row < numRows && p < n){
                temp[row][loopIndex] = s.charAt(p);
                row++;
                p++;
            }
            row-=2;
            loopIndex++;
            while (row > 0 && p < n && loopIndex<length){
                temp[row][loopIndex] = s.charAt(p);
                row--;
                loopIndex++;
                p++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++){
            for (int j = 0; j <= (numRows - 1)*totalRound; j++){
                System.out.print(temp[i][j]==0?' ':(char)temp[i][j]);
                if (temp[i][j] != 0)
                    sb.append((char)temp[i][j]);
            }
            System.out.println();
        }
        return sb.toString();
    }

    private static int reverse(int x) {
        if (x == 0)
            return 0;
        int[] temp = new int[13];
        long px = x;
        if (px<0)
            px = -px;
        System.out.println(px);
        int i = 0;
        while (px != 0){
            temp[i] = (int)(px%10);
            px = px / 10;
            i++;
        }
        int exp = 0;
        boolean trailing = true;
        for (int j = i-1; j>=0; j--){
            long times = temp[j]*(long)Math.pow(10,exp);
            if (trailing && times != 0){
                trailing = false;
                px += times;
                exp++;
            }else if (!trailing){
                px += times;
                exp++;
            }
//            System.out.println(px);
        }
        System.out.println(px);
        if (x>0 && px > Integer.MAX_VALUE)
            return 0;
        if (x<0 && -px < Integer.MIN_VALUE)
            return 0;
        if (x>0)
            return (int)px;
        return (int)-px;
    }
}
