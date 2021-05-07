import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

public class UpTest {
    /**
     * 解压RAR
     * 仅限RAR 5 版本以下 后面的不支持了
     */
    public static void unRAR(String sourceP,String desP){
        File sP = new File(sourceP);
        File dP = new File(desP);
        Archive archive = null;
        FileOutputStream fileOutputStream = null;
        System.out.println("开始解压******");
        try {
            FileInputStream inputStreamsp = new FileInputStream(sP);
            archive = new Archive(inputStreamsp);
            FileHeader fh = archive.nextFileHeader();
            int count = 0;
            File desFileName = null;
            while (fh!=null){
                String compressFN = fh.getFileNameString().trim();
                desFileName = new File(dP.getAbsolutePath()+"/"+compressFN);
                if(fh.isDirectory()){
                    if(!desFileName.exists()){
                        desFileName.mkdir();
                    }
                    fh = archive.nextFileHeader();
                    continue;
                }
                if (!desFileName.getParentFile().exists()){
                    desFileName.getParentFile().mkdir();
                }
                fileOutputStream = new FileOutputStream(desFileName);
                archive.extractFile(fh,fileOutputStream);
                fileOutputStream.close();
                fileOutputStream = null;
                fh = archive.nextFileHeader();
            }
            archive.close();
            archive = null;
            System.out.println("解压完成");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RarException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                    fileOutputStream = null;
                }catch (Exception e){

                }
            }
            if(archive!=null){
                try {
                    archive.close();
                    archive = null;
                }catch (Exception e){

                }
            }
        }
    }

    public static void unZIP(String sourseP,String desP){
        System.out.println("开始解压******");
        String descFileNames = desP;
        if(!descFileNames.endsWith(File.separator)){
            descFileNames=descFileNames+File.separator;
        }
        try {
            /**
             * 中文windows默认是gbk编码
             * 但是ZipFile默认识别的是UTF-8
             */
            ZipFile zipFile = new ZipFile(sourseP,"gbk");
            //ZipFile zipFile = new ZipFile(sourseP); //如果是utf-8编码的,这个中文不会乱码，反而gbk会，，但也就是文件名而已
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4098];
            int readByte=0;
            //@SuppressWarnings("rawtypes")
            Enumeration<?> enums = zipFile.getEntries();
            while (enums.hasMoreElements()){
                entry = (ZipEntry) enums.nextElement();
                entryName = entry.getName();
                //System.out.println(entryName);
                descFileDir = descFileNames+entryName;
                if(entry.isDirectory()){
                    /**
                     * boolean mkdir() :  创建此抽象路径名指定的目录。
                     * boolean mkdirs() :  创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。
                     */
                    new File(descFileDir).mkdirs();//这里用mkdir会出问题
                    continue;
                }else {
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                OutputStream os = new FileOutputStream(file);
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte=is.read(buf))!=-1){
                    os.write(buf,0,readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            System.out.println("解压完成！");
        }catch (Exception e){
            System.out.println("文件解压失败");
            e.printStackTrace();
        }
    }

}
