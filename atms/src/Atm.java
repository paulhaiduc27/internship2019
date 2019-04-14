import java.time.LocalTime;
import java.util.List;
public class Atm {
    private int id;
    private LocalTime openTime;
    private LocalTime closeTime;
    private List<Integer> distances;

    public Atm(){

    }
    public Atm(int id,LocalTime openTime,LocalTime closeTime,List<Integer> distances)
    {
        this.id=id;
        this.openTime=openTime;
        this.closeTime=closeTime;
        this.distances=distances;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public LocalTime getOpenTime()
    {
        return this.openTime;
    }
    public void setOpenTime(LocalTime openTime)
    {
        this.openTime=openTime;
    }
    public LocalTime getCloseTime()
    {
        return this.closeTime;
    }
    public void setCloseTime(LocalTime closeTime)
    {
        this.closeTime=closeTime;
    }
    public List<Integer> getDistances()
    {
        return this.distances;
    }
    public void setDistances(List<Integer> distances)
    {
        this.distances=distances;
    }
    @Override
    public String toString()
    {
        return "Atm{" +
                "id='" + id + '\'' +
                ", opentime='" + openTime + '\'' +
                ", closetime='" + closeTime +
                ", distances='" + distances +
                "} ";
    }
}
