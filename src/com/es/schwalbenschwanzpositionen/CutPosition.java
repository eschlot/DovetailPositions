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
		PINPIECE_STRAIGHT_LEFT, PINPIECE_STRAIGHT_RIGHT, 
		DOVETAILPIECE_BEVEL_LEFT, // Schnitt von links vor dem Umdrehen des Schwalbenstuecks
		DOVETAILPIECE_BEVEL_RIGHT, // Schnitt von rechts nach dem Umdrehen des Schwalbenstuecks;
		DOVETAILPIECE_STRAIGHT; //Gerade Schnitte am Schwalbenstueck
		
		
		public String toString()
		{
			switch (this)
			{
				case DOVETAILPIECE_BEVEL_LEFT: return "Dovetail piece Bevel left";
				case DOVETAILPIECE_BEVEL_RIGHT: return "Dovetail piece Bevel right";
				case DOVETAILPIECE_STRAIGHT: return "Dovetail piece straight cuts";
				case PINPIECE_STRAIGHT_LEFT: return "Pin piece straight cut from left";
				case PINPIECE_STRAIGHT_RIGHT: return "Pin piece straight cut from right";
			}
			return "?";
			
		}
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
