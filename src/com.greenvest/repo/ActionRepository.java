package com.greenvest.repo;

import java.util.*;
import com.greenvest.model.SustainabilityAction;

/*
 * ActionRepository defines methods
 * for storing and managing sustainability actions.
 */
public interface ActionRepository {

    // Saves a new sustainability action
    void save(SustainabilityAction action);

    // Returns all actions waiting for approval
    List<SustainabilityAction> getPendingActions();

    // Updates the status of an action
    void update(SustainabilityAction action);

    // Returns all approved actions
    List<SustainabilityAction> getApprovedActions();

    // Returns all rejected actions
    List<SustainabilityAction> getRejectedActions();
}
