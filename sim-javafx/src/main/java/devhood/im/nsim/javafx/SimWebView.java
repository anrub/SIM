package devhood.im.nsim.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SimWebView extends Application {
	private Scene scene;

	@Override
	public void start(Stage stage) {
		// create the scene
		stage.setTitle("SIM in WebView.");
		scene = new Scene(new Browser(), 1050, 700, Color.web("#666970"));
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

class Browser extends Region {

	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();

	public Browser() {
		// apply the styles
		getStyleClass().add("browser");

		webEngine.getLoadWorker().stateProperty()
				.addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov,
							State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							netscape.javascript.JSObject js = (netscape.javascript.JSObject) webEngine
									.executeScript("window");

							js.setMember("app", new Handler());
						}
					}
				});

		// load the web page
		load();

		// add the web view to the scene
		getChildren().add(browser);
	}

	class Handler {
		public void exit() {
			Platform.exit();
		}
	}

	private void load() {
		webEngine.load("http://localhost:8080");
	}

	private Node createSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return 750;
	}

	@Override
	protected double computePrefHeight(double width) {
		return 500;
	}
}