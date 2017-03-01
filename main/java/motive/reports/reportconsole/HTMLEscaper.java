package motive.reports.reportconsole;

public class HTMLEscaper {
    public String escape(String in) {
        StringBuffer out = new StringBuffer();
        char[] chars = in.toCharArray();
        for ( int i = 0 ; i < chars.length ; ++i ) {
            switch (chars[i]){
                case '"':
                    out.append("&quot;");
                    break;
                case '<':
                    out.append("&lt;");
                    break;
                case '>':
                    out.append("&gt;");
                    break;
                case '&':
                    out.append("&amp;");
                    break;
                default:
                    out.append(chars[i]);
                    break;
            }
        }
        return out.toString();
    }
}
