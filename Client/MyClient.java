import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;


public class MyClient implements Runnable{
	
	static Registry registry;
	static String[] serverList;
	static MyInterface myInterface;
	static File f;
	static int chunkSize = 1024*64;
	static RandomAccessFile randomAccessFile;
	static int serverListIndex=0;
	static long numberOfChunks;
	static int numberOfServers;
	static int numberOfThreads;
	
	//static int t=0;
	public static void main(String[] args) throws NotBoundException, IOException {
        

        registry = LocateRegistry.getRegistry(args[1], Integer.parseInt(args[2]));
        RegistryInterface registryInterface= (RegistryInterface) registry.lookup("Reg-Server");

        serverList = registryInterface.GetFileServers();
        for (int i=0; i<serverList.length; i++ ) {
            
            System.out.println(serverList[i]);
        }

        myInterface = (MyInterface)registry.lookup(serverList[0]);
        
        f=new File(args[0]);
        
        if(f.isFile())
        {
            FileInputStream fileInputStream=new FileInputStream(f);
            
            //Create chunks and write into stream
            byte [] chunk=new byte[chunkSize];
            int read;
            int i=0;
            while(((read=fileInputStream.read(chunk,0,chunkSize))!=-1))
            {
               
            	if (read== chunkSize)
                myInterface.FileWrite64K(f.getName(), i, chunk);
            	else
            	{
            		byte [] lastChunk  = new byte[read];
            		for (int j=0; j< lastChunk.length ; j++){
            			lastChunk[j] = chunk[j];
            		}
            		myInterface.FileWrite64K(f.getName(), i, lastChunk);
            	}
            	
            	
            	i++;
            }
            
            fileInputStream.close();
            
            // Reading
            randomAccessFile = new RandomAccessFile("output/"+f.getName(),"rw");
            //File fo=new File("D:\\1assignment\\output\\"+f.getName());
            //FileOutputStream fileOutputStream=new FileOutputStream(fo);
            
            numberOfChunks = myInterface.NumFileChunks(f.getName());
            numberOfThreads = 0;
            numberOfServers = 0;
            for (int j = 0; serverList[j] != null; j++) {
				numberOfServers = j;
			}
            numberOfServers++;
            if (numberOfChunks < numberOfServers)
            	numberOfThreads = (int)numberOfChunks;
            else 
            	numberOfThreads = numberOfServers;

           
           
           List<Thread> threads = new ArrayList<Thread>();
           
           for (int t = 0; t < numberOfThreads; t++) {
        	      Runnable task = new MyClient();
        	      Thread worker = new Thread(task);
        	      worker.setName(String.valueOf(t));
        	      // Start the thread, never call method run() direct
        	      worker.start();
        	      threads.add(worker);
           }
           
           
          
            
        }  
		
}
	@Override
	public void run() {
		try {
			byte [] incomingBytes = new byte[chunkSize];
			int serverIndex= Integer.parseInt(Thread.currentThread().getName());
			myInterface = (MyInterface)registry.lookup(serverList[serverIndex]);
			int currentOffset;
			currentOffset = serverIndex;
			//System.out.println(Thread.currentThread().getName()+"::");
			for (int k=0; k < numberOfChunks; k = k+numberOfServers )
			{
			incomingBytes = myInterface.FileRead64K(f.getName(), currentOffset);
			System.out.println(Thread.currentThread().getName()+":"+currentOffset);
			randomAccessFile.seek(currentOffset*chunkSize);
			currentOffset = currentOffset + numberOfServers;
			randomAccessFile.write(incomingBytes);
			
			}
		} catch (Exception e) {
			
		} 
		
	}
}
