
package com.phm.core.stage;
 
import com.phm.core.data.Instance;
import com.phm.core.ds.ExtendableDataStream;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class OutcomeStage<DataType extends Instance> extends BaseStage {
    
    private final ExtendableDataStream<DataType> stream;

    public OutcomeStage(ExtendableDataStream<DataType> strm) {
        stream = Objects.requireNonNull(strm);
    }
    
    @Override
    public void process (Object obj) throws StageException {
        stream.add((DataType) obj);
        this.emit(obj);
    }
    
    public ExtendableDataStream<DataType> getDataStream() {
        return stream;
    }
}
