package server.mastermind.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.mastermind.common.Chat;
import server.mastermind.common.Jogada;

public class ClienteInterno extends Thread {
	private Socket socket = null;
	private String nome = "";
	private ObjectInputStream objInStream = null;
	private ObjectOutputStream objOutStream = null;
	private boolean firstUser = false;

	public ClienteInterno(Socket s, boolean firstUser) {
		try {
			this.socket = s;
			this.objInStream = new ObjectInputStream(socket.getInputStream());
			this.objOutStream = new ObjectOutputStream(socket.getOutputStream());
			this.firstUser = firstUser;
			this.start();
		} catch (Exception e) { System.out.println(e); }
	}
	
	private void esperaStr() {
		try {
			Object o = objInStream.readObject();
			if(o instanceof String) {
				String str = o.toString();
				this.nome = str;
			}
		} catch (Exception e) { System.out.println(e+" --> pau no espera nome do servidor"); }
	}

	public void run() {
		//isAlive ou true?
		this.esperaStr();
		if(firstUser) {
			try {
				objOutStream.writeBoolean(true);
				objOutStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(this.isAlive()) {
			try {
				Object o = objInStream.readObject();
				if(o instanceof Chat) {
					Chat c = (Chat) o;
					c.setRemetente(this.nome);
					Servidor.enviaMsg(c);
				}
				else if(o instanceof Jogada) {
					Jogada j = (Jogada) o;
					j.setRemetente(this.nome);
					Servidor.enviaMsg(j);
				}
			} catch(Exception e) {
				//TODO: melhorar essa putaria
				Servidor.removerCliente(this);
				try {
					this.socket.close();
				} catch (Exception ex) { System.out.println("erro ao fechar os streams" + ex); }
				System.out.println(e); 
			}
		}
	}

	public String getNome() {
		return this.nome;
	}
	
	public ObjectOutputStream getObjOutStream() {
		return this.objOutStream;
	}
	
	public ObjectInputStream getObjInputStream() {
		return this.objInStream;
	}

}
