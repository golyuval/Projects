package defult.BusinessLayer.DeliverySystem.Forms;

import defult.DataAccessLayer.Transport.DTO.OverLoadDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class OverLoad implements FormType
{




    // -------- Variables ----------------------------------------------------------------------------------------------------------

    static public int overload_counter = 0;

    final public int overLoadID;
    final public int _selected_solution;

    public LocalDate _currentDate;
    public LocalTime _currentTime;


    // -------- Constructors ----------------------------------------------------------------------------------------------------------

    public OverLoad(int sol,int id, LocalDate date, LocalTime time)
    {
        overLoadID = id;
        _selected_solution = sol;
        _currentDate = date;
        _currentTime = time;
    }
    public OverLoad(int sol)
    {
        overload_counter++;
        overLoadID = overload_counter;
        _selected_solution = sol;
        _currentDate = LocalDate.now();
        _currentTime = LocalTime.now();
    }

    public OverLoadDTO toDTO(){
        return new OverLoadDTO(overLoadID,_selected_solution, _currentDate.toString(),  _currentTime.toString());
    }

    // -------- Printers ----------------------------------------------------------------------------------------------------------------


    public String printOverLoad(){
        String output = "\noverload ID:\t" + overLoadID + "\nselected solution to fix over weight:\t" + _selected_solution;
        output += "\nDate:\t" + _currentDate.toString() + "\nTime:\t" + _currentTime.toString();
        return output;
    }

    @Override
    public String light_toString() {
        String s = "";
        s+= "Overload ID: " + overLoadID + "\t ";
        s+= "Date: " + _currentDate + "\t ";
        s+= "Time: " + _currentTime + "\t ";
        s+= "Solution: " + _selected_solution + "\t ";

        return  s;
    }


    // -------- Getters / Setters ----------------------------------------------------------------------------------------------------------

    public int get_overload_ID () {
        return overLoadID;
    }

    public LocalDate get_currentDate() {
        return _currentDate;
    }

    public LocalTime get_currentTime() {
        return _currentTime;
    }

    public int get_selected_solution() {
        return _selected_solution;
    }
}