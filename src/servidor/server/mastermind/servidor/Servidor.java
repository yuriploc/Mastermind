package server.mastermind.servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.mastermind.common.Enviavel;

public class Servidor {
	private static List<ClienteInterno> listaClienteInterno;
	private ServerSocket serverSocket;
	private Socket socket;
	private boolean firstUser = false;
	private int countUsers = 0;

	public Servidor() {
		try {
			listaClienteInterno = new ArrayList<ClienteInterno>();
			this.serverSocket = new ServerSocket(2333);
			System.out.println("Servidor escutando na porta 2333...");
		} catch(Exception e) { System.out.println(e); }

		while(true) {
			try {
				this.socket = serverSocket.accept();
				++countUsers;
				if(countUsers == 1)
					firstUser = true;
				listaClienteInterno.add(new ClienteInterno(this.socket, firstUser));
			} catch(Exception e) { System.out.println(e); }
		}
	}

	public static void enviaMsg(Enviavel enviavel ) {
		synchronized (listaClienteInterno) {
			for (ClienteInterno c : listaClienteInterno) {
				if(c.getNome().equalsIgnoreCase(enviavel.getRemetente())) {
					continue;
				}
				else { 
					try{
						c.getObjOutStream().writeObject(enviavel);
						c.getObjOutStream().flush();
					} catch(Exception e) { System.out.println(e); }
				}
			}
		}
	}

	public static void removerCliente(ClienteInterno clienteInterno) {
		synchronized (listaClienteInterno) {
			if(!(listaClienteInterno.isEmpty()))
				listaClienteInterno.remove(clienteInterno);
		}
	}

}
