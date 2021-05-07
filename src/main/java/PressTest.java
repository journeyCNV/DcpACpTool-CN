import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PressTest {
    private static final int BUFF_SIZE = 2 * 1024;

    /**
     * 压缩文件为Zip文件
     */
    public static void cpZIP(String sourseP,String desP){
        ZipOutputStream zipOutputStream = null;
        try {
            FileOutputStream fileOutputP = new FileOutputStream(new File(desP));
            System.out.println("开始压缩文件******");
            zipOutputStream = new ZipOutputStream(fileOutputP);
            File sourF = new File(sourseP);
            //System.out.println(sourF.getName());
            compress(sourF,zipOutputStream,sourF.getName());
            System.out.println("压缩完成！");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(zipOutputStream!=null){
                try {
                    zipOutputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File sP,ZipOutputStream zos,String name){
        byte[] buf = new byte[BUFF_SIZE];
        try {
            if (sP.isFile()) {
                //向Zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(name));
                //copy文件到zip输出流中
                int len;
                FileInputStream in = new FileInputStream(sP);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sP.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    //需要对空文件夹进行处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                } else {
                    for (File file : listFiles) {
                        compress(file, zos, name + "/" + file.getName());
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
