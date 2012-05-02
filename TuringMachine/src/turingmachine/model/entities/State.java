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

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.finalState == null) ? 0 : this.finalState.hashCode());
		result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		State other = (State) obj;
		if (this.finalState == null && other.finalState != null) {
			return false;
		}
		else if (!this.finalState.equals(other.finalState)) {
			return false;
		}
		if (this.name == null && other.name != null) {
			return false;
		}
		else if (!this.name.equals(other.name)) {
			return false;
		}

		return true;
	}

}
