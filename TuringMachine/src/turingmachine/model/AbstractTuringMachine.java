package turingmachine.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.Transition;

public abstract class AbstractTuringMachine implements ITuringMachine {

	private String name;
	private Alphabet alphabet;
	private State initialState;
	private Tape inputTape;
	private Tape outputTape;

	private final Map<String, State> states;
	private final Map<String, Tape> tapes;
	private final Map<State, Collection<Transition>> transitions;

	private Boolean initialized;

	public AbstractTuringMachine() {
		this.states = new LinkedHashMap<String, State>();
		this.tapes = new LinkedHashMap<String, Tape>();
		this.transitions = new LinkedHashMap<State, Collection<Transition>>();

		this.setInitialized(Boolean.FALSE);
	}

	public final Boolean initialize(String name, Alphabet alphabet, State initialState, Tape inputTape, Tape outputTape) {
		if (this.isInitialized()) {
			return Boolean.FALSE;
		}

		this.name = name;
		this.alphabet = alphabet;
		this.initialState = initialState;
		this.inputTape = inputTape;
		this.outputTape = outputTape;

		this.setInitialized(Boolean.TRUE);
		this.reset();

		return Boolean.TRUE;
	}

	@Override
	public final Boolean reset() {
		if (!this.isInitialized()) {
			return Boolean.FALSE;
		}

		return this.doReset();
	}

	@Override
	public final Boolean transition() {
		if (!this.isInitialized()) {
			return Boolean.FALSE;
		}

		return this.doTransition();
	}

	protected abstract Boolean doReset();

	protected abstract Boolean doTransition();

	public final Boolean isInitialized() {
		return this.initialized;
	}

	private final void setInitialized(Boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	public final Alphabet getAlphabet() {
		return this.alphabet;
	}

	public final State getInitialState() {
		return this.initialState;
	}

	public final Tape getInputTape() {
		return this.inputTape;
	}

	public final Tape getOutputTape() {
		return this.outputTape;
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

}
