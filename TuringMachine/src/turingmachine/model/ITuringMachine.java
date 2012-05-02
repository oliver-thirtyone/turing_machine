/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model;

import turingmachine.model.entities.State;
import turingmachine.model.entities.Transition;

public interface ITuringMachine {

	public Boolean reset();

	public Boolean transition();

	// /////////////////////////////////////

	public void setInput(String string);

	public String getOutput();

	// /////////////////////////////////////

	public String getName();

	public State getCurrentState();

	public Transition getCurrentTransition();

	public Integer getTransitionCount();

}