package com.app.APICode.utility;

import java.util.ArrayList;
import java.util.List;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.springframework.stereotype.Component;

@Component
public class RandomPassword {
    List<CharacterRule> rules;
    PasswordGenerator generator;
    PasswordValidator validator;

    public RandomPassword() {
        rules = new ArrayList<>();
        
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);
        rules.add(lowerCaseRule);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);
        rules.add(upperCaseRule);
    
        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);
        rules.add(digitRule);
    
        CharacterData specialChars = new CharacterData() {
            @Override
            public String getErrorCode() {
                return "ERRONEOUS_SPECIAL_CHARS";
            }
    
            @Override
            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);
        rules.add(splCharRule);

        generator = new PasswordGenerator();
        validator = new PasswordValidator(rules);
    }

    public String generatePassayPassword() {
        return generator.generatePassword(10, rules);
    }

    public boolean validateComplexPassword(String password) {
        return validator.validate(new PasswordData(password)).isValid();
    }
}
