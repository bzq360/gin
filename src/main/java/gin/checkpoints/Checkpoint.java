package gin.checkpoints;

public class Checkpoint {

    protected int ID;

    protected String variableName;

    protected int value;

    protected Checkpoint(int ID, String variableName, int value) {
        this.ID = ID;
        this.variableName = variableName;
        this.value = value;
    }

}
