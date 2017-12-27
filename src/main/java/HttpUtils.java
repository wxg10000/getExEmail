import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/6/24.
 */
public class HttpUtils {
    public static String getAjaxCotnent(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("casperjs qqemail.js --ssl-protocol=any --ignore-ssl-errors=yes");//�����ҵ�qqemail.js
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while((tmp = br.readLine())!=null){
            //new String(s1.getBytes("utf-8"), "gbk")
            sbf.append(tmp);
           //System.out.println(tmp);
        }
        //System.out.println(sbf.toString());
        return sbf.toString();
    }

    public static String parserHtml(String url) throws IOException {
        List exportData = new ArrayList<Map>();

       String msg =  getAjaxCotnent("https://exmail.qq.com/login");
      // System.out.println(msg);
        String[] strArr = msg.replace("&nbsp;","").split("<tbody><tr><td class=\"cx\">");
       System.out.println(strArr.length);
       // System.out.println(strArr[1]);
//        System.out.println(strArr[2]);
       // System.out.println(strArr[strArr.length-1]);
        //����Map row1
        Map row1;

        for(int i=1;i<strArr.length;i++) {
            if (strArr[i].startsWith("<input totime=\"")) {

            String timeStr = strArr[i].split("<input totime=\"")[1].split("\" type=\"checkbox\"")[0].substring(0, 13);
            //ʱ��ת��
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(Long.parseLong(timeStr));

            String service = strArr[i].split("</nobr></td><td class=\"fg_n \">")[0].split("\"><nobr>")[1];
           // String service = strArr[i].split("type=\"checkbox\" unread=\"true\" fn=\"")[1].split("\" fa=\"")[0];
            String topic = strArr[i].split("class=\"black \">")[1].split("</u><b class=\"no \">")[0];
            String name = null;
            if (topic.startsWith("��")) {

                //name = topic.split("_������Ƹ���ĺ�ѡ��")[0].split("�� ")[1];
                name = topic;
                service = "��Ƹ��";
            } else if (service.startsWith("������ְ��")) {
                // System.out.println(service);
                name = service.split("��ְ��")[1];
                service = "������Ƹ";
            } else if(service.equals("������")){
                if(topic.contains("�ļ���(��������)")){
                    name = topic.split("�ļ���(��������)")[0].split(":")[1];
                }else {
                    name = topic;
                }
                if(name.contains("<")){
                    name = name.split("<")[0];
                }
                service = "������";
            }else if(service.equals("����")){
                name = topic.split("�ļ���")[0].replace("����������ѡ��","");
            }else if(service.contains("51job")) {
                name = service.replace("ǰ������[51job]", "");
                service = "ǰ������";
            }else if(service.contains("BossֱƸ")) {
                //���۾���-�ٱ�-����н��10k-20k-����4�꡾BossֱƸ��
                name = topic.split("-����н��")[0].split("-")[1];
            }else{
                name = topic;
            }
            // String info = strArr[i].split("<b class=\"no \">&nbsp;-&nbsp;")[1].split("</b>&nbsp;</div><div class=\"TagDiv\">")[0];
            String job = strArr[i].split("<td class=\"ts\" title=\"")[1].split("\"><nobr fid=\"")[0];
            //String name = strArr[i].split("<td class=\"ts\" title=\"")[1].split("\"><nobr fid=\"")[0];
            String mark = strArr[i].split("</div></td><td class=\"fg")[1].split("\"><div></div></td></tr></tbody></table></td></tr></tbody></table>")[0];
            if (mark.trim().equals("fs1")){
                mark = "���";
            } else {
                mark = "δ���";
            }

            // System.out.println(topic);
            if(service.contains("<")){
                service = service.split("</nobr></td><td class=\"fg_n  fr\">")[0];
            }
            if(name.contains("</nobr></td><td class=\"fg_n  fr\">")){
                name = name.split("</nobr></td><td class=\"fg_n  fr\">")[0];
            }
            if(name.contains("</u><b class=\"no addrtitle\">")){
                name = name.split("</u><b class=\"no addrtitle\">")[0];
            }
            if(name.contains("_������Ƹ���ĺ�ѡ��")){
                name = name.split("_������Ƹ���ĺ�ѡ��")[0].split(" ")[1];
            }else if(name.contains("�ļ���(��������)")){
                name = name.replace("�ļ���(��������)","");
            }else if(name.contains("����������վ�᡿")){
                name = name.split("-")[1].trim().replace("�ļ���","");
            }
           if(name.contains(",")){
                name = name.replace(",",";");
           }
           if(service.contains(",")){
               service = service.replace(",","");
           }
           if(name.contains("- ��������(neitui.Me)")){
               name = name.replace("- ��������(neitui.Me)","");
               if(name.contains("\\s")){
                  name = name.split("\\s")[0];
               }
               if(name.contains("-")){
                    name = name.split("-")[1];
               }
           }
           System.out.println("time:" + time + ";service:" + service + ";name:" + name + ";job:" + job + ";mark:" + mark);
            row1 = new LinkedHashMap<String, String>();
            row1.put("1", time);
            row1.put("2", service);
            row1.put("3", name);
            row1.put("4", job);
            row1.put("5", mark);
            exportData.add(row1);
          }
        }
        LinkedHashMap map = new LinkedHashMap();
        map.put("1", "����");
        map.put("2", "������");
        map.put("3", "����");
        map.put("4", "ְλ");
        map.put("5", "����Ǳ�");


//time:1498496496;service:������;topic:��Ʒ����:�ź�Ȼ�ļ���(��������)<b class="no">(2) </b>;info:��������|��ҳ&nbsp;DataHunter&nbsp;��&nbsp;hr����ã�&nbsp;���������������������ġ�&nbsp;��Ʒ����&nbsp;��ְλ�յ��ļ�����&nbsp;�ź�Ȼ:job:��Ʒ����

        String path = "D:/csv/";
        String fileName = "QQ��ҵ����HR�ʼ�";
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);
        String fileName2 = file.getName();
        System.out.println("�ļ����ƣ�" + fileName2);
        return fileName2;
    }

    public static void main(String[] args) {
        String url = "https://exmail.qq.com/login";
        try {
            HttpUtils.parserHtml(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
