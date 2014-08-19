package client.mastermind.controller;

import client.mastermind.common.Chat;
import client.mastermind.common.Jogada;
import client.mastermind.common.Jogada.Cores;
import client.mastermind.connection.Handler;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

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

	@FXML
	private Button btnLimpar;

	@FXML
	private Text txtCorRepetida;

	@FXML
	private VBox vBoxLinhasFeed;
	
	@FXML
	private static HBox linhaMaster;

	private int linhaEmJogo = 9;

	private String txtField = "";

	private Jogada jogada;

	private int circuloDaVez = 0;

	private HBox linhaDaVez;

	private FlowPane feedDaVez;
	
	private static boolean firstUser = false;

	private static GameController gc;

	public GameController() {
		gc = this;
	}

	@FXML
	private void initialize() {
		jogada = new Jogada();
		feedDaVez = (FlowPane) vBoxLinhasFeed.getChildren().get(linhaEmJogo);
		linhaDaVez = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo);

		btnEnviar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				txtField = txtFieldMsgChat.getText();
				Chat c = new Chat(txtField);
				Handler.getHandler().enviaMsg(c);
				txtFieldMsgChat.clear();
				getTxtAreaChat().appendText(txtField + "\n");
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

				feedDaVez = (FlowPane) vBoxLinhasFeed.getChildren().get(linhaEmJogo);
				for (int i = 0; i < feedDaVez.getChildren().size(); i++) {
					Color color = (Color) ((Circle) feedDaVez.getChildren().get(i)).getFill();
					jogada.getFeedback()[i] = getCor(color);
				}

				Handler.getHandler().enviaMsg(jogada);
				--linhaEmJogo;
				if(linhaEmJogo >= 0) {
					linhaDaVez = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo);
					jogada = new Jogada();
				} //else fim de jogo

				circuloDaVez = 0;
			}

		});
		btnLimpar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for(Node n : linhaDaVez.getChildren()) {
					Circle c = (Circle) n;
					c.setFill(Color.DARKGRAY);
				}
				
				for (Node n : feedDaVez.getChildren()) {
					Circle c = (Circle) n;
					c.setFill(Color.DARKGRAY);
				}
				jogada = new Jogada();
				circuloDaVez = 0;
			}
			
		});
		
	}

	@FXML
	public void mouseReleased(MouseEvent me) {

		Circle c = (Circle) linhaDaVez.getChildren().get(circuloDaVez);
		Color cor = (Color) ((Circle)me.getSource()).getFill();

		for(int i = 0; i < jogada.getLinha().length; i++) {
			if(getCor(cor).toString().equalsIgnoreCase(jogada.getLinha()[i].toString())) {
				txtCorRepetida.setVisible(true); // cor já jogada
				me.consume();
				return;
			}
			else if(txtCorRepetida.isVisible()) 
				txtCorRepetida.setVisible(false);
		}
		jogada.getLinha()[circuloDaVez] = getCor(cor);
		c.setFill(cor);
		++circuloDaVez;

		if(circuloDaVez >= 4)
			circuloDaVez = 0;
		me.consume();
		
	}

	@FXML
	public void mousePressed(MouseEvent me) {
		Circle circle = (Circle) me.getSource();
		feedDaVez = (FlowPane) vBoxLinhasFeed.getChildren().get(linhaEmJogo);
		for (Node n : feedDaVez.getChildren()) {
			Circle c = (Circle) n;
			if(c.equals(circle)) {
				if(circle.getFill() == Color.DARKGRAY)
					circle.setFill(Color.WHITE);
				else if(circle.getFill() == Color.WHITE)
					circle.setFill(Color.BLACK);
				else if(circle.getFill() == Color.BLACK)
					circle.setFill(Color.DARKGRAY);
				me.consume();
			}
		}
		me.consume();
		
	}

	public static void updateTxtAreaChat(String str) {
		gc.txtAreaChat.appendText(str+"\n");
	}

	public static void updateJogo(Jogada j) {
		for(int i = 0; i < j.getLinha().length; i++) {
			( (Circle) gc.linhaDaVez.getChildren().get(i) ).setFill(getEnglishColor( j.getLinha()[i] ));
		}
		if(gc.linhaEmJogo < 9) {
			gc.feedDaVez = (FlowPane) gc.vBoxLinhasFeed.getChildren().get(gc.linhaEmJogo+1);
		} else gc.feedDaVez = (FlowPane) gc.vBoxLinhasFeed.getChildren().get(gc.linhaEmJogo);
		for(int i = 0; i < j.getFeedback().length; i++) {
			( (Circle) gc.feedDaVez.getChildren().get(i) ).setFill(getEnglishColor( j.getFeedback()[i] ));
		}
	}

	public TextArea getTxtAreaChat() {
		return txtAreaChat;
	}

	private static Paint getEnglishColor(Cores cor) {
		if(cor == Cores.AMARELO)
			return Color.YELLOW;
		else if(cor == Cores.VERDE)
			return Color.GREEN;
		else if(cor == Cores.AZUL)
			return Color.DODGERBLUE;
		else if(cor == Cores.ROSA)
			return Color.PINK;
		else if(cor == Cores.ROXO)
			return Color.PURPLE;
		else if(cor == Cores.LARANJA)
			return Color.ORANGE;
		else if(cor == Cores.BRANCO)
			return Color.WHITE;
		else if(cor == Cores.PRETO)
			return Color.BLACK;
		else
			return Color.DARKGRAY;
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

	public static void setFirstUser() {
		firstUser = true;
	}

	public static void configMasterGame() {
		//TODO: implementar o turno inicial etc.
	}

}
