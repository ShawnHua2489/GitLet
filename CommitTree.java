package gitlet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.File;

public class CommitTree implements Serializable {
    public CommitNode head;
    public String curBranch; //branchName
    public TreeMap<String, String> branchPointer; //branchName, CommitID
    public TreeMap<String, CommitNode> nodePointer; //CommitID, CommitNode
    public ArrayList<String> removedBranch;
    public TreeMap<String, String> allNodesPointer; //CommitID, branchName
    //public TreeMap<CommitNode, String> IDPointer; //CommitNode, CommitID

    public CommitTree() {
        CommitNode initial = new CommitNode("initial commit"); //no parent
        initial.parentID = null;
        String curID = Utils.sha1(initial.commitMessage + initial.timeStamp);

        this.head = initial;
        this.curBranch = "master";
        this.branchPointer = new TreeMap<>();
        this.nodePointer = new TreeMap<>();
        this.nodePointer.put(curID, initial);
        //this.IDPointer = new TreeMap<>();
        this.branchPointer.put(this.curBranch, ""); // first commit has no parentID
        this.removedBranch = new ArrayList<>();
        this.allNodesPointer = new TreeMap<>();

    }


    public void addBranch(String branchName) {
        if (branchPointer.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        branchPointer.put(branchName, this.getCommitID(head));
    }

    public void rmBranch(String branchName) {
        removedBranch.add(branchName);
        // what if current branch is removed? where should pointer do.
    }

    public CommitNode getCommitNode(String commitID) { // return a commitNode with its CommitID
        // to be completed
        CommitNode curNode = nodePointer.get(commitID);
        return curNode;
    }

    public String getCommitID(CommitNode node) { // return a commitNode with its CommitID
        // to be completed
        /*
        if (IDPointer.keySet().contains(node)) {
            return IDPointer.get(node);
        }
        */
        return Utils.sha1(node.commitMessage + node.timeStamp);
    }

    public void log() {
        // print info about head commit
        // find its parent commit using parent ID
        // repeat until parent ID is "".

        // have to follow a certain format

        CommitNode temp = head;
        if (temp.parentID == null) {
            return;
        }
        while (temp != null) {
            System.out.println("===");
            System.out.println("Commit " + getCommitID(temp));
            System.out.println(temp.timeStamp);
            System.out.println(temp.commitMessage);
            System.out.println("");
            if (temp.parentID == null) {
                return;
            }
            temp = getCommitNode(temp.parentID);
        }
    }

    public void globalLog() {
        ArrayList<String> printedID = new ArrayList<>();
        CommitNode temp2 = head;
        if (temp2.parentID == null) {
            return;
        }
/*
        for (String s: branchPointer.keySet()) {
            CommitNode temp = getCommitNode(branchPointer.get(s));
            System.out.println("Commit " + getCommitID(temp));
            System.out.println(temp.TimeStamp);
            System.out.println(temp.commitMessage);
            System.out.println("");
        }
*/

        for (String s: branchPointer.keySet()) {
            CommitNode temp = getCommitNode(branchPointer.get(s));

            while (temp != null) {
                if (printedID.contains(this.getCommitID(temp))) {
                    break;
                }
                System.out.println("Commit " + getCommitID(temp));
                System.out.println(temp.timeStamp);
                System.out.println(temp.commitMessage);
                System.out.println("");

                printedID.add(this.getCommitID(temp));
                if (temp.parentID == null) {
                    break;
                }
                temp = getCommitNode(temp.parentID);
            }
        }

    }

    public void find(String message) {
        ArrayList<String> searchedID = new ArrayList<>();
        boolean commitExist = false;
        for (String s: branchPointer.keySet()) {
            CommitNode temp = getCommitNode(branchPointer.get(s));
            while (temp != null) {
                if (searchedID.contains(this.getCommitID(temp))) {
                    break;
                }
                if (temp.commitMessage.equals(message)) {
                    commitExist = true;
                    System.out.println(getCommitID(temp));
                }
                searchedID.add(this.getCommitID(temp));
                if (temp.parentID == null) {
                    break;
                }
                temp = getCommitNode(temp.parentID);
            }
        }
        if (!commitExist) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    // for all the checkouts and reset,
    // the input CommitID may be abbreviated
    public void checkoutFile(String filename) {

        checkoutIDFile(this.getCommitID(this.head), filename);
        //String fileID = head.NameAndID.get(filename);
        //Blob file = head.IDAndBlob.get(fileID);
        //File coFile = new File(filename);
        //Utils.writeContents(coFile, file.content);
    }

    public void checkoutIDFile(String commitID, String filename) {

        if (commitID.length() < 40) {
            for (String iD: nodePointer.keySet()) {
                if (iD.contains(commitID)) {
                    commitID = iD;
                }
            }
        }

        if (!nodePointer.containsKey(commitID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        CommitNode cur = getCommitNode(commitID);
        //String x = cur.commitMessage;
        //String y = this.head.commitMessage;
        //String fake = head.NameAndID.get(filename);
        if (!cur.nameAndID.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        String fileID = cur.nameAndID.get(filename);
        Blob file = cur.iDAndBlob.get(fileID);



        File working = new File(System.getProperty("user.dir"));



        for (File f: working.listFiles()) {
            if (f.getName().equals(filename)) {
                Utils.restrictedDelete(f);
            }
        }
        File coIDFile = new File(filename);
        Utils.writeContents(coIDFile, file.content);
    }

    public void checkoutBranch(String branchName) {
        String pwd = System.getProperty("user.dir");
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        File working = new File(System.getProperty("user.dir"));

        if (!branchPointer.containsKey(branchName) || removedBranch.contains(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (branchName.equals(curBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        //String test = getCommitNode(branchPointer.get(curBranch)).commitMessage ;
        //String wtf = head.NameAndID.get("halo!.txt");

        String curID = branchPointer.get(branchName);
        CommitNode curNode = getCommitNode(curID);
        //working directory


        for (File f: working.listFiles()) {
            //String test2 = head.commitMessage;
            if (!head.nameAndID.containsKey(f.getName())
                    && curNode.nameAndID.containsKey(f.getName())) {
                System.out.println("There is an untracked file "
                        + "in the way; delete it or add it first.");
                System.exit(0);
            }

            if (!(curNode.nameAndID.containsKey(f.getName()))) {
                Utils.restrictedDelete(f);
            }
        }

        for (String s: curNode.nameAndID.keySet()) {
            checkoutIDFile(curID, s);
        }


        //stagingArea
        if (!(branchName.equals(curBranch))) {
            staging.stagedID.clear();
        }
        //tree
        head = curNode;
        curBranch = branchName;

        Serial.serialize(infile2, staging);
    }

    public void reset(String commitID) {
        String pwd = System.getProperty("user.dir");
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);


        if (!nodePointer.containsKey(commitID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        CommitNode curNode = getCommitNode(commitID);
        String branchName = allNodesPointer.get(commitID);


        File working = new File(System.getProperty("user.dir"));

        for (File f: working.listFiles()) {
            //String test2 = head.commitMessage;
            if (!head.nameAndID.containsKey(f.getName())
                    && curNode.nameAndID.containsKey(f.getName())) {
                System.out.println("There is an untracked file "
                        + "in the way; delete it or add it first.");
                System.exit(0);
            }

            if (!(curNode.nameAndID.containsKey(f.getName()))) {
                Utils.restrictedDelete(f);
            }
        }

        for (String s: curNode.nameAndID.keySet()) {
            checkoutIDFile(commitID, s);
        }


        //if (!(branchName.equals(curBranch))) {
          //  staging.stagedID.clear();
        //}

        //tree
        head = curNode;
        curBranch = branchName;
        staging.stagedID = new TreeMap<>();

        Serial.serialize(infile2, staging);


    }
}
