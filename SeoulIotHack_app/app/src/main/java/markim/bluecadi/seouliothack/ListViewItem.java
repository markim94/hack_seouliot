package markim.bluecadi.seouliothack;

public class ListViewItem {
    private String txtFloor;
    private String txtPerson;
    private String txtState;


    public void setFloor(String floor){
        txtFloor = floor;
    }

    public void setPerson(String person){
        txtPerson = person;
    }

    public void setState(String state) { txtState = state; }

    public String getFloor(){
        return this.txtFloor;
    }

    public String getPerson(){
        return this.txtPerson;
    }

    public String getState() { return this.txtState; }

}