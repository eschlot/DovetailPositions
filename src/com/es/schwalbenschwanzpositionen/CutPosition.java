/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * The code was written by Eckart Schlottmann, 2007
 ******************************************************************************/
package com.es.schwalbenschwanzpositionen;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CutPosition
{

	public enum CutType
	{
		ZINKENSTUECK_STRAIGHT_LEFT, ZINKENSTUECK_STRAIGHT_RIGHT, 
		SCHWALBENSTUECK_BEVEL_LEFT, // Schnitt von links vor dem Umdrehen des Schwalbenstücks
		SCHWALBENSTUECK_BEVEL_RIGHT, // Schnitt von rechts ncah dem umdrehen am Schwalbenstueck;
		SCHWALBENSTUECK_STRAIGHT, //Gerade Schnitte am schwalbenstueck
	}

	public SimpleObjectProperty<CutType> cutType = new SimpleObjectProperty<CutType>();

	public SimpleDoubleProperty position = new SimpleDoubleProperty();
	public SimpleDoubleProperty width = new SimpleDoubleProperty();

	public CutPosition(CutType cutType, double position, double width)
	{
		super();
		this.cutType.set(cutType);
		this.position.set(position);
		this.width.set(width);
	}

	public CutType getCutType()
	{
		return cutType.get();
	}

	public void setCutType(CutType cutType)
	{
		this.cutType.set(cutType);
	}

	public double getPosition()
	{
		return position.get();
	}

	public void setPosition(double position)
	{
		this.position.set(position);
	}

	public SimpleDoubleProperty getWidth()
	{
		return width;
	}

	public void setWidth(SimpleDoubleProperty width)
	{
		this.width = width;
	}

	public String toString()
	{
		return "pos:" + position.get() + " width:" + width.get() + " type:" + cutType.get().name();
	}

}
