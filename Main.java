package gitlet;


/* Driver class for Gitlet, the tiny stupid version-control system.
   @author
*/
public class Main {

    /* Usage: java gitlet.Main ARGS, where ARGS contains
       <COMMAND> <OPERAND> .... */
    public static void main(String... args) {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
        }
        boolean init = false;

        GitLet gitLit = new GitLet();
        String input = args[0];
        switch (input) {
            case "init":
                gitLit.init();
                break;
            case "status":
                gitLit.status();
                break;
            case "commit":
                gitLit.commit(args[1]);
                break;
            case "add":

                gitLit.add(args[1]);
                break;
            case "rm":

                gitLit.rm(args[1]);
                break;
            case "log":
                gitLit.log();
                break;
            case "global-log":
                gitLit.globalLog();
                break;
            case "find":
                gitLit.find(args[1]);
                break;
            case "checkout":
                gitLit.checkout(args);
                break;
            case "branch":
                gitLit.branch(args[1]);
                break;
            case "rmBranch":
                gitLit.rmBranch(args[1]);
                break;
            case "reset":
                gitLit.reset(args[1]);
                break;
            case "merge":
                gitLit.globalLog();
                break;
            default:
                break;


        }
    }
}
