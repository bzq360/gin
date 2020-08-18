public class testMethodFileGenerator {

    public static void main(String[] args) {
        int n = 28;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("faulty_programs.LIS_TEST.test_").append(i).append(" []");
            if (i < n - 1) {
                sb.append(", ");
            }
        }

        System.out.println(sb.toString());
    }

}
