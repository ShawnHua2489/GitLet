package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    byte[] content;

    public Blob(File s) {
        this.content = Utils.readContents(s);
    }

}
