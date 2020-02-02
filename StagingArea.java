package gitlet;




import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;


public class StagingArea implements Serializable {

    public ArrayList<String> removed; // filename
    public TreeMap<String, String> stagedID; // filename, ID
    public TreeMap<String, Blob> stagedBlobs; // ID, blob
    public String curCommitID = ""; //not sure. store cur commit's ID

    public StagingArea() {
        this.removed = new ArrayList<>();
        this.stagedID = new TreeMap<>();
        this.stagedBlobs = new TreeMap<>();
    }

    public ArrayList<String> getRemoved() {
        return removed;
    }


    public void add(String filename, File file) {
        if (removed.contains(filename)) {
            removed.remove(filename);
            return;
        }


        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);


        Blob newFile = new Blob(file);
        String temp = Utils.sha1(newFile.content);


        if (tree.head.nameAndID.containsKey(filename)
                && tree.head.nameAndID.get(filename).equals(temp)) {
            return;
        } else if (stagedID.containsKey(filename)) {
            if (stagedID.get(filename).equals(temp)) {
                return;
            }
            stagedID.replace(filename, temp);
            stagedBlobs.put(temp, newFile);
        } else {
            stagedID.put(filename, temp);
            stagedBlobs.put(temp, newFile);
        }



        //System.out.println(removed);
        //System.out.println(stagedID);
        //System.out.println(stagedBlobs);
        //System.out.println(stagedID.isEmpty());

    }

    public void rm(String filename) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        //System.out.println(tree.head.commitMessage);
        //System.out.println(tree.getCommitNode(tree.head.parentID).commitMessage);
        //System.out.println(tree.nodePointer.size());
        if (!tree.head.nameAndID.containsKey(filename)
                && !stagedID.keySet().contains(filename)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }

        if (tree.head.nameAndID.containsKey(filename)) {
            Utils.restrictedDelete(filename);
            removed.add(filename);
        }
        if (stagedID.containsKey(filename)) {
            stagedID.remove(filename);            // no need to remove blob?
        }



        File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        Serial.serialize(ctree, tree);
    }

    public void commit(String message) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);



        CommitNode curNode = new CommitNode(message);
        //CommitNode copy = tree.head;
        //curNode.NameAndID = copy.NameAndID;
        //curNode.IDAndBlob = copy.IDAndBlob;

        for (String s: tree.head.nameAndID.keySet()) {
            curNode.nameAndID.put(s, tree.head.nameAndID.get(s));
        }

        for (String s: tree.head.iDAndBlob.keySet()) {
            curNode.iDAndBlob.put(s, tree.head.iDAndBlob.get(s));
        }

        //curNode.NameAndID = tree.head.NameAndID;
        //curNode.IDAndBlob = tree.head.IDAndBlob;

        for (String s: removed) {
            if (curNode.nameAndID.containsKey(s)) {
                curNode.nameAndID.remove(s);
            }
        }


        for (String s: stagedID.keySet()) {
            if (curNode.nameAndID.keySet().contains(s)) {
                curNode.nameAndID.replace(s, stagedID.get(s));
                curNode.iDAndBlob.put(stagedID.get(s), stagedBlobs.get(stagedID.get(s)));
                stagedID.remove(s);
                //if contains name, then change the blob to staged one
                // if does not contain name, then make new entry
                // with new filename, ID and blob.
            }
        }

        if (!(stagedID.keySet().isEmpty())) {
            for (String s : stagedID.keySet()) {
                String fileID = stagedID.get(s);
                curNode.nameAndID.put(s, fileID);
                curNode.iDAndBlob.put(stagedID.get(s), stagedBlobs.get(stagedID.get(s)));
                //stagedID.remove(s);
            }
        }

        curNode.parentID = tree.getCommitID(tree.head);
        String curID = Utils.sha1(curNode.commitMessage + curNode.timeStamp);

        stagedID = new TreeMap<>();
        stagedBlobs = new TreeMap<>();
        removed.clear();

        tree.head = curNode;
        tree.nodePointer.put(curID, curNode);
        tree.branchPointer.put(tree.curBranch, curID);
        tree.allNodesPointer.put(curID, tree.curBranch);
        //tree.IDPointer.put(curNode, curID);
        //System.out.println(tree.nodePointer);


        File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        Serial.serialize(ctree, tree);

    }

    public int sISize() {
        return stagedID.size();
    }

    public int rMSize() {
        return removed.size();
    }

    public static void main(String... args) {
        //StagingArea s = new StagingArea();
        //s.init();

        //s.status();
        //s.log();
        //s.globalLog();
        //s.branch("hallo!");
        //s.rmBranch("hallo!");
        //s.add("wug.txt");
    }

}
