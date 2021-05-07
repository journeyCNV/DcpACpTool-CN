import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.IOException;
import java.io.RandomAccessFile;

public class UpTestRAR5 {
    /**
     * 针对RAR5的解压缩
     */
    public static void unRAR5(String sourseP,String desP){
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            System.out.println("开始解压******");
            randomAccessFile = new RandomAccessFile(sourseP, "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            int[] in = new int[inArchive.getNumberOfItems()];
            for (int i = 0; i < in.length; i++) {
                in[i] = i;
            }
            inArchive.extract(in, false, new ExtractCallback(inArchive, "366", desP));
            System.out.println("解压完成！");
            randomAccessFile.close();
            inArchive.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
