import java.text.SimpleDateFormat;

/**
 * Created by root on 2017/6/29.
 */
public class DateUtils {

    public static String stringToDate(String time) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(Long.parseLong(time)));
        return sdf.format(Long.parseLong(time));
    }

    public static void main(String[] args) {
        String time = "1074353009253";
        try {
            String s = DateUtils.stringToDate(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}