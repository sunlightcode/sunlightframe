/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * HTML脚本工具
 */
public class HTMLUtil {
	/**
	 * 解析html，提取html中的文本内容
	 * 
	 * @param content
	 *            html内容
	 * @return 得到的文本内容
	 * @throws ParserException
	 */
	public static String parseHtml(String content) throws ParserException {
		Parser myParser;
		NodeList nodeList = null;
		StringBuilder result = new StringBuilder();
		myParser = Parser.createParser(content, "utf-8");

		NodeFilter textFilter = new NodeClassFilter(TextNode.class);
		// NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
		// NodeFilter metaFilter = new NodeClassFilter(MetaTag.class);

		OrFilter lastFilter = new OrFilter();
		lastFilter.setPredicates(new NodeFilter[] { textFilter });

		nodeList = myParser.parse(lastFilter);

		Node[] nodes = nodeList.toNodeArray();
		String line = "";

		for (int i = 0; i < nodes.length; i++) {
			Node anode = (Node) nodes[i];

			if (anode instanceof TextNode) {
				TextNode textnode = (TextNode) anode;
				line = textnode.toPlainTextString();
				if (line.startsWith("<!--")) {
					continue;
				}
			} else if (anode instanceof LinkTag) {
				LinkTag linknode = (LinkTag) anode;
				line = linknode.getLink();
			}

			if (isTrimEmpty(line))
				continue;

			result.append(line + "\n");
		}
		return result.toString();
	}

	private static boolean isTrimEmpty(String astr) {
		if ((null == astr) || (astr.length() == 0)) {
			return true;
		}
		if (isBlank(astr.trim())) {
			return true;
		}
		return false;
	}

	private static boolean isBlank(String astr) {
		if ((null == astr) || (astr.length() == 0)) {
			return true;
		} else {
			return false;
		}
	}
}