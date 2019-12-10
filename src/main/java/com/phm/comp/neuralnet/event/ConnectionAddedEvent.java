
package com.phm.comp.neuralnet.event;
 
import com.phm.comp.neuralnet.connection.Connection;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class ConnectionAddedEvent {
    public final Connection connection;
    
    public ConnectionAddedEvent (Connection con) { 
        connection = Objects.requireNonNull(con);
    }
}
