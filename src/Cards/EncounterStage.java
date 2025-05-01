package Cards;
import java.util.List;

public class EncounterStage {
    private final String title;
    private  final String description;
    private final List<StageOption> options;

    public EncounterStage(String title, String description, List<StageOption> options){
        this.title = title;
        this.description = description;
        this.options = options;
    }
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public List<StageOption> getOptions(){return options;}

}
