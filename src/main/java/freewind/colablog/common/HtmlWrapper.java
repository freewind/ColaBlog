package freewind.colablog.common;

public class HtmlWrapper {

    public String full(String body) {
        return "<style>\n" +
                "body,div,code {\n" +
                "  font-family: 'Hiragino Sans GB';\n" +
                "}\n" +
                "code {" +
                "  border: 1px solid #eee;" +
                "  background: #eee;" +
//                "  padding: 3px 8px;" +
                "}" +
                "pre {" +
                "  font-family: 'Hiragino Sans GB';" +
                "  background: #eee;" +
                "  padding: 5px 10px;" +
                "}\n" +
                "pre code {" +
                "  border: 0px" +
                "  background: #eee;" +
                "  padding: 0px;" +
                "}\n" +
                "blockquote {" +
                "  border-left: 8px solid #eee;" +
                "  background: #f6f6f6;" +
                "  color: #000;" +
                "  margin: 0px;" +
                "  padding: 2px 10px" +
                "}\n" +
                "</style>" +
                "<script>\n" +
                "function resizeText(newFontSize) {\n" +
                "  document.body.style.fontSize = newFontSize + \"px\";\n" +
                "}\n" +
                "function getDocHeight() {\n" +
                "    return Math.max(\n" +
                "        document.body.scrollHeight || 0, \n" +
                "        document.documentElement.scrollHeight || 0,\n" +
                "        document.body.offsetHeight || 0, \n" +
                "        document.documentElement.offsetHeight || 0,\n" +
                "        document.body.clientHeight || 0, \n" +
                "        document.documentElement.clientHeight || 0,\n" +
                "        0" +
                "    );\n" +
                "}\n" +
                "function scrollToPercent(p) {\n" +
                "    window.scrollTo(0,  p * getDocHeight());\n" +
                "}\n" +
                "</script>\n" +
                body;
    }

}
