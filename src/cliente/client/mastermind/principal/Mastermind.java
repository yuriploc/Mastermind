package client.mastermind.principal;

import java.io.IOException;

import client.mastermind.common.Chat;
import client.mastermind.common.Enviavel;
import client.mastermind.common.Jogada;
import client.mastermind.connection.Handler;
import client.mastermind.controller.GameController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Mastermind extends Application {
	private Stage primaryStage;
	private FXMLLoader loader = null;
	private BorderPane rootLayout;

	public Mastermind() {
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		init(this.primaryStage);
		this.primaryStage.show();
	}

	private void init(Stage primaryStage) {
		primaryStage.setTitle("Mastermind - Yuri");

		//carrega o layout principal
		loader = new FXMLLoader(Mastermind.class.getResource("view/RootLayout2.fxml"));
		try {
			rootLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		primaryStage.setScene(new Scene(rootLayout));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				System.exit(1);
			}
		});

		String nome = Dialogs.showInputDialog(this.primaryStage, "Digite seu nome:", "ATENÇÃO", "Mensagem do servidor");
		//inicia conexao com server, manda nome do usuario
		Handler.getHandler().enviaStr(nome);
		//verifica se é o primeiro usuario a conectar e avisa ao controller
		boolean first = Handler.getHandler().isFirstUser();
		if(first) 
			GameController.setFirstUser();
		
		GameController.configMasterGame();
		//TODO: ajeitar essa porra
		@SuppressWarnings("rawtypes")
		Task task = new Task() {
			@Override
			protected Object call() throws Exception {
				while(true){
					Enviavel e = Handler.getHandler().esperaMsg();
					if(e instanceof Chat) {
						Chat c = (Chat) e;
						GameController.updateTxtAreaChat(c.toString());
					}
					else if(e instanceof Jogada) {
						Jogada j = (Jogada) e;
						GameController.updateJogo(j);
					}
				}
			}
		};
		new Thread(task).start();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public BorderPane getRootLayout() {
		return rootLayout;
	}

}
