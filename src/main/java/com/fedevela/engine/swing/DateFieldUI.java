package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.engine.ConfigureEngine;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class DateFieldUI extends FieldUI {

    private static final long serialVersionUID = 4851228827808190290L;
    final SimpleDateFormat simpleDateFormat;
    private String maskPattern = null;
    private final char[] possibleSimpleDateFormatPatternChars = {'y', 'd', 'M'};




    public DateFieldUI(ConfigureEngine ce) {
        super(ce);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setLenient(false);
        maskPattern = datePatternToMaskPattern(simpleDateFormat.toPattern());
        setRegexPattern(Pattern.compile("\\d{2,2}\\/\\d{2,2}\\/\\d{4,4}"));
        DateFieldUI.RegexMaskFormatter regexMaskFormatter;
        try {
            regexMaskFormatter = new DateFieldUI.RegexMaskFormatter(maskPattern);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Mask Pattern not Parseable.", e);
        }
        regexMaskFormatter.setPlaceholderCharacter('_');
        regexMaskFormatter.setAllowsInvalid(false);
        regexMaskFormatter.setOverwriteMode(true);
        regexMaskFormatter.setCommitsOnValidEdit(true);
        setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory();
        defaultFormatterFactory.setDisplayFormatter(regexMaskFormatter);
        defaultFormatterFactory.setDefaultFormatter(regexMaskFormatter);
        defaultFormatterFactory.setEditFormatter(regexMaskFormatter);
        defaultFormatterFactory.setNullFormatter(regexMaskFormatter);
        setFormatterFactory(defaultFormatterFactory);
    }

    public String getMaskPattern() {
        return maskPattern;
    }

    private String datePatternToMaskPattern(String datePattern) {
        String msk = datePattern;
        for (char simpleDateFormatPatternChar : possibleSimpleDateFormatPatternChars) {
            msk = msk.replace(simpleDateFormatPatternChar, '#');
        }

        return msk;
    }

    public class RegexMaskFormatter extends MaskFormatter {

        private static final long serialVersionUID = 4914213405643291776L;

        public RegexMaskFormatter(String mask) throws ParseException {
            super(mask);
        }

        /**
         * Parses
         * <code>text</code> returning an arbitrary Object. Some formatters may
         * return null. <p> If a
         * <code>Pattern</code> has been specified and the text completely
         * matches the regular expression this will invoke
         * <code>setMatcher</code>.
         *
         * @throws ParseException if there is an error in the conversion
         * @param text String to convert
         * @return Object representation of text
         */
        @Override
        public Object stringToValue(String text) throws ParseException {
            Pattern pattern = getRegexPattern();
            Matcher matcher;
            if (pattern != null) {
                matcher = pattern.matcher(text);
                if (!matcher.matches()) {
                    throw new ParseException("Regex Pattern did not match", 0);
                }
            }
            try {
                simpleDateFormat.parse(text);
            } catch (ParseException ex) {
                setUserValid(ex.getMessage());
            }
            return super.stringToValue(text);
        }
    }
}
