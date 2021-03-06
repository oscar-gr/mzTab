package uk.ac.ebi.pride.jmztab.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ebi.pride.jmztab.MzTabParsingException;

/**
 * Represents both, cv as well as userParams
 *
 * @author jg
 *
 */
public class Param {
    public static String SEPARATOR = ",";
    public static String SEPARATOR_ESCAPED = "&#44;";

    /**
     * Possible param types.
     *
     * @author jg
     *
     */
    public enum ParamType {

	CV_PARAM, USER_PARAM
    };
    /**
     * The param's type.
     */
    private ParamType type;
    /**
     * Param properties
     */
    String cvLabel;
    String accession;
    String name;
    String value;
    /**
     * Pattern used to parse a Param object from a mzTab String.
     */
    private static Pattern mzTabParamPattern = Pattern.compile("\\[([^,]+)?,([^,]+)?,([^,]+),([^,]*)\\]");

    /**
     * Creates a new cvParam.
     *
     * @param cvLabel
     * @param accession
     * @param name
     * @param value
     * @throws MzTabParsingException
     */
    public Param(String cvLabel, String accession, String name, String value) throws MzTabParsingException {
	TableObject.checkStringValue(cvLabel);
	TableObject.checkStringValue(accession);
	TableObject.checkStringValue(name);
	TableObject.checkStringValue(value);

	this.cvLabel = escapeSpecialCharacters(cvLabel);
	this.accession = escapeSpecialCharacters(accession);
	this.name = escapeSpecialCharacters(name);
	this.value = escapeSpecialCharacters(value);

	if (cvLabel == null && accession == null) {
	    this.type = ParamType.USER_PARAM;
	} else {
	    this.type = ParamType.CV_PARAM;
	}
	
	if (this.type == ParamType.USER_PARAM && (this.name == null || this.name.length() < 1)) {
	    throw new MzTabParsingException("User parameters must have a name.");
	}
    }

    /**
     * Creates a new userParam.
     *
     * @param name
     * @param value
     * @throws MzTabParsingException
     */
    public Param(String name, String value) throws MzTabParsingException {
	this(null, null, name, value);
    }

    /**
     * Creates a new param object from a mzTab string.
     *
     * @param mzTabString
     */
    public Param(String mzTabString) {
	// parse the mzTabString
	Matcher matcher = mzTabParamPattern.matcher(mzTabString);

	if (!matcher.find()) {
	    throw new RuntimeException("Invalid mzTabString passed to Param constructor: \"" + mzTabString + "\"");
	}

	// check if it's a user or a cvParam by checking whether a cvLabel is set
	if (matcher.group(1) == null || matcher.group(1).trim().length() < 1) {
	    this.type = ParamType.USER_PARAM;
	    this.name = matcher.group(3).trim();
	    this.value = matcher.group(4).trim();
	} else {
	    this.type = ParamType.CV_PARAM;
	    this.cvLabel = matcher.group(1).substring(0, matcher.group(1).length()).trim();
	    this.accession = matcher.group(2).substring(0, matcher.group(2).length()).trim();
	    this.name = matcher.group(3).trim();
	    this.value = matcher.group(4).trim();
	}
    }

    public ParamType getType() {
	return type;
    }

    public String getCvLabel() {
	return decodeSpecialCharacters(cvLabel);
    }

    public String getAccession() {
	return decodeSpecialCharacters(accession);
    }

    public String getName() {
	return decodeSpecialCharacters(name);
    }

    public String getValue() {
	return decodeSpecialCharacters(value);
    }
    
    private String escapeSpecialCharacters(String value) {
	if (value == null) {
	    return null;
	}
	
	String escapedValue = value.replaceAll(SEPARATOR, SEPARATOR_ESCAPED);
	
	return escapedValue;
    }
    
    private String decodeSpecialCharacters(String value) {
	if (value == null) {
	    return null;
	}
	
	return value.replaceAll(SEPARATOR_ESCAPED, SEPARATOR);
    }

    @Override
    public String toString() {
	String theCvLabel = (cvLabel == null) ? "" : escapeSpecialCharacters(cvLabel);
	String theAccession = (accession == null) ? "" : escapeSpecialCharacters(accession);
	String theName = (name == null) ? "" : escapeSpecialCharacters(name);
	String theValue = (value == null) ? "" : escapeSpecialCharacters(value);
	
	return "[" + theCvLabel + "," + theAccession + "," + theName + "," + theValue + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((accession == null) ? 0 : accession.hashCode());
	result = prime * result + ((cvLabel == null) ? 0 : cvLabel.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((value == null) ? 0 : value.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Param other = (Param) obj;
	if (accession == null) {
	    if (other.accession != null) {
		return false;
	    }
	} else if (!accession.equals(other.accession)) {
	    return false;
	}
	if (cvLabel == null) {
	    if (other.cvLabel != null) {
		return false;
	    }
	} else if (!cvLabel.equals(other.cvLabel)) {
	    return false;
	}
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	if (value == null) {
	    if (other.value != null) {
		return false;
	    }
	} else if (!value.equals(other.value)) {
	    return false;
	}
	return true;
    }
}
