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
		return this.visibleCharacters.get(hiddenCharacter);
	}

	public Character getHiddenCharacter(Character visibleCharacter) {
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

}
