package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Teclado {
	private static BufferedReader br;
	
	private static void inicializar() {
		if(br == null) {
			br = new BufferedReader(new InputStreamReader(System.in));
		}
	}
	
public static String lerLinha() {
	
	inicializar();
	
	try {
		return br.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "leitura do teclado deu pau";
	
}

}
