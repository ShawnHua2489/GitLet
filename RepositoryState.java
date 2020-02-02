package gitlet;




public class RepositoryState {
    /* public void init() {
        File gitletDir = new File(".gitlet");
        gitletDir.mkdir();


        CommitTree tree = new CommitTree();
        File ctree = new File(".gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);

        StagingArea staging = new StagingArea();
        File sarea = new File(".gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

    public void status() {
        //branches
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);
        System.out.println("=== Branches ===");
        System.out.println("*" + tree.curBranch);
        for (String branchName : tree.branchPointer.keySet()) {
            if (branchName != tree.curBranch) {
                System.out.println(branchName);
            }
        }
        System.out.println();

        //staged files
        File infile2 = new File(".gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        System.out.println("=== Staged Files ===");
        for (String s : staging.stagedID.keySet()) {
            System.out.println(s);
        }
        System.out.println();

        //removed
        System.out.println("=== Removed Files ===");
        for (String s : staging.removed) {
            System.out.println(s);
        }
        System.out.println();

        //modifications and untracked
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public void commit(String message) {
        File infile2 = new File(".gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);

        staging.commit(message);

        File sarea = new File(".gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

    public void add(String filename) {
        File infile2 = new File(".gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);

        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        // check whether the file is the same as the file in the head commit
        // if not: add
        staging.add(filename);

        File sarea = new File(".gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

    public void rm(String filename) {
        File infile2 = new File(".gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        staging.rm(filename);

        File ctree = new File(".gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);
        File sarea = new File(".gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

    public void log() {
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.log();
    }

    public void globalLog() {
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.globalLog();
    }

    public void find(String message) {
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.find(message);
    }

    public void checkout(String[] args){
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        if (args.length == 1) {
            //
            tree.checkoutBranch(args[0]);
        } else if (args.length == 2) {
            tree.checkoutIDFile(); //???
        }
    }

    public void branch(String branchName) {
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.addBranch(branchName);

        File ctree = new File(".gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);
    }

    public void rmBranch(String branchName) {
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.rmBranch(branchName);

        File ctree = new File(".gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);
    }

    public void reset(String commitID) {
        File infile2 = new File(".gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        File infile = new File(".gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.reset(commitID);

        File ctree = new File(".gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);
        File sarea = new File(".gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

*/

}

