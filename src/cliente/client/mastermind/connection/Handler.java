package client.mastermind.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.mastermind.common.Chat;
import client.mastermind.common.Enviavel;
import client.mastermind.common.Jogada;

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

	public void enviaStr(String msg) {
		try {
			objOutStream.writeObject(msg);
			objOutStream.flush();
		} catch(Exception e) { System.out.println(e+" --> pau no enviaNome do handler"); }
	}

	public void enviaMsg(Enviavel env) {
		try {
			objOutStream.writeObject(env);
			objOutStream.flush();
		} catch(Exception e) { System.out.println(e+" --> pau no enviaMsg do handler"); }
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

	public boolean isFirstUser() {
		boolean is = false;
		try {
			is = objInStream.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return is;
	}

}