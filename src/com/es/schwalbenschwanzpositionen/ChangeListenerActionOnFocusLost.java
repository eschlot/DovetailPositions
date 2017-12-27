package com.es.schwalbenschwanzpositionen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class ChangeListenerActionOnFocusLost implements ChangeListener<Boolean>
{

  TextField tv;

  /**
   * @param tv
   */
  public ChangeListenerActionOnFocusLost(TextField tv)
  {
    this.tv = tv;
  }

  @Override
  public void changed(ObservableValue< ? extends Boolean> value, Boolean oldValue, Boolean newValue)
  {
    if (!newValue)
    {
      tv.fireEvent(new ActionEvent());
    }
  }

}
