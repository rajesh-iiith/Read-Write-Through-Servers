import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



@SuppressWarnings("serial")
public class RemoteImplementation extends UnicastRemoteObject implements MyInterface{

	protected RemoteImplementation() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int FileWrite64K(String filename, long offset, byte[] data)
			throws IOException, RemoteException {
			int chunkSize = 1024*64;
			//String path = "D:\\Files";
	       File dir=new File(filename);
	       if(dir.exists()){
	           //System.out.println("A folder with name 'new folder' is already exist in the path "+path);
	       }else{
	           dir.mkdir();
	       }
	       
	       if (data.length == chunkSize)
	       {
	    	   File fileNew = new File(filename+"/"+"chunk"+offset);
		       FileOutputStream fop = new FileOutputStream(fileNew);

		       fop.write(data);
		       fop.flush();
		       fop.close();
		       return 1;
	       }
	     //if chunk received is less than 64K then don't write: 
	     //return 0 to signify that file not written successfully
	       else 
	       {
	    	   return 0;
	       }
	       
	}

	@Override
	public long NumFileChunks(String filename) throws IOException,
			RemoteException {
		//String path = "D:\\Files";
	    //File dir=new File(filename);
		File dir=new File(filename);
	    long numberOfChunks=dir.listFiles().length;
	    //long numberOfChunks=3;
		
		return numberOfChunks;
	}

	@Override
	public byte[] FileRead64K(String filename, long offset) throws IOException,
			RemoteException {
		int chunkSize = 1024*64;
        byte [] currentByte=new byte[chunkSize];
		
		//String path = "D:\\Files";
        File dir=new File(filename+"/"+"chunk"+offset);
	    FileInputStream fileInputStream = new FileInputStream(dir);

	    fileInputStream.read(currentByte);
		
		fileInputStream.close();
		
		return currentByte;
	}

	

}
