import java.io.File;
import java.io.IOException;

/**
 * Created by root on 2017/6/30.
 */
public class NameFile {
    /** *//**�ļ�������
     * @param path �ļ�Ŀ¼
     * @param oldname  ԭ�����ļ���
     * @param newname ���ļ���
     */
    public static void renameFile(String path,String oldname,String newname){
        if(!oldname.equals(newname)){//�µ��ļ�������ǰ�ļ�����ͬʱ,���б�Ҫ����������
            File oldfile=new File(path+"/"+oldname);
            File newfile=new File(path+"/"+newname);
            if(!oldfile.exists()){
                return;//�������ļ�������
            }
            if(newfile.exists())//���ڸ�Ŀ¼���Ѿ���һ���ļ������ļ�����ͬ��������������
                System.out.println(newname+"�Ѿ����ڣ�");
            else{
                oldfile.renameTo(newfile);
            }
        }else{
            System.out.println("���ļ����;��ļ�����ͬ...");
        }
    }

    public static void main(String[] args) {
        String oldName = "QQ��ҵ����HR�ʼ�.csv";
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
        //���������ȡ�ɹ�����ɾ��ԭ����csv
        if(newFile.length()>32){
            if(oldFile.exists()){
                CSVUtils.deleteFile("D:\\csv",oldName);
            }
            //�����ļ����ָ���Ϊԭ�ļ���
            NameFile.renameFile("D:\\csv",newName,oldName);
        }
    }
}
