/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * XML解析工具。
 * 
 */

public final class XMLUtil {
	private static Hashtable<String, Templates> xslTemplates = new Hashtable<String, Templates>();

	/**
	 * 给一个节点添加子节点。
	 * 
	 * @param targetNode
	 *            目标节点。
	 * @param name
	 *            节点名。
	 * @param value
	 *            节点值。
	 */
	public static void appendChild(Node targetNode, String name, String value) {
		Document document = targetNode.getOwnerDocument();
		Node node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		targetNode.appendChild(node);
	}

	/**
	 * 将一个document对象输出成字符串.
	 * 
	 * @param document
	 *            目标document对象。
	 * @param dtdPublicId
	 *            dtd公共id。
	 * @param dtdSystemId
	 *            The dtd系统id。
	 * @param encoding
	 *            输出字符串编码。
	 * @return 转换后的字符串。
	 * @throws XMLDocumentException
	 */
	public static String getContent(Document document, String dtdPublicId, String dtdSystemId, String encoding)
			throws Exception {
		StringWriter outWriter = new StringWriter();
		StreamResult result = new StreamResult(outWriter);
		outputContent(document, dtdPublicId, dtdSystemId, encoding, result);
		return outWriter.toString();
	}

	/**
	 * 将一个document对象输出成字符串.
	 * 
	 * @param document
	 *            目标document对象。
	 * @param dtdPublicId
	 *            dtd公共id。
	 * @param dtdSystemId
	 *            The dtd系统id。
	 * @param encoding
	 *            输出字符串编码。
	 * @param result
	 *            结果流。
	 * @throws XMLDocumentException
	 */
	public static void outputContent(Document document, String dtdPublicId, String dtdSystemId, String encoding,
			StreamResult result) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		if (dtdSystemId != null) {
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdSystemId);
		}
		if (dtdPublicId != null) {
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, dtdPublicId);
		}
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(document), result);
	}

	/**
	 * 将一个字符串解析成Document对象。
	 * 
	 * @param sourceString
	 *            目标字符串。
	 * @param dtdPublicId
	 *            dtd公共id。
	 * @param validating
	 *            是否做有效性检测。
	 * @return 得到的Document对象。
	 * @throws Exception
	 */
	public static Document parseString(String sourceString, String dtdPublicId, boolean validating) throws Exception {
		InputSource input = new InputSource(new StringReader(sourceString));
		return parseInputSource(input, dtdPublicId, validating);
	}

	/**
	 * 将一个输入源解析成Document对象。
	 * 
	 * @param source
	 *            目标输入源。
	 * @param dtdPublicId
	 *            dtd公共id。
	 * @param validating
	 *            是否做有效性检测。
	 * @return 得到的Document对象。
	 * @throws Exception
	 */
	public static Document parseInputSource(InputSource source, String dtdPublicId, boolean validating)
			throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		if (dtdPublicId != null) {
			return builder.parse(source.getByteStream(), dtdPublicId);
		} else {
			return builder.parse(source);
		}
	}

	/**
	 * 创建一个Document对象。
	 * 
	 * @return 创建得到的Document对象。
	 * @throws Exception
	 */
	public static Document createDocument() throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setCoalescing(true);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.newDocument();
		return document;
	}

	/**
	 * 解析一个xml文件得到Document对象。
	 * 
	 * @param xmlFile
	 *            xml源文件。
	 * @return 解析得到Document对象。
	 * @throws XMLDocumentException
	 */
	public static Document createDocument(File xmlFile) throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setValidating(false);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(xmlFile);
		return document;
	}

	/**
	 * 检测一个Document对象的dtd公共id是否是指定的dtd公共id。
	 * 
	 * @param document
	 *            目标Document对象。
	 * @param dtdPublicId
	 *            指定的dtd公共id
	 * @return 检测结果。
	 */
	public static boolean checkDocumentType(Document document, String dtdPublicId) {
		DocumentType documentType = document.getDoctype();
		if (documentType != null) {
			String publicId = documentType.getPublicId();
			return publicId != null && publicId.equals(dtdPublicId);
		}
		return true;
	}

	/**
	 * 将xml和xslt合成得到html字符串。
	 * 
	 * @param xml
	 *            目标xml字符串。
	 * @param xslFilePath
	 *            xslt文件路径。
	 * @return 合成得到html字符串。
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static String combineXSL(String xml, String xslFilePath)
			throws TransformerConfigurationException, TransformerException {
		StreamSource xmlSrc = new StreamSource(new StringReader(xml));
		StreamSource xslSrc = new StreamSource(xslFilePath);
		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformer = factory.newTransformer(xslSrc);
		StringWriter resultWriter = new StringWriter();
		StreamResult result = new StreamResult(resultWriter);
		transformer.transform(xmlSrc, result);
		return resultWriter.getBuffer().toString();
	}

	/**
	 * 将xml和xslt合成得到html字符串,同时利用缓存机制提高运行的效率。
	 * 
	 * @param xml
	 *            目标xml字符串。
	 * @param xslFilePath
	 *            xslt文件路径。
	 * @return 合成得到html字符串。
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static String combineXSLWithCache(String xml, String xslFilePath)
			throws TransformerConfigurationException, TransformerException {
		StreamSource xmlSrc = new StreamSource(new StringReader(xml));
		Templates cachedXSLT = xslTemplates.get(xslFilePath);
		if (cachedXSLT == null) {
			StreamSource xslSrc = new StreamSource(xslFilePath);
			TransformerFactory factory = TransformerFactory.newInstance();
			cachedXSLT = factory.newTemplates(xslSrc);
			xslTemplates.put(xslFilePath, cachedXSLT);
		}
		Transformer transformer = cachedXSLT.newTransformer();
		StringWriter resultWriter = new StringWriter();
		StreamResult result = new StreamResult(resultWriter);
		transformer.transform(xmlSrc, result);
		return resultWriter.getBuffer().toString();
	}
}
