/**
 * @author $Author: $
 * @version $Revision: $
 * @date $Date: $
 */
package turingmachine.model.entities;

import java.util.HashMap;
import java.util.Map;

public class Alphabet {

	private final Map<Character, Character> visibleCharacters;
	private final Map<Character, Character> hiddenCharacters;

	public Alphabet() {
		this.visibleCharacters = new HashMap<Character, Character>();
		this.hiddenCharacters = new HashMap<Character, Character>();
	}

	public Character getVisibleCharacter(Character hiddenCharacter) {
		if (!this.visibleCharacters.containsKey(hiddenCharacter)) {
			throw new IllegalArgumentException("A symbol with the hidden character '" + hiddenCharacter + "' does not exist!");
		}

		return this.visibleCharacters.get(hiddenCharacter);
	}

	public Character getHiddenCharacter(Character visibleCharacter) {
		if (!this.hiddenCharacters.containsKey(visibleCharacter)) {
			throw new IllegalArgumentException("A symbol with the visible character '" + visibleCharacter + "' does not exist!");
		}

		return this.hiddenCharacters.get(visibleCharacter);
	}

	public Boolean addSymbol(Character visibleCharacter) {
		return this.addSymbol(visibleCharacter, visibleCharacter);
	}

	public Boolean addSymbol(Character visibleCharacter, Character hiddenCharacter) {
		if (this.visibleCharacters.containsKey(hiddenCharacter) || this.hiddenCharacters.containsKey(visibleCharacter)) {
			return Boolean.FALSE;
		}

		this.visibleCharacters.put(hiddenCharacter, visibleCharacter);
		this.hiddenCharacters.put(visibleCharacter, hiddenCharacter);
		return Boolean.TRUE;
	}

	public Boolean removeSymbol(Character visibleCharacter) {
		return this.removeSymbol(visibleCharacter, visibleCharacter);
	}

	public Boolean removeSymbol(Character visibleCharacter, Character hiddenCharacter) {
		if (!this.visibleCharacters.containsKey(hiddenCharacter) || !this.hiddenCharacters.containsKey(visibleCharacter)) {
			return Boolean.FALSE;
		}

		this.visibleCharacters.remove(hiddenCharacter);
		this.hiddenCharacters.remove(visibleCharacter);
		return Boolean.TRUE;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((this.hiddenCharacters == null) ? 0 : this.hiddenCharacters.hashCode());
		result = 31 * result + ((this.visibleCharacters == null) ? 0 : this.visibleCharacters.hashCode());
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

		Alphabet other = (Alphabet) obj;
		if (this.hiddenCharacters == null && other.hiddenCharacters != null) {
			return false;
		}
		else if (!this.hiddenCharacters.equals(other.hiddenCharacters)) {
			return false;
		}
		if (this.visibleCharacters == null && other.visibleCharacters != null) {
			return false;
		}
		else if (!this.visibleCharacters.equals(other.visibleCharacters)) {
			return false;
		}

		return true;
	}

}
