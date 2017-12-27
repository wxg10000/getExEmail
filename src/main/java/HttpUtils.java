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
        Process p = rt.exec("casperjs qqemail.js --ssl-protocol=any --ignore-ssl-errors=yes");//这里我的qqemail.js
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
        //创建Map row1
        Map row1;

        for(int i=1;i<strArr.length;i++) {
            if (strArr[i].startsWith("<input totime=\"")) {

            String timeStr = strArr[i].split("<input totime=\"")[1].split("\" type=\"checkbox\"")[0].substring(0, 13);
            //时间转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(Long.parseLong(timeStr));

            String service = strArr[i].split("</nobr></td><td class=\"fg_n \">")[0].split("\"><nobr>")[1];
           // String service = strArr[i].split("type=\"checkbox\" unread=\"true\" fn=\"")[1].split("\" fa=\"")[0];
            String topic = strArr[i].split("class=\"black \">")[1].split("</u><b class=\"no \">")[0];
            String name = null;
            if (topic.startsWith("【")) {

                //name = topic.split("_来自猎聘网的候选人")[0].split("】 ")[1];
                name = topic;
                service = "猎聘网";
            } else if (service.startsWith("智联求职者")) {
                // System.out.println(service);
                name = service.split("求职者")[1];
                service = "智联招聘";
            } else if(service.equals("拉勾网")){
                if(topic.contains("的简历(来自拉勾)")){
                    name = topic.split("的简历(来自拉勾)")[0].split(":")[1];
                }else {
                    name = topic;
                }
                if(name.contains("<")){
                    name = name.split("<")[0];
                }
                service = "拉勾网";
            }else if(service.equals("脉脉")){
                name = topic.split("的简历")[0].replace("来自脉脉候选人","");
            }else if(service.contains("51job")) {
                name = service.replace("前程无忧[51job]", "");
                service = "前程无忧";
            }else if(service.contains("Boss直聘")) {
                //销售经理-荣彬-期望薪资10k-20k-工作4年【Boss直聘】
                name = topic.split("-期望薪资")[0].split("-")[1];
            }else{
                name = topic;
            }
            // String info = strArr[i].split("<b class=\"no \">&nbsp;-&nbsp;")[1].split("</b>&nbsp;</div><div class=\"TagDiv\">")[0];
            String job = strArr[i].split("<td class=\"ts\" title=\"")[1].split("\"><nobr fid=\"")[0];
            //String name = strArr[i].split("<td class=\"ts\" title=\"")[1].split("\"><nobr fid=\"")[0];
            String mark = strArr[i].split("</div></td><td class=\"fg")[1].split("\"><div></div></td></tr></tbody></table></td></tr></tbody></table>")[0];
            if (mark.trim().equals("fs1")){
                mark = "标记";
            } else {
                mark = "未标记";
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
            if(name.contains("_来自猎聘网的候选人")){
                name = name.split("_来自猎聘网的候选人")[0].split(" ")[1];
            }else if(name.contains("的简历(来自拉勾)")){
                name = name.replace("的简历(来自拉勾)","");
            }else if(name.contains("【简历来自站酷】")){
                name = name.split("-")[1].trim().replace("的简历","");
            }
           if(name.contains(",")){
                name = name.replace(",",";");
           }
           if(service.contains(",")){
               service = service.replace(",","");
           }
           if(name.contains("- 来自内推(neitui.Me)")){
               name = name.replace("- 来自内推(neitui.Me)","");
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
        map.put("1", "日期");
        map.put("2", "服务商");
        map.put("3", "姓名");
        map.put("4", "职位");
        map.put("5", "标记星标");


//time:1498496496;service:拉勾网;topic:产品经理:张浩然的简历(来自拉勾)<b class="no">(2) </b>;info:简历管理|首页&nbsp;DataHunter&nbsp;的&nbsp;hr，你好！&nbsp;以下是你在拉勾网发布的“&nbsp;产品经理&nbsp;”职位收到的简历：&nbsp;张浩然:job:产品经理

        String path = "D:/csv/";
        String fileName = "QQ企业邮箱HR邮件";
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);
        String fileName2 = file.getName();
        System.out.println("文件名称：" + fileName2);
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
