package br.com.welike.instagram.helper;

public class JavaScriptUtils {

    public static String getElementByXpath(String xpath) {
        return "document.evaluate(" + xpath +  ", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue";
    }

}
