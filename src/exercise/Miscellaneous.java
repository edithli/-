package exercise;

public class Miscellaneous {
    public static void main(String[] args){
//        System.out.println(DragonCurve(0));
//        System.out.println(DragonCurve(1));
//        System.out.println(DragonCurve(2));
//        System.out.println(DragonCurve(3));
//        System.out.println(DragonCurve(4));
//        System.out.println(DragonCurve(5));
//        RGBtoYIQ(32,65, 32);
//        CMYKtoRGB(0.2,0.3, 0.4,0.2);
//        RGBtoCMYK(255,0, 255);
//        System.out.println(Math.sqrt(2)*Math.sqrt(2)==2);
//        DivideByZero();
//        System.out.println(FibonacciWord(5));
//        int threeInt = 3;
//        int fourInt  = 4;
//        double threeDouble = 3.0;
//        double fourDouble  = 4.0;
//        System.out.println(threeInt / fourInt);
//        System.out.println(threeInt / fourDouble);
//        System.out.println(threeDouble / fourInt);
//        System.out.println(threeDouble / fourDouble);
//        TriangleArea(3,4,6);
//        System.out.println("BMI: " + BodyMassIndex(50, 1.65));
//        System.out.println(FahrenheitToCelsius(44));
//        int a = -(2147483648);
//        FunctionGrowth();
//        Fibonacci(10);
//        EstimatePI(1000000);
//        Kary(7582, 2);
//        IntegerToBinaryString(7582);
//        Ramanujan(100000000);
//        Checksum(20131452);
//        Exp(-10);
        MonteHall(1000000);
    }
    static int maxCrossingSum(int arr[], int l, int m, int h) {
        int sum = 0;
        int left_sum = Integer.MIN_VALUE;
        for (int i = m; i >= l; i--) {
            sum = sum + arr[i];
            if (sum > left_sum)
                left_sum = sum;
        }
        sum = 0;
        int right_sum = Integer.MIN_VALUE;
        for (int i = m + 1; i <= h; i++)
            sum = sum + arr[i];
        if (sum > right_sum)
            right_sum = sum;
        return left_sum + right_sum;
    }

    static int maxSubArraySum(int arr[], int l, int h) {
        if (l == h)
            return arr[l];
        int m = (l + h)/2;
        return Math.max(Math.max(maxSubArraySum(arr, l, m),
                maxSubArraySum(arr, m+1, h)),
                maxCrossingSum(arr, l, m, h));
    }

    private static void MonteHall(int n){
        int[] doors = new int[3];
        int switchWin = 0, unswitchWin = 0;
        for (int i = 0; i < n; i++){
            int prize = (int)Math.round(Math.random()*3);
            int firstChoise = (int)Math.round(Math.random()*3);
            if (firstChoise == prize){
                unswitchWin++;
            }else {
                switchWin++;
            }
        }
        System.out.println("Switch: " + (double)switchWin/n);
        System.out.println("Unswitch: " + (double)unswitchWin/n);
    }

    private static void IntegerToBinaryString(int n){
        StringBuilder s = new StringBuilder();
        while (n > 0){
            s.insert(0, (n % 2));
            n /= 2;
        }
        System.out.println(s);
    }

    private static void Kary(int n, int base){
        System.out.println(Integer.toString(n, base) + " compare");
        int power = 1;
        while (power <= n/base)
            power *= base;
        while (power > 0){
            if (n < power)
                System.out.print(0);
            else {
                int b = n / power;
                if (b >= 10)
                    System.out.print((char)(b-10+'A'));
                else System.out.print(b);
                n %= power;
            }
            power /= base;
        }
        System.out.println();
    }

    private static void EstimatePI(int n){
        double sum = 0, sum2 = 0;
        for (int i = 1; i <= n; i++){
            sum += 1 / (1.0*i*i);
            sum2 += 1.0 / (i*i); // when i is too large, i*i will overflow
        }
        double Pi = Math.sqrt(6.0*sum), Pi2 = Math.sqrt(6.0*sum2);
        System.out.println(sum);
        System.out.println(sum2);
    }

    private static String DragonCurve(int order){
        if (order == 0) return "F";
        String part1 = DragonCurve(order-1);
        StringBuilder sb = new StringBuilder();
        sb.append(part1);
        sb.append("L");
        for (int i = part1.length()-1; i >= 0; i--){
            char c = part1.charAt(i);
            if (c == 'F')
                sb.append('F');
            else if (c == 'L')
                sb.append('R');
            else if (c == 'R')
                sb.append('L');
        }
        return sb.toString();
    }

    private static void RGBtoYIQ(int r, int g, int b){
        int[] result = new int[3];
        int y = (int)(0.299*r+0.587*g+0.114*b);
        int i = (int)(0.596*r-0.274*g-0.322*b);
        int q = (int)(0.211*r-0.523*g+0.312*b);
        System.out.println("Y: " + y);
        System.out.println("I: " + i);
        System.out.println("Q: " + q);
    }

    private static void CMYKtoRGB(double C, double M, double Y, double K){ // CMYK all in [0,1]
        int R = (int)Math.round(255*(1-C)*(1-K));
        int G = (int)Math.round(255*(1-M)*(1-K));
        int B = (int)Math.round(255*(1-Y)*(1-K));
        System.out.println("R: " + R);
        System.out.println("G: " + G);
        System.out.println("B: " + B);
    }

    private static void RGBtoCMYK(int R, int G, int B){
        double r = (double)R/255, g = (double)G/255, b = (double)B/255;
        double K = 1 - Math.max(r, Math.max(g, b));
//        double C = 1 - r/(1-K);
        double C = (1-r-K)/(1-K);
        double M = (1-g-K)/(1-K);
        double Y = (1-b-K)/(1-K);
        System.out.println("C: " + C);
        System.out.println("M: " + M);
        System.out.println("Y: " + Y);
        System.out.println("K: " + K);
    }

