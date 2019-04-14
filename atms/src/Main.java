import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Solve solve=new Solve();
        List<Atm> atmsroute=new ArrayList<>();
        atmsroute=solve.getAtmsRoute();
        System.out.println("\nATMs route, in order in which they are visited:");
        atmsroute.stream().forEach(System.out::println);
    }
}
