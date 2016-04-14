import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



@SuppressWarnings("serial")
public class RegistryImplementation extends UnicastRemoteObject implements RegistryInterface{

	static int serverCount=0;
	String serverList[] = new String[10];
	protected RegistryImplementation() throws RemoteException {
		super();
	}


public boolean RegisterServer(String name) throws IOException, RemoteException
{	
	serverCount++;
	serverList[serverCount-1] = name;
	if (serverCount==1)
		return true;
	else
		return false;
	
}

public String[] GetFileServers() throws IOException, RemoteException
{
	return serverList;
}

	

}
