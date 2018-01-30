/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * The code was written by Eckart Schlottmann, 2007
 ******************************************************************************/
package com.es.schwalbenschwanzpositionen;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Properties;

import com.es.schwalbenschwanzpositionen.CutPosition.CutType;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class SchwalbenschwanzPositionenController
{

	SimpleDoubleProperty woodWidthValue = new SimpleDoubleProperty();

	SimpleDoubleProperty woodHeightValue = new SimpleDoubleProperty();

	SimpleDoubleProperty sawBladeWidthValue = new SimpleDoubleProperty();

	SimpleDoubleProperty angleValue = new SimpleDoubleProperty();

	FileFormatHandler fileFormatHandler;

	final FileChooser fileChooser = new FileChooser();

	Properties properties = new Properties();

	private Scene scene;

	private Stage stage;

	public SchwalbenschwanzPositionenController()
	{
		super();
		fileFormatHandler = new FileFormatHandler();
	}

	protected void saveProperties()
	{
		try
		{
			String filename = System.getProperty("user.home") + File.separator + ".SchwalbenschwanzPositionen.prop";
			properties.store(new FileWriter(new File(filename)), "SchwalbenschwanzPositionen Properties file");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	protected void initialize()
	{

		try
		{
			properties.load(new FileReader(new File(System.getProperty("user.home") + File.separator + ".SchwalbenschwanzPositionen.prop")));
		} catch (IOException e)
		{
		}
		Comparator<Number> numberComparator = new Comparator<Number>()
		{

			@Override
			public int compare(Number arg0, Number arg1)
			{
				return Double.compare(arg0.doubleValue(), arg1.doubleValue());
			}
		};
		getDoveTailPositionTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		{
			TableColumn<DoveTail, Number> startPositionTableColumn = new TableColumn<DoveTail, Number>("Start Position");
			getDoveTailPositionTable().getColumns().add(startPositionTableColumn);
			startPositionTableColumn.setMinWidth(100);
			startPositionTableColumn.setCellFactory(EditCell.forTableColumn(new NumberStringConverter()));

			startPositionTableColumn.setCellValueFactory(new Callback<CellDataFeatures<DoveTail, Number>, ObservableValue<Number>>()
			{
				@Override
				public ObservableValue<Number> call(CellDataFeatures<DoveTail, Number> p)
				{
					return p.getValue().startPosition;

				}
			});
			startPositionTableColumn.setSortable(true);

			TableColumn<DoveTail, Number> widthColumn = new TableColumn<DoveTail, Number>("Width");
			getDoveTailPositionTable().getColumns().add(widthColumn);
			widthColumn.setMinWidth(100);
			// widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
			widthColumn.setCellFactory(EditCell.forTableColumn(new NumberStringConverter()));

			widthColumn.setCellValueFactory(new Callback<CellDataFeatures<DoveTail, Number>, ObservableValue<Number>>()
			{
				@Override
				public ObservableValue<Number> call(CellDataFeatures<DoveTail, Number> p)
				{
					return p.getValue().width;

				}
			});
			widthColumn.setComparator(numberComparator);
			getDoveTailPositionTable().setEditable(true);
		}
		{
			TableColumn<CutPosition, Number> startPositionTableColumn = new TableColumn<CutPosition, Number>("Start Position");
			cutPositionTable.getColumns().add(startPositionTableColumn);
			startPositionTableColumn.setMinWidth(100);
			startPositionTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
			startPositionTableColumn.setCellValueFactory(new Callback<CellDataFeatures<CutPosition, Number>, ObservableValue<Number>>()
			{
				@Override
				public ObservableValue<Number> call(CellDataFeatures<CutPosition, Number> p)
				{
					return p.getValue().position;

				}
			});
			TableColumn<CutPosition, Number> widthTableColumn = new TableColumn<CutPosition, Number>("Width");
			cutPositionTable.getColumns().add(widthTableColumn);
			widthTableColumn.setMinWidth(100);
			widthTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
			widthTableColumn.setCellValueFactory(new Callback<CellDataFeatures<CutPosition, Number>, ObservableValue<Number>>()
			{
				@Override
				public ObservableValue<Number> call(CellDataFeatures<CutPosition, Number> p)
				{
					return p.getValue().width;

				}
			});
			TableColumn<CutPosition, String> cutTypeTableColumn = new TableColumn<CutPosition, String>("Cut Type");
			cutPositionTable.getColumns().add(cutTypeTableColumn);
			cutTypeTableColumn.setMinWidth(200);
			cutTypeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<CutPosition, String>, ObservableValue<String>>()
			{
				@Override
				public ObservableValue<String> call(CellDataFeatures<CutPosition, String> p)
				{
					SimpleStringProperty sp = new SimpleStringProperty();
					sp.set(p.getValue().cutType.get().toString());
					return sp;
				}
			});
		}
		woodWidthValue.set(150.0);
		woodWidth.setText(Double.toString(woodWidthValue.get()));
		woodWidth.focusedProperty().addListener(new ChangeListenerActionOnFocusLost(woodWidth));

		woodHeightValue.set(19.0);
		woodHeight.setText(Double.toString(woodHeightValue.get()));
		woodHeight.focusedProperty().addListener(new ChangeListenerActionOnFocusLost(woodHeight));

		sawBladeWidthValue.set(2.3);
		sawBladeWidth.setText(Double.toString(sawBladeWidthValue.get()));
		sawBladeWidth.focusedProperty().addListener(new ChangeListenerActionOnFocusLost(sawBladeWidth));

		angleValue.set(10.0);
		angle.setText(Double.toString(angleValue.get()));
		angle.focusedProperty().addListener(new ChangeListenerActionOnFocusLost(angle));

		LinkedList<String> extensionList = new LinkedList<>();
		extensionList.add("xml");
		extensionList.add("dovetail");

		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("xml", extensionList));

	}

	@FXML
	private TextField woodHeight;

	@FXML
	private TextField angle;

	@FXML
	private TextField woodWidth;

	@FXML
	private TextField sawBladeWidth;

	@FXML
	private Button addDovetail;

	@FXML
	private Button removeDovetail;

	@FXML
	private Button calculate;

	@FXML
	private TableView<DoveTail> doveTailPositionTable;

	@FXML
	private TableView<CutPosition> cutPositionTable;

	@FXML
	private Canvas dovetailViewFront;

	@FXML
	private Canvas dovetailViewBack;

	@FXML
	void addDovetailClicked(ActionEvent event)
	{
		DoveTail d = new DoveTail(0.0, 1.0);
		getDoveTailPositionTable().getItems().add(d);

	}

	@FXML
	void calculateClicked(ActionEvent event)
	{
		cutPositionTable.getItems().clear();
		double radAngle = Math.toRadians(angleValue.get());
		// System.out.println("angle:" + angleValue.get() + " angelRad:" + radAngle);
		double offsetInWoodDirection = Math.tan(radAngle) * woodHeightValue.get();
		// System.out.println("offsetInWoodDirection:" + offsetInWoodDirection);
		double offsetInJigDirection = offsetInWoodDirection * Math.cos(radAngle);
		// System.out.println("offsetInJigDirection:" + offsetInJigDirection);

		// Ensure the correct sorting by the start position is done.
		getDoveTailPositionTable().getSortOrder().clear();
		TableColumn<DoveTail, ?> tc = getDoveTailPositionTable().getColumns().get(0);
		getDoveTailPositionTable().getSortOrder().add(tc);

		double pinBoardStartPos = 0;
		boolean first = true;
		for (DoveTail d : getDoveTailPositionTable().getItems())
		{
			{
				double woodCutPosStart = d.getStartPosition();
				double effectiveCutPositionStart = woodCutPosStart * Math.cos(radAngle) + offsetInJigDirection;
				// System.out.println("woodCutPosStart:" + woodCutPosStart + "
				// effectiveCutPositionStart:" + effectiveCutPositionStart);

				double woodCutPosEnd = d.getStartPosition() + d.getWidth();
				double effectiveCutPositionEnd = woodCutPosEnd * Math.cos(radAngle) + offsetInJigDirection;

				CutPosition cutPos = new CutPosition(CutType.PINPIECE_STRAIGHT_LEFT, effectiveCutPositionStart, effectiveCutPositionEnd - effectiveCutPositionStart);
				cutPositionTable.getItems().add(cutPos);
			}
			{
				double woodCutPosStart = woodWidthValue.get() - d.getStartPosition();
				// System.out.println("\nwoodCutPosStart:" + woodCutPosStart);
				double effectiveCutPositionStart = woodCutPosStart * Math.cos(radAngle) + offsetInJigDirection;

				double cutWidth = woodHeightValue.get() * Math.sin(2 * radAngle) * Math.cos(2 * radAngle);
				// System.out.println("effectiveCutPositionStart:" + (effectiveCutPositionStart
				// - cutWidth));

				// System.out.println("cutWidth:" + cutWidth);

				CutPosition cutPos = new CutPosition(CutType.PINPIECE_STRAIGHT_RIGHT, effectiveCutPositionStart - cutWidth, cutWidth);
				cutPositionTable.getItems().add(cutPos);
			}
			{
				double woodCutPosStart = d.getStartPosition();
				// System.out.println("\n\nCutType.SCHWALBENSTUECK_BEVEL_LEFT: woodCutPosStart:"
				// + woodCutPosStart);

				double bevelWidthAtTopOfCut = Math.tan(radAngle) * woodHeightValue.get();
				System.out.println("bevelWidthAtTopOfCut:" + bevelWidthAtTopOfCut);

				double effectiveCutPositionEnd = woodCutPosStart - bevelWidthAtTopOfCut;

				// System.out.println("effectiveCutPositionEnd:" + effectiveCutPositionEnd);

				double effectiveCutWidthOfBlade = sawBladeWidthValue.get() / Math.cos(radAngle);
				double effectiveCutStartPosition = effectiveCutPositionEnd - Math.max(effectiveCutWidthOfBlade, bevelWidthAtTopOfCut);
				double overallCutWidth = effectiveCutPositionEnd - effectiveCutStartPosition;

				// System.out.println("effectiveCutWidthOfBlade:" + effectiveCutWidthOfBlade);
				// System.out.println("effectiveCutStartPosition:" + effectiveCutStartPosition);
				// System.out.println("overallCutWidth:" + overallCutWidth);

				CutPosition cutPos = new CutPosition(CutType.DOVETAILPIECE_BEVEL_LEFT, effectiveCutStartPosition, overallCutWidth);
				cutPositionTable.getItems().add(cutPos);
			}
			{
				double woodCutPosStart = woodWidthValue.get() - (d.getStartPosition() + d.getWidth());
				// System.out.println("\n\nCutType.SCHWALBENSTUECK_BEVEL_RIGHT:
				// woodCutPosStart:" + woodCutPosStart);

				double bevelWidthAtTopOfCut = Math.tan(radAngle) * woodHeightValue.get();
				double effectiveCutPositionEnd = woodCutPosStart - bevelWidthAtTopOfCut;

				// System.out.println("effectiveCutPositionEnd:" + effectiveCutPositionEnd);

				double effectiveCutWidthOfBlade = sawBladeWidthValue.get() / Math.cos(radAngle);
				double effectiveCutStartPosition = effectiveCutPositionEnd - Math.max(effectiveCutWidthOfBlade, bevelWidthAtTopOfCut);
				double overallCutWidth = effectiveCutPositionEnd - effectiveCutStartPosition;

				// System.out.println("effectiveCutWidthOfBlade:" + effectiveCutWidthOfBlade);
				// System.out.println("effectiveCutStartPosition:" + effectiveCutStartPosition);
				// System.out.println("overallCutWidth:" + overallCutWidth);

				CutPosition cutPos = new CutPosition(CutType.DOVETAILPIECE_BEVEL_RIGHT, effectiveCutStartPosition, overallCutWidth);
				cutPositionTable.getItems().add(cutPos);
			}
			{
				double cutPosRelief = sawBladeWidthValue.get() /2;
				double woodCutPosStart = d.getStartPosition();
				// System.out.println("\n\nCutType.SCHWALBENSTUECK_STRAIGHT: woodCutPosStart:" +
				// woodCutPosStart);

				double bevelWidthAtTopOfCut = Math.tan(radAngle) * woodHeightValue.get();
				double effectiveCutPositionEnd = woodCutPosStart - bevelWidthAtTopOfCut;
				CutPosition cutPos;
				if (first)
				{
					cutPos = new CutPosition(CutType.DOVETAILPIECE_STRAIGHT, pinBoardStartPos, effectiveCutPositionEnd - pinBoardStartPos - cutPosRelief);
				} else
				{
					cutPos = new CutPosition(CutType.DOVETAILPIECE_STRAIGHT, pinBoardStartPos + cutPosRelief, effectiveCutPositionEnd - pinBoardStartPos - 2 * cutPosRelief);
				}
				cutPositionTable.getItems().add(cutPos);
				pinBoardStartPos = woodCutPosStart + d.getWidth() + bevelWidthAtTopOfCut;
			}
			first = false;
		}
		{
			double cutPosRelief = sawBladeWidthValue.get() / 2;
			double effectiveCutPositionEnd = woodWidthValue.get();
			CutPosition cutPos = new CutPosition(CutType.DOVETAILPIECE_STRAIGHT, pinBoardStartPos + cutPosRelief, effectiveCutPositionEnd - pinBoardStartPos - cutPosRelief);
			cutPositionTable.getItems().add(cutPos);
		}

		// for (CutPosition cut : cutPositionTable.getItems())
		// {
		// //System.out.println(cut);
		// }
		drawCanvasDoveTailViewFront();
		drawCanvasDoveTailViewBack();
	}

	private void drawCanvasDoveTailViewFront()
	{
		GraphicsContext gc = dovetailViewFront.getGraphicsContext2D();
		double height = dovetailViewFront.getHeight();
		double width = dovetailViewFront.getWidth();
		gc.setTransform(new Affine(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0));

		gc.clearRect(0, 0, width, height);

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		Font f = Font.font("Calibri Light", FontWeight.EXTRA_LIGHT, 12);

		gc.setFont(f);
		gc.setTextBaseline(VPos.TOP);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.strokeText("0 = Left side of sawblade", 10, 0);

		gc.setTextAlign(TextAlignment.CENTER);

		gc.strokeText("First side: Blue is \"" + CutPosition.CutType.DOVETAILPIECE_BEVEL_LEFT + "\"; Dark orange is \"" + CutType.DOVETAILPIECE_STRAIGHT + "\"", width / 2, 0);

		gc.setTextAlign(TextAlignment.RIGHT);
		gc.strokeText(Double.toString(woodWidthValue.get()), width - 10, 0);

		double transfomScaleFactor = width / woodWidthValue.get();
		Affine a = new Affine(transfomScaleFactor, 0.0, 0.0, 0.0, 0.0, transfomScaleFactor, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		gc.setTransform(a);

		gc.setLineWidth(1);

		double radAngle = Math.toRadians(angleValue.get());

		double startX = 0;
		double startY = 6;

		gc.strokeLine(0, 0, startX, startY);
		for (DoveTail d : doveTailPositionTable.getItems())
		{
			double nextX = d.startPosition.get();
			gc.strokeLine(startX, startY, nextX, startY);

			double corneradditionLength = woodHeightValue.get() * Math.tan(radAngle);
			gc.strokeLine(nextX, startY, nextX - corneradditionLength, startY + woodHeightValue.get());
			gc.strokeLine(nextX - corneradditionLength, startY + woodHeightValue.get(), nextX + d.getWidth() + corneradditionLength, startY + woodHeightValue.get());
			gc.strokeLine(nextX + d.getWidth() + corneradditionLength, startY + woodHeightValue.get(), nextX + d.getWidth(), startY);
			startX = d.getStartPosition() + d.getWidth();

		}
		gc.strokeLine(startX, startY, woodWidthValue.get(), startY);
		gc.strokeLine(woodWidthValue.get(), startY, woodWidthValue.get(), 0.0);
		/* Draw the cuts */

		for (CutPosition cut : cutPositionTable.getItems())
		{
			if (cut.getCutType() == CutType.DOVETAILPIECE_STRAIGHT)
			{
				gc.setLineWidth(0);
				gc.setStroke(Color.DARKORANGE);
				gc.setFill(Color.DARKORANGE);
				gc.fillRect(cut.getPosition(), startY, cut.getWidth().get(), woodHeightValue.get());
			}
		}
		for (CutPosition cut : cutPositionTable.getItems())
		{
			if (cut.getCutType() == CutType.DOVETAILPIECE_BEVEL_LEFT)
			{
				double corneradditionLength = woodHeightValue.get() * Math.tan(radAngle);
				gc.setLineWidth(0);
				gc.setStroke(Color.BLUE);
				gc.setFill(Color.LIGHTBLUE);
				gc.beginPath();
				gc.moveTo(cut.getPosition(), woodHeightValue.get() + startY);
				gc.lineTo(cut.getPosition() + corneradditionLength, startY);
				gc.lineTo(cut.getPosition() + cut.getWidth().get() + corneradditionLength, startY);
				gc.lineTo(cut.getPosition() + cut.getWidth().get(), woodHeightValue.get() + startY);
				gc.lineTo(cut.getPosition(), woodHeightValue.get() + startY);
				gc.stroke();
				gc.fill();

			}
		}
	}

	private void drawCanvasDoveTailViewBack()
	{
		GraphicsContext gc = dovetailViewBack.getGraphicsContext2D();
		double height = dovetailViewFront.getHeight();
		double width = dovetailViewFront.getWidth();
		gc.setTransform(new Affine(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0));

		gc.clearRect(0, 0, width, height);
		gc.clearRect(0, 0, width, height);

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		Font f = Font.font("Calibri Light", FontWeight.EXTRA_LIGHT, 12);

		gc.setFont(f);
		gc.setTextBaseline(VPos.TOP);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.strokeText("0", 10, 0);

		gc.setTextAlign(TextAlignment.CENTER);

		gc.strokeText("Turn around: Blue is " + CutPosition.CutType.DOVETAILPIECE_BEVEL_RIGHT, width / 2, 0);

		gc.setTextAlign(TextAlignment.RIGHT);
		gc.strokeText(Double.toString(woodWidthValue.get()), width - 10, 0);

		double transfomScaleFactor = width / woodWidthValue.get();
		/*
		 * scale it to the size of the screen, invert the direction of the represenation
		 * and move (translate) it by the width
		 */
		Affine a = new Affine(-transfomScaleFactor, 0.0, 0.0, width, 0.0, transfomScaleFactor, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		gc.setTransform(a);

		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);

		gc.setLineWidth(1);

		double radAngle = Math.toRadians(angleValue.get());

		double startX = 0;
		double startY = 6;

		gc.strokeLine(0, 0, startX, startY);
		for (DoveTail d : doveTailPositionTable.getItems())
		{
			double nextX = d.startPosition.get();
			gc.strokeLine(startX, startY, nextX, startY);

			double corneradditionLength = woodHeightValue.get() * Math.tan(radAngle);
			gc.strokeLine(nextX, startY, nextX - corneradditionLength, startY + woodHeightValue.get());
			gc.strokeLine(nextX - corneradditionLength, startY + woodHeightValue.get(), nextX + d.getWidth() + corneradditionLength, startY + woodHeightValue.get());
			gc.strokeLine(nextX + d.getWidth() + corneradditionLength, startY + woodHeightValue.get(), nextX + d.getWidth(), startY);
			startX = d.getStartPosition() + d.getWidth();

		}
		gc.strokeLine(startX, startY, woodWidthValue.get(), startY);
		gc.strokeLine(woodWidthValue.get(), startY, woodWidthValue.get(), 0.0);
		/* Draw the cuts */

		/* do not invert the direction of the cuts and do not translate the cuts */
		a = new Affine(transfomScaleFactor, 0.0, 0.0, 0.0, 0.0, transfomScaleFactor, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		gc.setTransform(a);

		for (CutPosition cut : cutPositionTable.getItems())
		{
			if (cut.getCutType() == CutType.DOVETAILPIECE_BEVEL_RIGHT)
			{
				double corneradditionLength = woodHeightValue.get() * Math.tan(radAngle);
				gc.setLineWidth(0);
				gc.setStroke(Color.BLUE);
				gc.setFill(Color.LIGHTBLUE);
				gc.beginPath();
				gc.moveTo(cut.getPosition(), woodHeightValue.get() + startY);
				gc.lineTo(cut.getPosition() + corneradditionLength, startY);
				gc.lineTo(cut.getPosition() + cut.getWidth().get() + corneradditionLength, startY);
				gc.lineTo(cut.getPosition() + cut.getWidth().get(), woodHeightValue.get() + startY);
				gc.lineTo(cut.getPosition(), woodHeightValue.get() + startY);
				gc.stroke();
				gc.fill();
			}
		}

	}

	@FXML
	void removeDovetailClicked(ActionEvent event)
	{
		getDoveTailPositionTable().getItems().remove(getDoveTailPositionTable().getSelectionModel().getSelectedItem());
	}

	// @FXML
	// void sortDoveTailPositions(Event event)
	// {
	// System.out.println(event);
	// }

	@FXML
	void woodHeightEntered(ActionEvent event)
	{
		try
		{
			Double d = Double.valueOf(woodHeight.getText());
			if (d != null)
			{
				woodHeightValue.set(d);

			}
		} catch (NumberFormatException ex)
		{

		}
		woodHeight.setText(Double.toString(woodHeightValue.get()));
	}

	@FXML
	void woodWidthEntered(ActionEvent event)
	{
		try
		{
			Double d = Double.valueOf(woodWidth.getText());
			if (d != null)
			{
				woodWidthValue.set(d);

			}
		} catch (NumberFormatException ex)
		{

		}
		woodWidth.setText(Double.toString(woodWidthValue.get()));
	}

	@FXML
	void sawBladeWidthEntered(ActionEvent event)
	{
		try
		{
			Double d = Double.valueOf(sawBladeWidth.getText());
			if (d != null)
			{
				sawBladeWidthValue.set(d);

			}
		} catch (NumberFormatException ex)
		{

		}
		sawBladeWidth.setText(Double.toString(sawBladeWidthValue.get()));
	}

	@FXML
	void angleEntered(ActionEvent event)
	{
		try
		{
			Double d = Double.valueOf(angle.getText());
			if (d != null)
			{
				angleValue.set(d);

			}
		} catch (NumberFormatException ex)
		{

		}
		angle.setText(Double.toString(angleValue.get()));
	}

	public TableView<DoveTail> getDoveTailPositionTable()
	{
		return doveTailPositionTable;
	}

	@FXML
	void save(ActionEvent event)
	{
		String lastFileName = (String) properties.get("LastFile");
		if (lastFileName != null)
		{
			File file = new File(lastFileName);
			fileFormatHandler.setFile(file);
			fileFormatHandler.save(this);
		}
	}

	@FXML
	void saveAs(ActionEvent event)
	{
		fileChooser.setTitle("Save File as ... ");
		File file = fileChooser.showSaveDialog(stage);

		if (file != null)
		{
			fileFormatHandler.setFile(file);
			fileFormatHandler.save(this);
			properties.put("LastFile", file.getAbsolutePath());
			saveProperties();
		}

	}

	void updateGuiFromValues()
	{
		angle.setText(Double.toString(angleValue.get()));
		woodHeight.setText(Double.toString(woodHeightValue.get()));
		woodWidth.setText(Double.toString(woodWidthValue.get()));
		sawBladeWidth.setText(Double.toString(sawBladeWidthValue.get()));
	}

	@FXML
	void open(ActionEvent event)
	{

		fileChooser.setTitle("Open File");
		File file = fileChooser.showOpenDialog(stage);

		if (file != null)
		{
			fileFormatHandler.setFile(file);
			fileFormatHandler.read(this);
			updateGuiFromValues();
			properties.put("LastFile", file.getAbsolutePath());
			saveProperties();

		}

	}

	@FXML
	void openRecent(ActionEvent event)
	{
		String lastFileName = (String) properties.get("LastFile");
		if (lastFileName != null)
		{
			File file = new File(lastFileName);
			fileFormatHandler.setFile(file);
			fileFormatHandler.read(this);
			updateGuiFromValues();
		}
	}

	@FXML
	void quit(ActionEvent event)
	{
		System.exit(0);

	}

	static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	@FXML
	void about(ActionEvent event)
	{
		try
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About DoveTailposition");
			alert.setHeaderText("This software was written by Eckart Schlottmann \u00a9 2017");
			alert.setContentText("Please read the License below.");
			alert.setGraphic(null);
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("com/es/schwalbenschwanzpositionen/view/2.htm").getFile());
			String s = readFile(file.getAbsolutePath(), Charset.forName("UTF-8"));

			Label label = new Label("License: ");

			WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.loadContent(s);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(browser, 0, 1);

			alert.getDialogPane().setExpandableContent(expContent);

			alert.showAndWait();
		} catch (Exception e)
		{

		}
	}

	public void setScene(Scene scene)
	{
		this.scene = scene;
	}

	public void setStage(Stage primaryStage)
	{
		stage = primaryStage;
		stage.setTitle("Dovetail positions");
	}

}