    private static void DivideByZero(){
        System.out.println("17.0/0.0: " + (17.0/0.0)); // infinity
        System.out.println("17.0%0.0: " + (17.0%0.0)); // nan
        System.out.println("17%0: " + (17%0)); // java.lang.ArithmeticException: / by zero
        System.out.println("17/0: " + (17/0)); //java.lang.ArithmeticException: / by zero
    }

    private static String FibonacciWord(int n){
        if (n == 0) return "a";
        if (n == 1) return "b";
        return FibonacciWord(n-1) + FibonacciWord(n-2);
    }

    private static double TriangleArea(double a, double b, double c){
        //Heron's formula: area = sqrt(s(s-a)(s-b)(s-c)), where s = (a + b + c) / 2
        double s = (a+b+c)/2;
        double area = Math.sqrt(s * (s-a)*(s-b)*(s-c));
        System.out.println("Triangle area: " + area);
        return area;
    }

    private static void EquatorialToHorizontal(double declination, double hourAngle, double latitude){
        // Altitude = asin (sin φ sin δ  + cos φ cos δ cos H)
        // Azimuth  = acos ((cos φ sin δ  - sin φ cos δ cos H) / cos (Altitude) )
        double Altitude = Math.asin(Math.sin(latitude)*Math.sin(declination) + Math.cos(latitude)*Math.cos(declination)*Math.cos(hourAngle));
        double Azimuth = Math.acos((Math.cos(latitude)*Math.sin(declination)-Math.sin(latitude)*Math.cos(declination)*Math.cos(hourAngle))/Math.cos(Altitude));
        System.out.println("Altitude: " + Altitude + "\n" + "Azimuth: " + Azimuth);
    }

    private static double BodyMassIndex(double weight, double height){
        return weight / (height*height);
    }

    private static double FahrenheitToCelsius(double Fahrenheit){
        return (Fahrenheit-32)*5/9;
    }

    private static void FunctionGrowth(){
        for (int i = 4; i <= 11; i++){
            int n = (int)Math.pow(2, i);
            StringBuilder sb = new StringBuilder();
            sb.append(Math.log(n));
            sb.append("\t");
            sb.append(n);
            sb.append("\t");
            sb.append(n * Math.log(n));
            sb.append("\t");
            sb.append(Math.pow(n, 2));
            sb.append("\t");
            sb.append(Math.pow(n, 3));
            sb.append("\t");
            sb.append(Math.pow(2, n));
            System.out.println(sb.toString());
        }
    }

    private static void Fibonacci(int n){
        int f = 0, g = 1;
        for (int i = 0; i < n; i++){
            System.out.println(f);
            f = f+g;
            g = f-g;
        }
    }

    // !!!
    // return all numbers less or equal than n that is two pairs of numbers whose cube sum are the same
    private static void Ramanujan(int n){
        for (int i = 1; i < n; i++){
            int i3 = i*i*i;
            if (i3>=n) break;
            for (int j = i; j<n; j++){
                int j3 = j*j*j;
                if (j3>=n||j3+i3>n)break;
                for (int k = i; k < n; k++){
                    int k3 = k*k*k;
                    if (k3>=n) break;
                    for (int l = k; l < n; l++){
                        int l3 = l*l*l;
                        if (l3 >= n||l3+k3 > n) break;
                        if (i3+j3 == k3+l3){
                            if (k != i && k != j)
                                System.out.println(i+"^3+"+j+"^3="+k+"^3+"+l+"^3="+(k3+l3));

                        }
                    }
                }
            }
        }
    }

    private static void Checksum(int n){
        // input a 9-digit integer
        int DIGIT_COUNT = 9, TOTAL_DIGIT = 10;
        int[] digits = new int[DIGIT_COUNT];
        int index = 0;
        while (n != 0){
            if (index >= 9)
                throw new RuntimeException("input should be a 9-digit number");
            digits[index] = n % 10;
            System.out.print(digits[index]+" ");
            n = n/10;
            index++;
        }
        System.out.println();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= TOTAL_DIGIT; i++){
            int sum = 0;
            for (int j = 0; j < DIGIT_COUNT; j++){
                sb.insert(0, digits[j]);
                if (j+1 < i){
                    sum += (j+1)*digits[j];
                }else{
                    sum += (j+2)*digits[j];
                }
            }
            for (int j = 0; j <= 10; j++){
                int check = i * j;
                if ((sum+check) % 11 == 0){
                    System.out.println("checksum: " + j);
                    System.out.println("position: " + i);
                    sb.insert(TOTAL_DIGIT-i, j==10?'X':j+"");
                    System.out.println("ISBN: " + sb.toString());
                    return;
                }
            }
            sb.delete(0, sb.length());
        }
    }
    private static void Exp(double x){
        // compute e^x using Taylor series expansion
//        double result = 1 + x;
//        int factor = 1;
//        for (int i = 2; i <= 4; i++){
//            factor *= i;
//            result += (Math.pow(x, i)/factor);
//        }
//        System.out.println("e^x: " + result);
        System.out.println(Math.exp(x));
        boolean isNegative = false;
        if (x < 0) {
            x = -x;
            isNegative = true;
        }
        double sum = 0;
        double term = 1.0;
        for (int n = 1; sum != sum+term; n++){
            sum += term;
            term *= x/n;
        }
        if (isNegative)
            sum = 1.0/sum;
        System.out.println(sum);
    }
}
