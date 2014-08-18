package mastermind.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import mastermind.common.Chat;
import mastermind.common.Jogada;
import mastermind.common.Jogada.Cores;
import cliente.Handler;

public class GameController {

	@FXML // fx:id="btnEnviar"
	private Button btnEnviar;

	@FXML
	private Button btnJogar;

	@FXML
	private VBox vBoxLinhasCores;

	@FXML // fx:id="txtFieldMsgChat"
	private TextField txtFieldMsgChat;

	@FXML // fx:id="txtAreaChat"
	private TextArea txtAreaChat;

	private int linhaEmJogo = 9;

	private String txtField = "";

	private Jogada jogada;
	
	private int circuloDaVez;

	private static GameController gc;

	public GameController() {
		gc = this;
	}

	public static void updateTxtAreaChat(String str) {
		gc.txtAreaChat.appendText(str+"\n");
	}

	@FXML
	private void initialize() {
		jogada = new Jogada();
		circuloDaVez = 0;

		btnEnviar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				txtField = txtFieldMsgChat.getText();
				System.out.println("msg: " + txtField + "(btnENVIAR)");
			}

		});
		txtFieldMsgChat.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					txtField = txtFieldMsgChat.getText();
					Chat c = new Chat(txtField);
					Handler.getHandler().enviaMsg(c);
					txtFieldMsgChat.clear();
					getTxtAreaChat().appendText(txtField + "\n");
				}
			}

		});
		btnJogar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				HBox hBox = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo--);
				Handler.getHandler().enviaMsg(jogada);

			}

		});
	}

	private Cores getCor(Color cor) {
		if(cor == Color.YELLOW)
			return Cores.AMARELO;
		else if(cor == Color.GREEN)
			return Cores.VERDE;
		else if(cor == Color.DODGERBLUE)
			return Cores.AZUL;
		else if(cor == Color.PINK)
			return Cores.ROSA;
		else if(cor == Color.PURPLE)
			return Cores.ROXO;
		else if(cor == Color.ORANGE)
			return Cores.LARANJA;
		else if(cor == Color.WHITE)
			return Cores.BRANCO;
		else if(cor == Color.BLACK)
			return Cores.PRETO;
		else
			return Cores.VAZIO;
	}

	@FXML
	public void mouseReleased(MouseEvent me) {

		HBox hBox = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo);
		System.out.println("mouse released");
		
		if(circuloDaVez < 4 && circuloDaVez > 0) {
			Circle c = (Circle) hBox.getChildren().get(circuloDaVez);
			Color cor = (Color) ((Circle)me.getSource()).getFill();
			for(int i = 0; i < jogada.getLinha().length; i++) {
				if(getCor(cor).toString().equalsIgnoreCase(jogada.getLinha()[i].toString())){
					System.out.println("COR JÁ EXISTE");
					System.out.println(getCor(cor).toString() + " igual a " + jogada.getLinha()[i].toString());
				}
				else {
					jogada.getLinha()[circuloDaVez++] = getCor(cor);
					c.setFill(cor);
				}
			}
		}
		else circuloDaVez = 0;
		
	}



	public TextArea getTxtAreaChat() {
		return txtAreaChat;
	}

}
