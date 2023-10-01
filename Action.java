public class Action {
    public final static int EXIT = 0;
    public final static int PARK = 1;
    public final static int PREPARK = 11;
    public final static int UNPARK = 12;
    public final static int WAIT = 2;
    public final static int ENTER = 3;
    public final static int GO_UP = 4;
    private int id;
    private String name;
    private SceneElement parameter;
    private boolean finished;


    public Action(int id_action, SceneElement... params) {
        this.id = id_action;
        this.finished = false;
        if (id_action == EXIT) {
            this.name = "Exit";
        }
        else if (id_action == PARK) {
            this.name = "Park";
        }
        else if (id_action == PREPARK) {
            this.name = "Pre-park";
        }
        else if (id_action == UNPARK) {
            this.name = "Unpark";
        }
        else if (id_action == WAIT) {
            this.name = "Wait";
            this.finished = true;
        }
        else if (id_action == GO_UP) {
            this.name = "Go up";
        }
        else {
            System.out.println("Continue what?");
            System.exit(0);
        }
        if (params.length > 0)
            this.parameter = params[0];
    }

    public Action getCopy() {
        Action ret = new Action(this.id);
        if (this.parameter != null)
            ret.parameter = this.parameter.getCopy();
        ret.finished = this.finished;
        return ret;
    }

    public int getId() {
        return id;
    }

    public SceneElement getParameter() {
        return parameter;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getName() {
        return name;
    }
}
