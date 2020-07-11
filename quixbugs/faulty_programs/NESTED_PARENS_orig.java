package faulty_programs;//Corrected package name from quixey to java_programs.extra;
//package quixey;

import java.util.*;

//Corrected class from from java_programs.NESTED_PARENS to java_programs.NESTED_PARENS_orig
public class NESTED_PARENS_orig {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        CAS: while(in.hasNext()) {
            String parens = in.next();
            int depth = 0;
            for(int i=0; i < parens.length(); i++) {
                if(parens.charAt(i) == '(') {
                    depth++;
                } else {
                    depth--;
                    if(depth < 0) {
                        System.out.println("0");
                        continue CAS;
                    }
                }
            }
            System.out.println(depth == 0 ? "1" : "0");
        }
    }

    public static <T> List<T> list() { return new ArrayList<T>(); }
    public static <K,V> Map<K,V> map() { return new HashMap<K,V>(); }
    public static int i(String s) { return Integer.parseInt(s); }
    public static long l(String s) { return Long.parseLong(s); }
}
