package phrase.towerManaEvent.util.colorizer;

public abstract class ColorizerProvider {
    protected final ColorizerService colorizerService;

    public ColorizerProvider(ColorizerService colorizerService) {
        this.colorizerService = colorizerService;
    }

    public abstract String colorize(String message);
}
