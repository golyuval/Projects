package defult.DataAccessLayer.Transport.DTO;
import java.time.LocalDate;
import java.time.LocalTime;

public class OverLoadDTO extends DTO {


    // ----------- Fields ----------------------------------------------------------------------------------------------

    private int overLoadID;
    private int selectedSolution;
    private String currentDate;
    private String currentTime;


    // ----------- Constructor ----------------------------------------------------------------------------------------------

    public OverLoadDTO(int ov_id, int selectedSolution, String currentDate, String currentTime) {
        this.overLoadID = ov_id;
        this.selectedSolution = selectedSolution;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }



    // ----------- Getter / Setter ----------------------------------------------------------------------------------------------

    public int getOv_id() {
        return overLoadID;
    }

    public int getselectedSolution() {
        return selectedSolution;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setOv_id(int ov_id) {
        this.overLoadID = ov_id;
    }

    public void setselectedSolution(int selectedSolution) {
        this.selectedSolution = selectedSolution;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}