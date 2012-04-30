/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.entities;

public class State {

	private final String name;
	private final Boolean finalState;

	public State(String name) {
		this(name, Boolean.FALSE);
	}

	public State(String name, Boolean finalState) {
		this.name = name;
		this.finalState = finalState;
	}

	public String getName() {
		return this.name;
	}

	public Boolean isFinal() {
		return this.finalState;
	}

}
