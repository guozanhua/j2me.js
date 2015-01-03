package gfx;

import javax.microedition.midlet.*;
import com.nokia.mid.ui.TextEditor;
import javax.microedition.lcdui.*;

public class TextEditorGfxTest extends MIDlet {
    public static String checkCodeFormat(String code) {
        if (code == null) {
            return code;
        }
        if (code.length() == 10) {
            String c1 = code.substring(0, 5);
            String c2 = code.substring(5);
            return c1 + " " + c2;
        } else if (code.length() == 6) {
            String c1 = code.substring(0, 2);
            String c2 = code.substring(2);
            return c1 + " " + c2;
        } else {
            return code;
        }
    }

    private final static int LEAD_OFFSET = 0xD800 - (0x10000 >> 10);

    public static String getSurrogatePairs(String inputString) {
        int character;
        char low, high;
        int start = 0, end = 0;
        if (inputString == null) {
            return inputString;
        }

        StringBuffer sb = new StringBuffer(1000);

        // Go through all characters in the input.
        // Space (0x20) is used as separator
        do {
            end = inputString.indexOf(" ", start);

            // Space not found -> last sub-string
            if (end == -1) {
                end = inputString.length();
            }

            try {
                character = Integer.parseInt(inputString.substring(start, end), 16);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            // Anything below 0xffff is not surrogate pair
            if (character < 0xffff) {
                sb.append((char) character);
            } else {
                // From http://www.unicode.org/faq/utf_bom.html
                high = (char) (LEAD_OFFSET + (character >> 10));
                low = (char) (0xDC00 + (character & 0x3FF));

                sb.append(high);
                sb.append(low);
            }

            // skip the space
            start = (end + 1);
        } while (end != inputString.length());

        return sb.toString();
    }

    class TestCanvas extends Canvas {
        protected void paint(Graphics g) {
            g.setColor(0x00FFFFFF);
            g.fillRect(0, 0, getWidth(), getHeight());

            System.out.println("PAINTED");
        }
    }

    public void startApp() {
        TestCanvas canvas = new TestCanvas();
        canvas.setFullScreenMode(true);
        Display.getDisplay(this).setCurrent(canvas);

        String emoji1 = getSurrogatePairs(checkCodeFormat("1f1ee1f1f9"));
        String emoji2 = getSurrogatePairs(checkCodeFormat("1f609"));
        String emoji3 = getSurrogatePairs(checkCodeFormat("2320e3"));

        TextEditor textEditor = TextEditor.createTextEditor("A stri" + emoji1 + "ng wit" + emoji2 + "h emoj" + emoji3 + "i", 50, TextField.ANY, 150, 70);
        textEditor.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        textEditor.setParent(canvas);
        textEditor.setMultiline(true);
        textEditor.setBackgroundColor(0x00FFFFFF);
        textEditor.setForegroundColor(0xFF000000);
        textEditor.setVisible(true);
        textEditor.setFocus(false);
        textEditor.setPosition(50, 50);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
};
