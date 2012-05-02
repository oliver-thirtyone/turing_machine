/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Stack;

public class Tape extends Observable {

	public static final Integer DEFAULT_VISIBLE_TAPE_RANGE = 15;
	public static final Character DEFAULT_BLANK_CHARACTER = '#';

	private final String name;
	private final Stack<Character> leftCharacters;
	private final Stack<Character> rightCharacters;

	private Character currentCharacter;

	public Tape(String name) {
		this.name = name;

		this.leftCharacters = new Stack<Character>();
		this.rightCharacters = new Stack<Character>();

		this.currentCharacter = null;
	}

	public synchronized void reset() {
		this.currentCharacter = null;

		this.leftCharacters.clear();
		this.rightCharacters.clear();
	}

	public synchronized Character read() {
		return this.getCurrentCharacter();
	}

	public synchronized void write(Character character) {
		this.setCurrentCharacter(character);
	}

	public synchronized void moveCursor(TapeMotion direction) {
		switch (direction) {
			case L:
				this.moveCursor(direction, this.rightCharacters, this.leftCharacters);
				break;
			case R:
				this.moveCursor(direction, this.leftCharacters, this.rightCharacters);
				break;
		}
	}

	public synchronized List<Character> getTape() {
		Integer range = this.leftCharacters.size() > this.rightCharacters.size() ? this.leftCharacters.size() : this.rightCharacters.size();
		return this.getTape(range);
	}

	public synchronized List<Character> getTape(Integer range) {
		List<Character> characters = new ArrayList<Character>();

		// Add left characters
		for (int i = this.leftCharacters.size() - range; i < this.leftCharacters.size(); i++) {
			try {
				characters.add(this.leftCharacters.get(i));
			}
			catch (ArrayIndexOutOfBoundsException exception) {
				characters.add(null);
			}
		}

		// Add current character
		characters.add(this.currentCharacter);

		// Add right characters
		for (int i = this.rightCharacters.size(); i > this.rightCharacters.size() - range; i--) {
			try {
				characters.add(this.rightCharacters.get(i - 1));
			}
			catch (ArrayIndexOutOfBoundsException exception) {
				characters.add(null);
			}
		}

		return characters;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(this.getName());
		stringBuilder.append(": ");

		List<Character> characters = this.getTape(Tape.DEFAULT_VISIBLE_TAPE_RANGE);
		for (Character character : characters) {
			stringBuilder.append(character != null ? character : Tape.DEFAULT_BLANK_CHARACTER);
		}

		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.currentCharacter == null) ? 0 : this.currentCharacter.hashCode());
		result = 31 * result + ((this.leftCharacters == null) ? 0 : this.leftCharacters.hashCode());
		result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = 31 * result + ((this.rightCharacters == null) ? 0 : this.rightCharacters.hashCode());
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

		Tape other = (Tape) obj;
		if (this.currentCharacter == null && other.currentCharacter != null) {
			return false;
		}
		else if (!this.currentCharacter.equals(other.currentCharacter)) {
			return false;
		}
		if (this.leftCharacters == null && other.leftCharacters != null) {
			return false;
		}
		else if (!this.leftCharacters.equals(other.leftCharacters)) {
			return false;
		}
		if (this.name == null && other.name != null) {
			return false;
		}
		else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.rightCharacters == null && other.rightCharacters != null) {
			return false;
		}
		else if (!this.rightCharacters.equals(other.rightCharacters)) {
			return false;
		}
		return true;
	}

	private Character getCurrentCharacter() {
		return this.currentCharacter;
	}

	private void setCurrentCharacter(Character currentCharacter) {
		if (this.currentCharacter == currentCharacter) {
			return;
		}

		this.currentCharacter = currentCharacter;
		this.setChanged();
	}

	private void moveCursor(TapeMotion direction, Stack<Character> pushToStack, Stack<Character> popFromStack) {
		pushToStack.push(this.currentCharacter);
		this.setCurrentCharacter(popFromStack.isEmpty() ? null : popFromStack.pop());

		this.setChanged();
	}

}
