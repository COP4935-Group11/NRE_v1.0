/*    */
package com.exception;
/*    */
/*    */ import java.text.MessageFormat;
/*    */ import org.xml.sax.SAXException;
/*    */
/*    */
/*    */
/*    */ public class WebElementNotFoundException extends SAXException {
	/*    */ private static final long serialVersionUID = -3905990580447479369L;
	/*    */
	/*    */ public WebElementNotFoundException(long projectID, String webElementGUID) {
		/* 13 */ super(MessageFormat.format("WebElementNotFoundException", projectID, webElementGUID));
		/* 14 */ }
	/*    */
	/*    */ public WebElementNotFoundException(String message) {
		/* 17 */ super(message);
		/* 18 */ }
	/*    */ }