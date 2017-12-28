/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * The code was written by Eckart Schlottmann, 2007
 ******************************************************************************/
package com.es.schwalbenschwanzpositionen;

import javafx.beans.property.SimpleDoubleProperty;

public class DoveTail implements Comparable<DoveTail>{
	public SimpleDoubleProperty startPosition= new SimpleDoubleProperty();
	public SimpleDoubleProperty width= new SimpleDoubleProperty();

	
	
	public DoveTail(double  startPosition, double width) {
		super();
		this.startPosition.set(startPosition);
		this.width.set(width);
	}

	public Double getStartPosition() {
		return startPosition.get();
	}

	public void setStartPosition(Double startPosition) {
		System.out.println("setStartPosition called:"+startPosition);
		this.startPosition.set(startPosition);
	}

	public Double getWidth() {
		return width.get();
	}

	public void setWidth(Double width) {
		System.out.println("setWidth called:"+width);
		this.width.set(width);
	}

	public String toString()
	{
		return "DoveTail: start:"+getStartPosition()+" width:"+getWidth();
	}

	@Override
	public int compareTo(DoveTail arg0) {
		return Double.compare(this.startPosition.doubleValue(),arg0.startPosition.doubleValue());
	}
	
}
