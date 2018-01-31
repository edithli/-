import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println("Hello World!");
//        System.out.println(args.length);
//        Scanner sc = new Scanner(System.in);
//        String initials = sc.nextLine();
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**              ");
//        System.out.println("**************  ");
//        System.out.println("**************  ");
        // student number parser
        int POINT = 4;
        Map<String, Integer> scores = new TreeMap<>();
        BufferedReader main = new BufferedReader(new InputStreamReader(new FileInputStream("src/temp2.txt")));
        String line;
        while ((line = main.readLine()) != null){
            scores.put(line, 0);
        }
        main.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/temp.txt")));
        Pattern pattern = Pattern.compile("(.*)(\\d{11})(.*)");
        while ((line = br.readLine()) != null){
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String stdno = matcher.group(2);
                if (!scores.containsKey(stdno))
                    System.out.println("Student no. not int map!!! - " + stdno);
                else
                    scores.put(stdno, POINT);
            }else
                System.out.println(line);
        }
//        while ((line = br.readLine()) != null){
//            String[] splits = line.split("\\s");
//            if (!scores.containsKey(splits[0]))
//                System.out.println("Student no. not in map: " + splits[0]);
////            int score = Integer.parseInt(splits[1]);
//            double score = Double.parseDouble(splits[2]);
//            scores.put(splits[0], score*0.35);
//        }
        br.close();
        for (String stdno: scores.keySet()){
            System.out.println(stdno + "\t" + scores.get(stdno));
        }
    }
}
