public class Action {
    public final static int EXIT = 0;
    public final static int PARK = 1;
    public final static int WAIT = 2;
    public final static int CONTINUE = 3;
    int id;
    String name;
    SceneElement parameter;


    public Action(int id_action, SceneElement... params) {
        this.id = id_action;
        if (id_action == EXIT) this.name = "Exit";
        else if (id_action == PARK) this.name = "Park";
        else if (id_action == WAIT) this.name = "Wait";
        else {
            System.out.println("Continue what?");
            System.exit(0);
        }
        if (params.length > 0)
            this.parameter = params[0];
    }

    public Action getCopy() {
        Action ret = new Action(this.id);
        //if (this.parameter != null)
        //    ret.parameter = this.parameter.getCopy();
        ret.parameter = this.parameter;
        return ret;
    }

    public int getId() {
        return id;
    }
}
