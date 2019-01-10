package fr.univ.nantes.alma.engine;

/**
* Implements a custom exception for the engine.
* @author Alexis Claveau, Martin Ars, Maud Van Dorssen, Alexis Loret
* @version 0.0.1
*/
public class EngineException extends Exception {
  public EngineException(String message) {
    super(message);
  }
}
