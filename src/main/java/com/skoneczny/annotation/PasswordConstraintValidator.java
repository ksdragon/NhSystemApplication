package com.skoneczny.annotation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.log4j.Logger;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import com.skoneczny.controllers.changePasswordController;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	private DictionaryRule dictionaryRule;
	private final Logger logger = Logger.getLogger(changePasswordController.class);
	// check if password is crypted
	private Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]?\\$\\d{1,2}\\$[.\\/A-Za-z0-9]{53}$");

	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		dictionaryRuleReader();
	}

	private void dictionaryRuleReader() {
		try {
			String invalidPasswordList = this.getClass().getResource("/invalid-password-list.txt").getFile();
			dictionaryRule = new DictionaryRule(new WordListDictionary(WordLists.createFromReader(
					// Reader around the word list file
					new FileReader[] { new FileReader(invalidPasswordList) },
					// True for case sensitivity, false otherwise
					false,
					// Dictionaries must be sorted
					new ArraysSort())));
		} catch (IOException e) {
			throw new RuntimeException("could not load word list", e);
		}
	}

	/*
	 * Get local properties.
	 */
	public Properties getLocalProperties() throws IOException, FileNotFoundException {
		Locale locale = Locale.getDefault();
		String nameLocalePropertiesFile = "/messages_" + locale.getLanguage() + ".properties";
		Properties props = new Properties();
		String file = this.getClass().getResource(nameLocalePropertiesFile).getFile();
		props.load(new FileInputStream(file));
		return props;

	}

	public String generateRanndomPassword() {
		List<CharacterRule> listRule = listOfCharacterRulePasswordValidation();
		PasswordGenerator generator = new PasswordGenerator();
		String password = generator.generatePassword(16, listRule);
		return password;

	}

	/*
	 * Validation restriction with MessageResolver
	 */
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		try {
			Properties localProperties = getLocalProperties();
			MessageResolver resolver = new PropertiesMessageResolver(localProperties);
			PasswordValidator validator = new PasswordValidator(resolver, listOfRulePasswordValidation());

			if (!BCRYPT_PATTERN.matcher(password).matches()) {
				RuleResult result = validator.validate(new PasswordData(password));
				if (result.isValid()) {
					logger.info("Validation password return true");
					return true;
				}
//				else {
//					System.out.println("Invalid password:");
//					for (String msg : validator.getMessages(result)) {
//						System.out.println(msg);
//					}
//				}

				List<String> messages = validator.getMessages(result);
				String messageTemplate = messages.stream().collect(Collectors.joining(","));
				if(context != null) {
				context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation()
						.disableDefaultConstraintViolation();}
			} else {
				return true;
			}

		} catch (IOException e) {
			logger.error("Validation has error." + e.getMessage());
			e.printStackTrace();
		}
		logger.info("Validation password return false");
		return false;
	}

	public List<Rule> listOfRulePasswordValidation() {
		if (dictionaryRule == null) {
			dictionaryRuleReader();
		}
		return Arrays.asList(

				// at least 8 characters
				new LengthRule(8, 30),

				// at least one upper-case character
				new CharacterRule(EnglishCharacterData.UpperCase, 1),

				// at least one lower-case character
				new CharacterRule(EnglishCharacterData.LowerCase, 1),

				// at least one digit character
				new CharacterRule(EnglishCharacterData.Digit, 1),

				// at least one symbol (special character)
				new CharacterRule(EnglishCharacterData.Special, 1),

				// no whitespace
				new WhitespaceRule(),

				// no common passwords
				dictionaryRule);
	}

	public List<CharacterRule> listOfCharacterRulePasswordValidation() {
		return Arrays.asList(

				// at least one upper-case character
				new CharacterRule(EnglishCharacterData.UpperCase, 1),

				// at least one lower-case character
				new CharacterRule(EnglishCharacterData.LowerCase, 1),

				// at least one digit character
				new CharacterRule(EnglishCharacterData.Digit, 1),

				// at least one symbol (special character)
				new CharacterRule(EnglishCharacterData.Special, 1));

	}
}
