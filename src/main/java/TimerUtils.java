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
         final String oldName = "QQ��ҵ����HR�ʼ�.csv";
         final String url = "https://exmail.qq.com/login/";
         final String filePath = "D:\\csv";
        //�õ�ʱ����
        Calendar date = Calendar.getInstance();
        //����ʱ��Ϊ xx-xx-xx 12:00:00
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 12, 0, 0);
        //һ��ĺ�����
        long daySpan = 24 * 60 * 60 * 1000;
        //�õ���ʱ��ʵ��
        Timer t = new Timer();
        //ʹ�������ڷ�ʽ���з�������
        t.schedule(new TimerTask() {
            public void run() {
                //run����д��ʱ����Ҫִ�еĴ����
                System.out.println("��ʱ��ִ��..");

                String newName = null;

                try {
                   newName =  HttpUtils.parserHtml(url);
                    File newFile = new File(filePath,newName);
                    File oldFile = new File(filePath,oldName);
                    //���������ȡ�ɹ�����ɾ��ԭ����csv
                    if(newFile.length()>100){
                        if(oldFile.exists()){
                            CSVUtils.deleteFile(filePath,oldName);
                        }
                        //�����ļ����ָ���Ϊԭ�ļ���
                        NameFile.renameFile(filePath,newName,oldName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // HttpUtils.main(String[] args);
            }
        }, date.getTime(), daySpan); //daySpan��һ��ĺ�������Ҳ��ִ�м��
    };
}
