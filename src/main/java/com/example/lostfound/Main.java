//import java.util.*;
//
//
//// 注意类名必须为 Main, 不要有任何 package xxx 信息
//public class Main {
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        // 注意 hasNext 和 hasNextLine 的区别
//        int t = in.nextInt();
//        in.nextLine();
//        while(t-- > 0) {
//            int n = in.nextInt();
//            int k = in.nextInt();
//            in.nextLine();
//            String s = in.nextLine();
//            int[] ints = new int[n];
//
//            for(int i = 0; i < n; i++) {
//                ints[i] = s.charAt(i) - '0';
//            }
////            System.out.println(Arrays.toString(ints));
//            int l = 0, r = n - 1;
//            int p = 0, h = 0;
//            while(l < r) {
//                if(ints[l] == 1 && l <= k) {
//                    swap(ints, l, p);
//                    p += 2;
//                    k -= l;
//                }
//                if(ints[r] == 1 && n - 1 - r <= k) {
//                    swap(ints, r, n - 1 - h);
//                    k -= n - 1 - r;
//                    h += 2;
//                }
//                r--;
//                l++;
//            }
//
//
//            int sum = 0;
//            for(int i = 1; i < n; i++) {
//                sum += ints[i - 1] * 10 + ints[i];
//            }
//            System.out.println(sum);
//        }
//    }
//    public static void swap(int[] a, int i , int j) {
//        int t= a[i];
//        a[i] = a[j];
//        a[j] = t;
//    }
//}