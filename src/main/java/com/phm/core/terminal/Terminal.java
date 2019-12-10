
package com.phm.core.terminal;

import com.google.common.eventbus.EventBus;
import com.phm.annotations.ImplementationDetails;
import com.phm.core.ParametersContainer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.pipeline.Pipeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ImplementationDetails (
    className = "Terminal",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "The basic class is a base platform "
                + "to define a data terminal."
)
public abstract class Terminal {
    public final String name;
    public final Logger log;
    public final ParametersContainer parameters = new ParametersContainer();
    
    protected final AtomicReference<SourceStatus> serviceStatus;
    protected final EventBus eventBus;
    protected Pipeline pipelineObj;
    
    public Terminal (String sn, Logger lg) {
        name = Objects.requireNonNull (sn);
        log = Objects.requireNonNull(lg);
        
        serviceStatus = new AtomicReference<>(SourceStatus.Terminated);
        eventBus = new EventBus(sn + " Status Listeners");
    }
    public Terminal (String sn) {
        this (sn, LogManager.getLogger(sn));
    }

    private void changeStatus (SourceStatus ss) {
        if (serviceStatus.get() != ss) {
            serviceStatus.set(ss);
            eventBus.post(ss);
            log.info("Status is changed to " + ss.name());
        }
    }
    public SourceStatus getStatus () {
        return serviceStatus.get();
    }
    
    public void registerStatusListener (Object obj) {
        eventBus.register(obj);
        log.info("A status listener is registered");
    }
    public void unregisterStatusListener (Object obj) {
        eventBus.unregister(obj);
        log.info("A status listener is unregistered");
    }
    protected void raise (Object obj) {
        eventBus.post(obj);
        log.info("The event is posted --> " + obj);
    }
    public String getName () {
        return name;
    }
    
    public void start () {
        if (initialize()) {
            changeStatus(SourceStatus.Initialized);
            if (activate()) {
                changeStatus(SourceStatus.Activated);
            }
        }
    }
    public void stop () {
        if (deactivate()) {
            changeStatus(SourceStatus.Deactivated);
            if (terminate()) {
                changeStatus(SourceStatus.Terminated);
            }
        }
    }
    
    protected abstract boolean initialize ();
    protected abstract boolean activate ();
    protected abstract boolean deactivate ();
    protected abstract boolean terminate ();
    
    @Override
    public boolean equals (Object obj) {
        return obj != null &&
               obj instanceof Terminal &&
               obj.hashCode() == hashCode();
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(getName());
        return hash;
    }
    
    public static class ParameterChanged {
        public String key;
        public Object value;
        
        public ParameterChanged (String k, Object v) {
            key = k;
            value = v;
        }
        @Override
        public String toString () {
            return key + " : " + value.toString();
        }
    }
}
