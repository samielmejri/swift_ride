/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.swiftride.interfaces;

import edu.swiftride.entities.MoyenTransport;
import edu.swiftride.entities.Station;
import java.util.List;

/**
 *
 * @author Ines
 */
public interface InterfaceCRUD<M> {
     void ajouterMoyenTransport(MoyenTransport m);
    void supprimerMoyenTransport(MoyenTransport m);
    boolean modifierMoyenTransport(MoyenTransport m);
    List<MoyenTransport> afficherMoyenTransport();
   
  
}
