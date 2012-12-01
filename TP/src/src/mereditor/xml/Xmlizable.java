package mereditor.xml;

import org.w3c.dom.Element;

public interface Xmlizable {
	public Element toXml(ParserXML parser) throws Exception;

	public void fromXml(Element elemento, ParserXML parser) throws Exception;
}
