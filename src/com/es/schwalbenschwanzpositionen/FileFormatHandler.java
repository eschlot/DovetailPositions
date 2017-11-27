/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * The code was written by Eckart Schlottmann, 2007
 ******************************************************************************/
package com.es.schwalbenschwanzpositionen;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileFormatHandler
{

	File file;

	public FileFormatHandler()
	{

	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public boolean buildDom(Document doc, SchwalbenschwanzPositionenController controller)
	{

		Element d = doc.createElement("SchwalbenschwanzPositionen");
		doc.appendChild(d);

		d.appendChild(doc.createElement("woodWidth")).appendChild(doc.createTextNode(Double.toString(controller.woodWidthValue.get())));
		d.appendChild(doc.createElement("woodHeight")).appendChild(doc.createTextNode(Double.toString(controller.woodHeightValue.get())));
		d.appendChild(doc.createElement("angle")).appendChild(doc.createTextNode(Double.toString(controller.angleValue.get())));
		d.appendChild(doc.createElement("sawBladeWidth")).appendChild(doc.createTextNode(Double.toString(controller.sawBladeWidthValue.get())));
		Element dts = doc.createElement("doveTails");
		d.appendChild(dts);
		for (DoveTail dto : controller.getDoveTailPositionTable().getItems())
		{
			Element dt = doc.createElement("doveTail");
			dts.appendChild(dt);
			dt.appendChild(doc.createElement("startPosition")).appendChild(doc.createTextNode(Double.toString(dto.getStartPosition())));
			dt.appendChild(doc.createElement("width")).appendChild(doc.createTextNode(Double.toString(dto.getWidth())));
		}
		return true;
	}

	Double getNode(Document doc, double defaultValue, String tagName)
	{
		NodeList nList = doc.getElementsByTagName(tagName);
		if (nList.getLength() != 1)
		{
			System.out.println("File is incomplete: Tag " + tagName + " is missing.");
			return defaultValue;
		}
		Node el = nList.item(0);
		Double value = Double.parseDouble(el.getFirstChild().getTextContent());
		return value;
	}

	public boolean read(SchwalbenschwanzPositionenController controller)
	{
		if (file == null)
			return false;
		if (file.exists() && !file.canRead())
		{
			return false;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
		DocumentBuilder db;
		try
		{
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			doc.getDocumentElement().normalize();
			controller.getDoveTailPositionTable().getItems().clear();

			controller.angleValue.set(getNode(doc, 10.0, "angle"));
			controller.woodHeightValue.set(getNode(doc, 10.0, "woodHeight"));
			controller.woodWidthValue.set(getNode(doc, 10.0, "woodWidth"));
			controller.sawBladeWidthValue.set(getNode(doc, 10.0, "sawBladeWidth"));

			NodeList nList = doc.getElementsByTagName("doveTails");
			if (nList.getLength() != 1)
			{
				System.out.println("File is incorrect: Tag doveTails is not exactly one time in the file.");
			} else
			{

			}
			Element dTs = (Element) nList.item(0);

			NodeList dTNL = dTs.getElementsByTagName("doveTail");
			for (Node dtW : XmlUtil.asList(dTNL))
			{
				if (dtW.getNodeType() == Node.ELEMENT_NODE)
				{
					Element dt = (Element) dtW;
					String startPos = dt.getElementsByTagName("startPosition").item(0).getTextContent();
					String width = dt.getElementsByTagName("width").item(0).getTextContent();

					DoveTail d = new DoveTail(Double.parseDouble(startPos), Double.parseDouble(width));
					controller.getDoveTailPositionTable().getItems().add(d);
				}
			}
			return true;

		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public boolean save(SchwalbenschwanzPositionenController controller)
	{
		if (file == null)
			return false;

		if (file.exists() && !file.canWrite())
		{
			return false;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();

		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document d = db.newDocument();
			if (buildDom(d, controller))
			{

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				try
				{
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					DOMSource source = new DOMSource(d);
					StreamResult result = new StreamResult(file);
					try
					{
						transformer.transform(source, result);
						return true;

					} catch (TransformerException e)
					{
						e.printStackTrace();
					}

				} catch (TransformerConfigurationException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

}
