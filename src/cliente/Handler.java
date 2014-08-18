package cliente;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mastermind.common.Chat;
import mastermind.common.Enviavel;
import mastermind.common.Jogada;

public final class Handler {
	private Socket socket;
	private ObjectOutputStream objOutStream;
	private ObjectInputStream objInStream;

	//SINGLETON
	private static Handler handler;
	public static Handler getHandler() {
		if(handler == null)
			handler = new Handler();
		return handler;
	}
	
	private Handler() {
		try {
			socket = new Socket("localhost", 2333);
			objOutStream = new ObjectOutputStream(socket.getOutputStream());
			objInStream = new ObjectInputStream(socket.getInputStream());
		} catch(Exception e) { System.out.println(e); }

	}

	public void enviaNome(String msg) {
		try {
			objOutStream.writeObject(msg);
			objOutStream.flush();
		} catch(Exception e) { System.out.println(e+" --> pau no enviamsg do handler"); }
	}
	
	public void enviaMsg(Enviavel env) {
		try {
			objOutStream.writeObject(env);
			objOutStream.flush();
		} catch(Exception e) { System.out.println(e+" --> pau no enviamsg do handler"); }
	}

	public Enviavel esperaMsg(){
		try {
			Object o = objInStream.readObject();
			if(o instanceof Chat) {
				Chat c = (Chat) o;
				return c;
			}
			else if(o instanceof Jogada) {
				Jogada j = (Jogada) o;
				return j;
			}
		} catch(Exception e) { System.out.println(e); }
		return null;
	}
	
}