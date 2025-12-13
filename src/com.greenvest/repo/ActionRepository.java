package com.greenvest.repo;
import java.util.*;
import com.greenvest.model.SustainabilityAction;

public interface ActionRepository {
    void save(SustainabilityAction action);
    List<SustainabilityAction> getPendingActions();
    void update(SustainabilityAction action);
    List<SustainabilityAction> getApprovedActions();
    List<SustainabilityAction> getRejectedActions();

}