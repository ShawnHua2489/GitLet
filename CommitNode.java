package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import java.util.Date;

public class CommitNode implements Serializable {
    String commitMessage;
    String timeStamp;
    String parentID;
    TreeMap<String, String> nameAndID; //filename and fileID (SHA-1)
    TreeMap<String, Blob> iDAndBlob;

    public CommitNode(String message) {
        this.commitMessage = message;
        this.timeStamp = convertDate(new Date());
        this.nameAndID = new TreeMap<>();
        this.iDAndBlob = new TreeMap<>();
    }

    public String convertDate(Date date) {
        StringBuilder builder = new StringBuilder();
        builder.append("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat(builder.toString());
        return dateFormat.format(date);
    }

}
