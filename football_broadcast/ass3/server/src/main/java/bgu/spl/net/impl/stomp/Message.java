package bgu.spl.net.impl.stomp;

import java.util.Map;
import java.util.Iterator;

public class Message {
  private StompCommand _command;
  private Map<String, String> _headers;
  private String _body;
  protected Message( StompCommand c, Map<String, String> h, String b ) {
    _command = c;
    _headers = h;
    _body = b;
  }
  public Map<String, String> headers() { return _headers; }
  public String body() { return _body; }
  public StompCommand command() { return _command; }
  public String toString() {

      StringBuffer message = new StringBuffer(_command.toString());
      message.append( "\n" );

      if (_headers != null) {
        for (Iterator keys = _headers.keySet().iterator(); keys.hasNext(); ) {
            String key = (String)keys.next();
            String value = _headers.get(key);
            message.append( key );
            message.append( ":" );
            message.append( value );
            message.append( "\n" );
        }
      }
      message.append( "\n" );

      if (_body != null) message.append( _body );

      return message.toString();
    }
}



