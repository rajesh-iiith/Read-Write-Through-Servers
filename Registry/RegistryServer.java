import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RegistryServer {

	
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		RegistryImplementation registryImplementation = new RegistryImplementation();
		Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
		registry.bind("Reg-Server", registryImplementation);
		
		System.out.println("Registry server running");
	}

}
