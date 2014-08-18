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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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

	@FXML
	private Button btnLimpar;

	@FXML
	private Text txtCorRepetida;

	@FXML
	private TilePane tileLinhaFeedback;
	
	@FXML
	private VBox vBoxLinhasFeed;

	private int linhaEmJogo = 9;

	private String txtField = "";

	private Jogada jogada;

	private int circuloDaVez;

	private HBox linhaDaVez;
	
	private FlowPane feedDaVez;

	private static GameController gc;

	public GameController() {
		gc = this;
	}

	@FXML
	private void initialize() {
		jogada = new Jogada();
		circuloDaVez = 0;
		linhaDaVez = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo);
		feedDaVez = (FlowPane) vBoxLinhasFeed.getChildren().get(linhaEmJogo);


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

				for (int i = 0; i < tileLinhaFeedback.getChildren().size(); i++) {
					Color color = (Color) ((Circle) tileLinhaFeedback.getChildren().get(i)).getFill();
					jogada.getFeedback()[i] = getCor(color);
				}
				
				Handler.getHandler().enviaMsg(jogada);
				--linhaEmJogo;
				linhaDaVez = (HBox) vBoxLinhasCores.getChildren().get(linhaEmJogo);
				feedDaVez = (FlowPane) vBoxLinhasFeed.getChildren().get(linhaEmJogo);
				jogada = new Jogada();
			}

		});
		btnLimpar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for(Node n : linhaDaVez.getChildren()) {
					Circle c = (Circle) n;
					c.setFill(Color.DARKGRAY);
				}
				for (Node n : tileLinhaFeedback.getChildren()) {
					Circle c = (Circle) n;
					c.setFill(Color.DARKGRAY);
				}
				circuloDaVez = 0;
			}

		});
	}

	@FXML
	public void mouseReleased(MouseEvent me) {
		Color cor = null;
		Circle c = null;

		/* Se circuloDaVez == 0, primeira jogada da linha
		 * Se circuloDaVez entre 1 e 3, verifica se cor já foi usada
		 */ 
		if(circuloDaVez < 4) {
			c = (Circle) linhaDaVez.getChildren().get(circuloDaVez);
			cor = (Color) ((Circle)me.getSource()).getFill();
		}

		if(circuloDaVez == 0) {
			jogada.getLinha()[circuloDaVez++] = getCor(cor);
			c.setFill(cor);
		}
		else if(circuloDaVez < 4 && circuloDaVez > 0) {
			for(int i = 0; i < circuloDaVez; i++) {
				if(getCor(cor).toString().equalsIgnoreCase(jogada.getLinha()[i].toString())) {
					System.out.println("COR JÁ JOGADA");
					//					txtCorRepetida.setVisible(true);
					return;
				}
			}
			jogada.getLinha()[circuloDaVez++] = getCor(cor);
			c.setFill(cor);
		}

	}

	@FXML
	public void mousePressed(MouseEvent me) {
		Circle circle = (Circle) me.getSource();
		if(circle.getFill() == Color.DARKGRAY)
			circle.setFill(Color.WHITE);
		else if(circle.getFill() == Color.WHITE)
			circle.setFill(Color.BLACK);
		else if(circle.getFill() == Color.BLACK)
			circle.setFill(Color.DARKGRAY);
	}

	public static void updateTxtAreaChat(String str) {
		gc.txtAreaChat.appendText(str+"\n");
	}

	public static void updateJogo(Jogada j) {
		for(int i = 0; i < j.getLinha().length; i++) {
			( (Circle) gc.linhaDaVez.getChildren().get(i) ).setFill(getEnglishColor( j.getLinha()[i] ));
		}
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
			return Color.BLUE;
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

}
