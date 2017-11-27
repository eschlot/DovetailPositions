/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * The code was written by Eckart Schlottmann, 2007
 ******************************************************************************/
package com.es.schwalbenschwanzpositionen;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {

	VBox rootLayout;

	
	@Override
	public void start(Stage primaryStage) {
		try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SchwalbenschwanzPositionenMainView.fxml"));
            rootLayout = (VBox) loader.load();

            SchwalbenschwanzPositionenController controller = (SchwalbenschwanzPositionenController)loader.getController();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            controller.setScene(scene);
            controller.setStage(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}


	


}
