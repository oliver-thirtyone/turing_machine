package turingmachine.model.entities;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Transition {

	private final State currentState;
	private final State nextState;

	private final Map<Tape, TapeTransition> tapeTransitions;

	private String token;

	public Transition(State currentState, State nextState) {
		this.currentState = currentState;
		this.nextState = nextState;

		this.tapeTransitions = new LinkedHashMap<Tape, TapeTransition>();

		this.token = "";
	}

	public String getToken() {
		return this.token;
	}

	public State getCurrentState() {
		return this.currentState;
	}

	public State getNextState() {
		return this.nextState;
	}

	public Collection<TapeTransition> getTapeTransitions() {
		return this.tapeTransitions.values();
	}

	public Boolean addTapeTransition(TapeTransition tapeTransition) {
		if (this.tapeTransitions.containsKey(tapeTransition.getTape())) {
			return Boolean.FALSE;
		}

		this.tapeTransitions.put(tapeTransition.getTape(), tapeTransition);
		this.renewToken();
		return Boolean.TRUE;
	}

	public Boolean removeTapeTransition(TapeTransition tapeTransition) {
		if (!this.tapeTransitions.containsKey(tapeTransition.getTape())) {
			return Boolean.FALSE;
		}

		this.tapeTransitions.remove(tapeTransition.getTape());
		this.renewToken();
		return Boolean.TRUE;
	}

	public static final String getToken(Map<String, Character> tapeReads) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String tapeName : tapeReads.keySet()) {
			stringBuilder.append(tapeName);
			stringBuilder.append(':');
			stringBuilder.append(tapeReads.get(tapeName));

			stringBuilder.append('|');
		}

		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}

		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.currentState == null) ? 0 : this.currentState.hashCode());
		result = 31 * result + ((this.nextState == null) ? 0 : this.nextState.hashCode());
		result = 31 * result + ((this.tapeTransitions == null) ? 0 : this.tapeTransitions.hashCode());
		result = 31 * result + ((this.token == null) ? 0 : this.token.hashCode());
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

		Transition other = (Transition) obj;
		if (this.currentState == null && other.currentState != null) {
			return false;
		}
		else if (!this.currentState.equals(other.currentState)) {
			return false;
		}
		if (this.nextState == null && other.nextState != null) {
			return false;
		}
		else if (!this.nextState.equals(other.nextState)) {
			return false;
		}
		if (this.tapeTransitions == null && other.tapeTransitions != null) {
			return false;
		}
		else if (!this.tapeTransitions.equals(other.tapeTransitions)) {
			return false;
		}
		if (this.token == null && other.token != null) {
			return false;
		}
		else if (!this.token.equals(other.token)) {
			return false;
		}

		return true;
	}

	private synchronized void renewToken() {
		Map<String, Character> tapeReads = new LinkedHashMap<String, Character>();

		for (TapeTransition tapeTransition : this.getTapeTransitions()) {
			tapeReads.put(tapeTransition.getTape().getName(), tapeTransition.getRead());
		}

		this.token = Transition.getToken(tapeReads);
	}
}
