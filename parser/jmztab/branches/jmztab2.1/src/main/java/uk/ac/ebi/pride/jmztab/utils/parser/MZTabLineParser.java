package uk.ac.ebi.pride.jmztab.utils.parser;

import uk.ac.ebi.pride.jmztab.model.Section;
import uk.ac.ebi.pride.jmztab.utils.errors.FormatErrorType;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabError;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabException;

import static uk.ac.ebi.pride.jmztab.model.MZTabConstants.TAB;

/**
 * User: Qingwei
 * Date: 10/02/13
 */
public class MZTabLineParser {
    protected int lineNumber;

    protected Section section;

    protected String line;

    /**
     * based on TAB char to split raw line into String array.
     */
    protected String[] items;

    protected MZTabErrorList errorList;

    /**
     * We assume that user before call this method, have parse the raw line
     * is not empty line and start with section prefix.
     */
    protected void parse(int lineNumber, String line, MZTabErrorList errorList) throws MZTabException {
        this.lineNumber = lineNumber;
        this.line = line;
        this.errorList = errorList == null ? new MZTabErrorList() : errorList;

        this.items = line.split("\\s*" + TAB + "\\s*");
        items[0] = items[0].trim();
        items[items.length - 1] = items[items.length - 1].trim();

        section = Section.findSection(items[0]);

        if (section == null) {
            MZTabError error = new MZTabError(FormatErrorType.LinePrefix, lineNumber, items[0]);
            errorList.add(error);
        }
    }
}