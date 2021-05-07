import net.sf.sevenzipjbinding.*;

import java.io.*;

public class ExtractCallback implements IArchiveExtractCallback {

    private int index;
    private String packageName;
    private IInArchive inArchive;
    private String cnDir;

    public ExtractCallback(IInArchive inArchive,String packageName,String cnDir){
        this.inArchive = inArchive;
        this.packageName = packageName;
        this.cnDir = cnDir;
    }

    @Override
    public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
        this.index = index;
        final String path = (String) inArchive.getProperty(index,PropID.PATH);
        final boolean isFolder = (boolean) inArchive.getProperty(index,PropID.IS_FOLDER);
        return new ISequentialOutStream() {
            @Override
            public int write(byte[] bytes) throws SevenZipException {
                try {
                    if(!isFolder){
                        File file = new File(cnDir+path);
                        save2File(file,bytes);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return bytes.length;
            }
        };
    }

    public static boolean save2File(File file,byte[] msg){
        OutputStream outputStream = null;
        try {
            File parent = file.getParentFile();
            boolean bool;
            if((!parent.exists())&&(!parent.mkdirs())){
                return false;
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(msg);
            outputStream.flush();
            return true;
        }catch (FileNotFoundException e){
            return false;
        }catch (IOException e){
            File parent;
            return false;
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                }catch (IOException e){

                }
            }
        }
    }

    @Override
    public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {

    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
        String path = (String) inArchive.getProperty(index,PropID.PATH);
        boolean isFolder = (boolean) inArchive.getProperty(index,PropID.IS_FOLDER);

    }

    @Override
    public void setTotal(long l) throws SevenZipException {

    }

    @Override
    public void setCompleted(long l) throws SevenZipException {

    }
}
