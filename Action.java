public class Action {
    String name;
    SceneElement parameter;

    public Action(String name, SceneElement... params) {
        this.name = name;
        if (params.length > 0)
            this.parameter = params[0];
    }
}
