package server.mastermind.common;

import java.io.Serializable;

public class Jogada implements Serializable, Enviavel {
	String remetente;
	boolean inicial, encerramento;
	Cores[] linha;
	Cores[] feedback;

	public enum Cores {
		AMARELO,
		VERDE,
		AZUL,
		ROSA,
		ROXO,
		LARANJA,
		BRANCO,
		PRETO,
		VAZIO;

	}
	
	public Jogada() {
		linha = new Cores[] {
				Cores.VAZIO, Cores.VAZIO, Cores.VAZIO, Cores.VAZIO
		};
		feedback = new Cores[] {
				Cores.VAZIO, Cores.VAZIO, Cores.VAZIO, Cores.VAZIO
		};
	}

	public Jogada(String remetente) {
		this.remetente = remetente;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setLinha(Cores[] color) {
		this.linha = color;
	}

	public Jogada.Cores[] getLinha() {
		return linha;
	}

	@Override
	public void setRemetente(String str) {
		remetente = str;
	}
	
	public Cores[] getFeedback() {
		return feedback;
	}

}
