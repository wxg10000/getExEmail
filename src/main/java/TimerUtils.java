/**
 * Created by root on 2017/6/29.
 */

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {
    public static void main(String[] args) throws Exception {
         final String oldName = "QQ企业邮箱HR邮件.csv";
         final String url = "https://exmail.qq.com/login/";
         final String filePath = "D:\\csv";
        //得到时间类
        Calendar date = Calendar.getInstance();
        //设置时间为 xx-xx-xx 12:00:00
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 12, 0, 0);
        //一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;
        //得到定时器实例
        Timer t = new Timer();
        //使用匿名内方式进行方法覆盖
        t.schedule(new TimerTask() {
            public void run() {
                //run中填写定时器主要执行的代码块
                System.out.println("定时器执行..");

                String newName = null;

                try {
                   newName =  HttpUtils.parserHtml(url);
                    File newFile = new File(filePath,newName);
                    File oldFile = new File(filePath,oldName);
                    //如果数据爬取成功，则删掉原来的csv
                    if(newFile.length()>100){
                        if(oldFile.exists()){
                            CSVUtils.deleteFile(filePath,oldName);
                        }
                        //将新文件名字更改为原文件名
                        NameFile.renameFile(filePath,newName,oldName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // HttpUtils.main(String[] args);
            }
        }, date.getTime(), daySpan); //daySpan是一天的毫秒数，也是执行间隔
    };
}
