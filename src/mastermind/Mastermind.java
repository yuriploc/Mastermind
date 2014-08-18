package mastermind;

import java.io.IOException;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mastermind.common.Chat;
import mastermind.common.Enviavel;
import mastermind.common.Jogada;
import mastermind.controller.GameController;
import cliente.Handler;

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

		String nome = Dialogs.showInputDialog(this.primaryStage, "Digite seu nome:", "ATENÇÃO", "Mensagem do servidor");
		//inicia conexao com server e manda nome do usuario
		Handler.getHandler().enviaNome(nome);

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
