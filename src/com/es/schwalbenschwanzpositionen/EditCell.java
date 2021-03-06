package com.es.schwalbenschwanzpositionen;

import javafx.event.Event;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class EditCell<S, T> extends TableCell<S, T> {

	// Text field for editing
	// TODO: allow this to be a plugable control.
	private final TextField textField = new TextField();

	// Converter for converting the text in the text field to the user type, and
	// vice-versa:
	private final StringConverter<T> converter;

	public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}

	public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
			final StringConverter<T> converter) {
		return list -> new EditCell<S, T>(converter);
	}

	public EditCell(StringConverter<T> converter) {
		this.converter = converter;

		itemProperty().addListener((obx, oldItem, newItem) -> {
			if (newItem == null) {
				setText(null);
			} else {
				setText(converter.toString(newItem));
			}
		});
		setGraphic(textField);
		setContentDisplay(ContentDisplay.TEXT_ONLY);

		textField.setOnAction(evt -> {
			try {
				commitEdit(this.converter.fromString(textField.getText()));
			} catch (Exception e) {
				cancelEdit();
			}
		});
		textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
			if (!isNowFocused) {
				try {
					commitEdit(this.converter.fromString(textField.getText()));
				} catch (Exception e) {
					cancelEdit();
				}
			}
		});
		textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				textField.setText(converter.toString(getItem()));
				cancelEdit();
				event.consume();
			} else if (event.getCode() == KeyCode.TAB) {
				TableView<S> tv = getTableView();
				int index = getTableView().getSelectionModel().getSelectedIndex();
				TableColumn<S, T> tc = this.getTableColumn();

				int nextIndex = index;
				int nextColumnIndex = 0;
				int columnIndex = tv.getColumns().indexOf(tc);

				if (!event.isShiftDown()) {
					if (columnIndex >= 0) {
						if (tv.getColumns().size() - 1 > columnIndex) {
							nextColumnIndex = columnIndex + 1;
						} else {
							if (tv.getItems().size() - 1 > index) {
								nextIndex = index + 1;
							} else {
								nextIndex = 0;
							}
						}
					}
				} else {
					if (columnIndex > 0) {
						nextColumnIndex = columnIndex - 1;
					} else {
						nextColumnIndex = tv.getColumns().size() - 1;
						if (index > 0) {
							nextIndex = index - 1;
						} else {
							nextIndex = tv.getItems().size() - 1;
						}
					}
				}

				tv.getSelectionModel().select(nextIndex, tv.getColumns().get(nextColumnIndex));
				tv.requestFocus();
				getTableView().edit(nextIndex, tv.getColumns().get(nextColumnIndex));
			} else if (event.getCode() == KeyCode.UP) {
				getTableView().getSelectionModel().selectAboveCell();

				TableColumn<S, T> tc = this.getTableColumn();
				int index = getTableView().getSelectionModel().getSelectedIndex();
				getTableView().requestFocus();
				getTableView().edit(index, tc);

				event.consume();
			} else if (event.getCode() == KeyCode.DOWN) {
				getTableView().getSelectionModel().selectBelowCell();

				TableColumn<S, T> tc = this.getTableColumn();
				int index = getTableView().getSelectionModel().getSelectedIndex();
				getTableView().requestFocus();
				getTableView().edit(index, tc);

				event.consume();

			}
		});
	}

	/**
	 * Convenience converter that does nothing (converts Strings to themselves and
	 * vice-versa...).
	 */
	public static final StringConverter<String> IDENTITY_CONVERTER = new StringConverter<String>() {

		@Override
		public String toString(String object) {
			return object;
		}

		@Override
		public String fromString(String string) {
			return string;
		}

	};

	/**
	 * Convenience method for creating an EditCell for a String value.
	 * 
	 * @return
	 */
	public static <S> EditCell<S, String> createStringEditCell() {
		return new EditCell<S, String>(IDENTITY_CONVERTER);
	}

	// set the text of the text field and display the graphic
	@Override
	public void startEdit() {
		super.startEdit();
		textField.setText(converter.toString(getItem()));
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.requestFocus();
	}

	// revert to text display
	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	// commits the edit. Update property if possible and revert to text display
	@Override
	public void commitEdit(T item) {

		// This block is necessary to support commit on losing focus, because the
		// baked-in mechanism
		// sets our editing state to false before we can intercept the loss of focus.
		// The default commitEdit(...) method simply bails if we are not editing...
		if (!isEditing() && !item.equals(getItem())) {
			TableView<S> table = getTableView();
			if (table != null) {
				TableColumn<S, T> column = getTableColumn();
				CellEditEvent<S, T> event = new CellEditEvent<>(table,
						new TablePosition<S, T>(table, getIndex(), column), TableColumn.editCommitEvent(), item);
				Event.fireEvent(column, event);
			}
		}

		super.commitEdit(item);

		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

}