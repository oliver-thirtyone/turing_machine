package turingmachine.parser;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import turingmachine.model.TuringMachine;
import turingmachine.model.entities.Alphabet;
import turingmachine.model.entities.State;
import turingmachine.model.entities.Tape;
import turingmachine.model.entities.TapeMotion;
import turingmachine.model.entities.TapeTransition;
import turingmachine.model.entities.Transition;

public class TuringXMLParser {

	public static final String SCHEMA_LOCATION = "data/xsd/turingMachine.xsd";

	private DocumentBuilder documentBuilder;
	private Validator validator;

	public TuringXMLParser() {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			this.documentBuilder = documentBuilderFactory.newDocumentBuilder();

			InputStream schema = this.getClass().getClassLoader().getResourceAsStream(TuringXMLParser.SCHEMA_LOCATION);
			Source source = new StreamSource(schema);

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			this.validator = schemaFactory.newSchema(source).newValidator();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public TuringMachine parse(InputStream inputStream) throws TuringXMLParserException {
		Document document;

		// Parse document
		try {
			document = this.documentBuilder.parse(inputStream);
			document.getDocumentElement().normalize();
		}
		catch (Exception exception) {
			throw new TuringXMLParserException("Could not parse the document!", exception);
		}

		// Validate document
		try {
			DOMSource domSource = new DOMSource(document);
			this.validator.validate(domSource);
		}
		catch (Exception exception) {
			throw new TuringXMLParserException("The document is not valid!", exception);
		}

		// Prepare TuringMachine
		TuringMachine turingMachine = new TuringMachine();
		String turingMachineName = null;
		Alphabet alphabet = new Alphabet();
		State initialState = null;
		Tape inputTape = null;
		Tape outputTape = null;

		// Parse document elements
		NodeList rootNodes = document.getChildNodes();
		for (int x = 0; x < rootNodes.getLength(); x++) {
			Node rootNode = rootNodes.item(x);

			if (rootNode.getNodeName().equals(TuringXML.TURINGMACHINE.toString())) {
				NodeList nodes = rootNode.getChildNodes();

				for (int y = 0; y < nodes.getLength(); y++) {
					Node node = nodes.item(y);

					if (node.getNodeName().equals(TuringXML.STATES.toString())) {
						this.parseStates(turingMachine, initialState, node);
					}
					else if (node.getNodeName().equals(TuringXML.TAPES.toString())) {
						this.parseTapes(turingMachine, inputTape, outputTape, node);
					}
					else if (node.getNodeName().equals(TuringXML.ALPHABET.toString())) {
						this.parseAlphabet(alphabet, node);
					}
					else if (node.getNodeName().equals(TuringXML.TRANSITIONS.toString())) {
						this.parseTransitions(turingMachine, alphabet, node);
					}
				}
			}
		}

		// Initialize TuringMachine
		turingMachine.initialize(turingMachineName, alphabet, initialState, inputTape, outputTape);

		return turingMachine;
	}

	private void parseStates(TuringMachine turingMachine, State initialState, Node statesNode) throws TuringXMLParserException {
		NodeList stateNodes = statesNode.getChildNodes();
		for (int x = 0; x < stateNodes.getLength(); x++) {
			Node stateNode = stateNodes.item(x);

			if (stateNode.getNodeName().equals(TuringXML.STATE.toString())) {
				Element stateElement = (Element) stateNode;

				State state = null;
				String name = stateElement.getAttribute(TuringXML.STATE_NAME.toString());

				if (stateElement.hasAttribute(TuringXML.STATE_FINAL.toString())) {
					String finalState = stateElement.getAttribute(TuringXML.STATE_FINAL.toString());
					state = new State(name, Boolean.valueOf(finalState));
				}
				else {
					state = new State(name);
				}

				turingMachine.addState(state);
			}
		}

		Element statesElement = (Element) statesNode;

		String initialStateName = statesElement.getAttribute(TuringXML.STATES_INITIAL.toString());
		initialState = turingMachine.getState(initialStateName);
		if (initialState == null) {
			throw new TuringXMLParserException("Cannot set the initial state to '" + initialStateName + "' because this state does not exist!");
		}
	}

	private void parseTapes(TuringMachine turingMachine, Tape inputTape, Tape outputTape, Node tapesNode) throws TuringXMLParserException {
		NodeList tapeNodes = tapesNode.getChildNodes();
		for (int x = 0; x < tapeNodes.getLength(); x++) {
			Node tapeNode = tapeNodes.item(x);

			if (tapeNode.getNodeName().equals(TuringXML.TAPE.toString())) {
				Element tapeElement = (Element) tapeNode;

				String name = tapeElement.getAttribute(TuringXML.TAPE_NAME.toString());

				Tape tape = new Tape(name);
				turingMachine.addTape(tape);
			}
		}

		Element tapesElement = (Element) tapesNode;

		String inputTapeName = tapesElement.getAttribute(TuringXML.TAPES_INPUT.toString());
		inputTape = turingMachine.getTape(inputTapeName);
		if (inputTape == null) {
			throw new TuringXMLParserException("Cannot set the input tape to '" + inputTapeName + "' because this tape does not exist!");
		}

		String outputTapeName = tapesElement.getAttribute(TuringXML.TAPES_OUTPUT.toString());
		outputTape = turingMachine.getTape(outputTapeName);
		if (outputTape == null) {
			throw new TuringXMLParserException("Cannot set the output tape to '" + inputTapeName + "' because this tape does not exist!");
		}
	}

	private void parseAlphabet(Alphabet alphabet, Node alphabetNode) {
		NodeList symbolNodes = alphabetNode.getChildNodes();
		for (int x = 0; x < symbolNodes.getLength(); x++) {
			Node symbolNode = symbolNodes.item(x);

			if (symbolNode.getNodeName().equals(TuringXML.SYMBOL.toString()) || symbolNode.getNodeName().equals(TuringXML.BLANKSYMBOL.toString())) {
				Element symbolElement = (Element) symbolNode;

				String value = symbolElement.getAttribute(TuringXML.SYMBOL_VALUE.toString());

				if (symbolNode.getNodeName().equals(TuringXML.SYMBOL.toString())) {
					alphabet.addSymbol(value.charAt(0));
				}
				else {
					alphabet.addSymbol(value.charAt(0), null);
				}
			}
		}
	}

	private void parseTransitions(TuringMachine turingMachine, Alphabet alphabet, Node transitionsNode) throws TuringXMLParserException {
		NodeList transitionNodes = transitionsNode.getChildNodes();
		for (int x = 0; x < transitionNodes.getLength(); x++) {
			Node transitionNode = transitionNodes.item(x);

			if (transitionNode.getNodeName().equals(TuringXML.TRANSITION.toString())) {
				Element transitionElement = (Element) transitionNode;

				String currentStateName = transitionElement.getAttribute(TuringXML.TRANSITION_CURRENTSTATE.toString());
				String nextStateName = transitionElement.getAttribute(TuringXML.TRANSITION_NEXTSTATE.toString());

				State currentState = turingMachine.getState(currentStateName);
				if (currentState == null) {
					throw new TuringXMLParserException("Cannot set the current state to '" + currentStateName + "' because this state does not exist!");
				}

				State nextState = turingMachine.getState(nextStateName);
				if (nextState == null) {
					throw new TuringXMLParserException("Cannot set the next state to '" + nextStateName + "' because this state does not exist!");
				}

				Transition transition = new Transition(currentState, nextState);
				this.parseTapeTransitions(turingMachine, alphabet, transition, transitionNode);

				turingMachine.addTransition(transition);
			}
		}
	}

	private void parseTapeTransitions(TuringMachine turingMachine, Alphabet alphabet, Transition transition, Node tapeTransitionsNode) throws TuringXMLParserException {
		NodeList tapeTransitionNodes = tapeTransitionsNode.getChildNodes();
		for (int x = 0; x < tapeTransitionNodes.getLength(); x++) {
			Node tapeTransitionNode = tapeTransitionNodes.item(x);

			if (tapeTransitionNode.getNodeName().equals(TuringXML.TAPETRANSITION.toString())) {
				Element tapeTransitionElement = (Element) tapeTransitionNode;

				String tapeName = tapeTransitionElement.getAttribute(TuringXML.TAPETRANSITION_TAPE.toString());
				String readSymbol = tapeTransitionElement.getAttribute(TuringXML.TAPETRANSITION_READSYMBOL.toString());
				String writeSymbol = tapeTransitionElement.getAttribute(TuringXML.TAPETRANSITION_WRITESYMBOL.toString());
				String tapeMotionEnum = tapeTransitionElement.getAttribute(TuringXML.TAPETRANSITION_TAPEMOTION.toString());

				Tape tape = turingMachine.getTape(tapeName);
				if (tape == null) {
					throw new TuringXMLParserException("Cannot set the tape to '" + tapeName + "' because this tape does not exist!");
				}

				Character read = null;
				try {
					read = alphabet.getHiddenCharacter(readSymbol.charAt(0));
				}
				catch (IllegalArgumentException exception) {
					throw new TuringXMLParserException("Cannot read the symbol '" + readSymbol + "' because it does not exist!");
				}

				Character write = null;
				try {
					write = alphabet.getHiddenCharacter(writeSymbol.charAt(0));
				}
				catch (IllegalArgumentException exception) {
					throw new TuringXMLParserException("Cannot write the symbol '" + writeSymbol + "' because it does not exist!");
				}

				TapeMotion tapeMotion = TapeMotion.valueOf(tapeMotionEnum);
				if (tapeMotion == null) {
					throw new TuringXMLParserException("The tape motion '" + tapeMotionEnum + "' does not exist!");
				}

				TapeTransition tapeTransition = new TapeTransition(tape, read, write, tapeMotion);
				transition.addTapeTransition(tapeTransition);
			}
		}
	}
}
