import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.IOException;
import java.rmi.NotBoundException;

public class MyServer {

	
	public static void main(String[] args) throws RemoteException, AlreadyBoundException, NotBoundException, IOException {
		
		//server is acting as client: registering to registry server
		Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
		RegistryInterface registryInterface= (RegistryInterface) registry.lookup("Reg-Server");
		//Server_<time since epoch in seconds>
		long epoch = System.currentTimeMillis()/1000;
		//int serverNumber = 1 + (int)(Math.random()* 100);

		boolean isMaster = registryInterface.RegisterServer("Server_".concat(String.valueOf(epoch)));
		System.out.println(isMaster);


		//server is acting as server: waiting to get binded by a client
		//Registry registry1 = LocateRegistry.getRegistry();
		RemoteImplementation remoteImplementation = new RemoteImplementation();
		registry.rebind("Server_".concat(String.valueOf(epoch)), remoteImplementation);
		
		System.out.println("server running");
		
		
	}

}
