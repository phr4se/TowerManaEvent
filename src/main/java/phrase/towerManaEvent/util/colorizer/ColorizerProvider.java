package phrase.towerManaEvent.util.colorizer;

public abstract class ColorizerProvider {

    protected final ColorizerSerivce colorizerSerivce;

    public ColorizerProvider(ColorizerSerivce colorizerSerivce) {
        this.colorizerSerivce = colorizerSerivce;
    }

    public abstract String colorize(String message);

}
