package mereditor.xml;

import org.w3c.dom.Element;

public interface Xmlizable {
	public Element toXml(ModeloParserXml parser) throws Exception;

	public void fromXml(Element elemento, ModeloParserXml parser) throws Exception;
}
