import java.io.File;
import java.io.IOException;

/**
 * Created by root on 2017/6/30.
 */
public class NameFile {
    /** *//**文件重命名
     * @param path 文件目录
     * @param oldname  原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path,String oldname,String newname){
        if(!oldname.equals(newname)){//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile=new File(path+"/"+oldname);
            File newfile=new File(path+"/"+newname);
            if(!oldfile.exists()){
                return;//重命名文件不存在
            }
            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname+"已经存在！");
            else{
                oldfile.renameTo(newfile);
            }
        }else{
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    public static void main(String[] args) {
        String oldName = "QQ企业邮箱HR邮件.csv";
        String url = "https://exmail.qq.com/login";
        String newName = null;
        try {
            newName = HttpUtils.parserHtml(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        // NameFile.renameFile("D:\\csv",oldName,newName);
        File newFile = new File("D:\\csv",newName);
        File oldFile = new File("D:\\csv",oldName);
        //如果数据爬取成功，则删掉原来的csv
        if(newFile.length()>32){
            if(oldFile.exists()){
                CSVUtils.deleteFile("D:\\csv",oldName);
            }
            //将新文件名字更改为原文件名
            NameFile.renameFile("D:\\csv",newName,oldName);
        }
    }
}
