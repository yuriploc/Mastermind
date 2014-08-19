package client.mastermind.common;

import java.io.Serializable;

public class Chat implements Serializable, Enviavel {
	private String remetente;
	private String mensagem;
	
	public Chat() {
	}
	
	public Chat(String msg) {
		this.mensagem = msg;
	}
	
	public String getRemetente() {
		return this.remetente;
	}
	
	public String getMensagem() {
		return this.mensagem;
	}

	public void setRemetente(String nome) {
		this.remetente = nome;
	}
	
	@Override
	public String toString() {
		return remetente + ": " + mensagem;
	}
}
