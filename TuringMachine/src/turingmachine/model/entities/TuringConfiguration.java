package turingmachine.model.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class TuringConfiguration {

	private final String name;
	private final Alphabet alphabet;

	private final String initialStateName;
	private final String inputTapeName;
	private final String outputTapeName;

	private final Map<String, State> states;
	private final Map<String, Tape> tapes;
	private final Map<State, Collection<Transition>> transitions;

	public TuringConfiguration(String name, Alphabet alphabet, String initialStateName, String inputTapeName, String outputTapeName) {
		this.name = name;
		this.alphabet = alphabet;

		this.initialStateName = initialStateName;
		this.inputTapeName = inputTapeName;
		this.outputTapeName = outputTapeName;

		this.states = new LinkedHashMap<String, State>();
		this.tapes = new LinkedHashMap<String, Tape>();
		this.transitions = new LinkedHashMap<State, Collection<Transition>>();
	}

	public String getName() {
		return this.name;
	}

	public Alphabet getAlphabet() {
		return this.alphabet;
	}

	public Collection<State> getStates() {
		return this.states.values();
	}

	public State getState(String name) {
		return this.states.get(name);
	}

	public Boolean addState(State state) {
		if (this.states.containsKey(state.getName())) {
			return Boolean.FALSE;
		}

		this.states.put(state.getName(), state);
		return Boolean.TRUE;
	}

	public Boolean removeState(State state) {
		if (!this.states.containsKey(state.getName())) {
			return Boolean.FALSE;
		}

		this.states.remove(state.getName());
		return Boolean.TRUE;
	}

	public Collection<Tape> getTapes() {
		return this.tapes.values();
	}

	public Tape getTape(String name) {
		return this.tapes.get(name);
	}

	public Boolean addTape(Tape tape) {
		if (this.tapes.containsKey(tape.getName())) {
			return Boolean.FALSE;
		}

		this.tapes.put(tape.getName(), tape);
		return Boolean.TRUE;
	}

	public Boolean removeTape(Tape tape) {
		if (!this.tapes.containsKey(tape.getName())) {
			return Boolean.FALSE;
		}

		this.tapes.remove(tape.getName());
		return Boolean.TRUE;
	}

	public Collection<Transition> getTransitions(State state) {
		Collection<Transition> transitions = this.transitions.get(state);
		return transitions == null ? new ArrayList<Transition>() : transitions;
	}

	public Boolean addTransition(Transition transition) {
		State state = transition.getCurrentState();

		if (!this.transitions.containsKey(state)) {
			this.transitions.put(state, new ArrayList<Transition>());
		}
		Collection<Transition> transitions = this.transitions.get(state);

		if (transitions.contains(transition)) {
			return Boolean.FALSE;
		}

		transitions.add(transition);
		return Boolean.TRUE;
	}

	public Boolean removeTransition(Transition transition) {
		State state = transition.getCurrentState();

		if (!this.transitions.containsKey(state)) {
			return Boolean.FALSE;
		}

		Collection<Transition> transitions = this.transitions.get(state);
		if (!transitions.contains(transition)) {
			return Boolean.FALSE;
		}

		transitions.remove(transition);

		if (transitions.isEmpty()) {
			this.transitions.remove(state);
		}

		return Boolean.TRUE;
	}

	public State getInitialState() {
		return this.getState(this.initialStateName);
	}

	public Tape getInputTape() {
		return this.getTape(this.inputTapeName);
	}

	public Tape getOutputTape() {
		return this.getTape(this.outputTapeName);
	}

}
