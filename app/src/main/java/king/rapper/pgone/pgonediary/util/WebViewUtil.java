package king.rapper.pgone.pgonediary.util;

/**
 * @ Create_time: 2018/7/14 on 9:54.
 * @ description：
 * @ author: radish  email: 15703379121@163.com
 * <p>
 * <!DOCTYPE html>
 * <html>
 * <head>
 * <meta name="viewport" content="user-scalable=no">
 * <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 * <link rel="stylesheet" type="text/css" href="normalize.css">
 * <link rel="stylesheet" type="text/css" href="style.css">
 * </head>
 * <body>
 * <div id="editor" contenteditable="true"></div>
 * <script type="text/javascript" src="rich_editor.js"></script>
 * </body>
 * </html>
 */
public class WebViewUtil {
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        String heads = "<head>\n" +
                " <meta name=\"viewport\" content=\"user-scalable=no\">\n" +
                " <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                " <link rel=\"stylesheet\" type=\"text/css\" href=\"normalize.css\">\n" +
                " <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n" +
                " </head>";
        String bodyDiv = "\n" +
                " <div id=\"editor\" contenteditable=\"true\"></div>\n" +
                " <script type=\"text/javascript\" src=\"rich_editor.js\"></script>";
        return "<html>" + heads + "<body>" + bodyDiv + bodyHTML + "</body></html>";
    }
}
