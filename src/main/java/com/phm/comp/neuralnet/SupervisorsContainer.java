
package com.phm.comp.neuralnet;

import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean; 

/**
 *
 * @author phm
 */
public class SupervisorsContainer extends LinkedList<Supervisor> {
    public boolean supervise (String name, Neuron n, Features signal, List<NNResult> result) {
        final AtomicBoolean status = new AtomicBoolean(false);
        this.stream().filter((x) -> (x.getName().contentEquals(name))).forEach((Supervisor x) -> {
            status.set (x.supervise(n, signal, result));
        });
        return status.get ();
    }
}
