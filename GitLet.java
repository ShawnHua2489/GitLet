package gitlet;




import java.io.File;

public class GitLet {


    public void init() {
        File pwd = new File(System.getProperty("user.dir"));
        File gitletDir = new File(pwd, ".gitlet");


        if (gitletDir.exists()) {
            System.out.println("A gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
            //return;
        }

        gitletDir.mkdir();

        CommitTree tree = new CommitTree();
        //System.out.println(tree.nodePointer);
        File ctree = new File(gitletDir, "CommitTree");
        Serial.serialize(ctree, tree);

        StagingArea staging = new StagingArea();
        File sarea = new File(gitletDir, "StagingArea");
        Serial.serialize(sarea, staging);

    }

    public void status() {
        //branches
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
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
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        System.out.println("=== Staged Files ===");
        if (staging.stagedID.keySet() == null) {
            System.out.println();
        } else {
            for (String s : staging.stagedID.keySet()) {
                System.out.println(s);
            }
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
        String pwd = System.getProperty("user.dir");
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        //System.out.println(staging.stagedID.isEmpty());

        if (message.length() == 0) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (staging.sISize() == 0 && staging.rMSize() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }


        staging.commit(message);

        File sarea = new File(pwd + "/.gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);

    }

    public void add(String filename) {
        String pwd = System.getProperty("user.dir");
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);

        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        //System.out.println(tree.nodePointer);


        File working = new File(System.getProperty("user.dir"));

        boolean fileExist = false;

        for (File f: working.listFiles()) {
            if (f.getName().equals(filename)) {
                staging.add(filename, f);
                fileExist = true;
                break;
            }
        }

        if (!fileExist) {
            System.out.println("File does not exist");
            System.exit(0);
        }


        // check whether the file is the same as the file in the head commit
        // if not: add

        //System.out.println(staging.stagedID);
        //System.out.println(staging.stagedBlobs);

        File sarea = new File(pwd + "/.gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
        //System.out.println(staging.stagedID.isEmpty());

    }

    public void rm(String filename) {
        String pwd = System.getProperty("user.dir");
        File infile2 = new File(pwd + "/.gitlet" + "/StagingArea");
        StagingArea staging = (StagingArea) Serial.deserialize(infile2);
        //File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        //CommitTree tree = (CommitTree) Serial.deserialize(infile);

        staging.rm(filename);

        //File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        //Serial.serialize(ctree, tree);
        File sarea = new File(pwd + "/.gitlet" + "/StagingArea");
        Serial.serialize(sarea, staging);
    }

    public void log() {

        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        //System.out.println(tree.head.parentID);

        tree.log();
    }

    public void globalLog() {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.globalLog();
    }

    public void find(String message) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.find(message);
    }

    public void checkout(String[] args) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        if (args.length == 2) {
            tree.checkoutBranch(args[1]);
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands");
                System.exit(0);
            }
            tree.checkoutFile(args[2]);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands");
                System.exit(0);
            }
            tree.checkoutIDFile(args[1], args[3]);
        }

        File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        Serial.serialize(ctree, tree);

    }

    public void branch(String branchName) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.addBranch(branchName);

        File ctree = new File(pwd + "/.gitlet" + "/CommitTree"); //?
        Serial.serialize(ctree, tree);
    }

    public void rmBranch(String branchName) {
        String pwd = System.getProperty("user.dir");
        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.rmBranch(branchName);

        File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        Serial.serialize(ctree, tree);
    }

    public void reset(String commitID) {
        String pwd = System.getProperty("user.dir");

        File infile = new File(pwd + "/.gitlet" + "/CommitTree");
        CommitTree tree = (CommitTree) Serial.deserialize(infile);

        tree.reset(commitID);

        File ctree = new File(pwd + "/.gitlet" + "/CommitTree");
        Serial.serialize(ctree, tree);

    }



    public static void main(String... args) {
        GitLet s = new GitLet();
        s.init();

        //s.status();
        //s.log();
        //s.globalLog();
        //s.rmBranch("hallo!");

        s.add("proj2.iml");
        s.add("halo!.txt");
        s.add("laugh.txt");
        s.commit("first time");
        s.branch("first branch");
        s.checkout(new String[]{"checkout,",  "first branch"});

        s.rm("halo!.txt");
        s.commit("aha!!");

        s.branch("second branch");
        s.checkout(new String[]{"checkout,",  "second branch"});

        s.rm("laugh.txt");
        s.commit("yoho");
        s.rmBranch("second branch");

        s.globalLog();

        s.checkout(new String[]{"checkout,",  "master"});

        //s.commit("yoho");


        //s.checkout(new String[]{"checkout,", "++", "halo!.txt"});


        //s.add("halo!.txt");
        //s.status();





        //s.checkout(new String[]{"checkout,",  "master"});







        //s.rm("halo!.txt");
        //s.status();




        //s.checkout(new String[]{"funny"});
        //s.rmBranch("funny");

        //s.rm("laugh.txt");

        //s.globalLog();
        //s.log();
        s.find("whatever");
        //s.rm("halo!.txt");
        //s.status();
        //s.globalLog();


        //s.branch("funny");
        //s.checkout(new String[]{"checkout,",  "funny"});


    }

}
