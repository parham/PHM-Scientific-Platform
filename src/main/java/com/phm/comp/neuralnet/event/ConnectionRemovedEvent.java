
package com.phm.comp.neuralnet.event;
 
import com.phm.comp.neuralnet.connection.Connection;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class ConnectionRemovedEvent {
    public final Connection connection;
    
    public ConnectionRemovedEvent (Connection con) { 
        connection = Objects.requireNonNull(con);
    }
}
